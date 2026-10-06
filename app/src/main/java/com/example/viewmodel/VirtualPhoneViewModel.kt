package com.example.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.model.BrowserTab
import com.example.model.DEFAULT_SERVERS
import com.example.model.PhoneApp
import com.example.model.VirtualPhoneState
import com.example.model.VpnServer
import com.example.model.VpnState
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.UUID
import kotlin.random.Random

class VirtualPhoneViewModel : ViewModel() {

    // --- VPN State ---
    private val _vpnState = MutableStateFlow(VpnState())
    val vpnState: StateFlow<VpnState> = _vpnState.asStateFlow()

    // --- Browser State ---
    private val initialTab = BrowserTab(
        id = UUID.randomUUID().toString(),
        title = "Google",
        url = "https://www.google.com"
    )
    private val _tabs = MutableStateFlow<List<BrowserTab>>(listOf(initialTab))
    val tabs: StateFlow<List<BrowserTab>> = _tabs.asStateFlow()

    private val _activeTabId = MutableStateFlow(initialTab.id)
    val activeTabId: StateFlow<String> = _activeTabId.asStateFlow()

    private val _isExtensionPopupVisible = MutableStateFlow(false)
    val isExtensionPopupVisible: StateFlow<Boolean> = _isExtensionPopupVisible.asStateFlow()

    private val _isTabSwitcherVisible = MutableStateFlow(false)
    val isTabSwitcherVisible: StateFlow<Boolean> = _isTabSwitcherVisible.asStateFlow()

    private val _isChromeMenuVisible = MutableStateFlow(false)
    val isChromeMenuVisible: StateFlow<Boolean> = _isChromeMenuVisible.asStateFlow()

    // --- Phone State ---
    private val _phoneState = MutableStateFlow(VirtualPhoneState())
    val phoneState: StateFlow<VirtualPhoneState> = _phoneState.asStateFlow()

    // --- Dialer & Messages State ---
    private val _dialerNumber = MutableStateFlow("")
    val dialerNumber: StateFlow<String> = _dialerNumber.asStateFlow()

    private val _callActive = MutableStateFlow(false)
    val callActive: StateFlow<Boolean> = _callActive.asStateFlow()

    private val _callDuration = MutableStateFlow(0)
    val callDuration: StateFlow<Int> = _callDuration.asStateFlow()

    // --- Clock State ---
    private val _currentTime = MutableStateFlow(getCurrentFormattedTime())
    val currentTime: StateFlow<String> = _currentTime.asStateFlow()

    private var trafficSimulationJob: Job? = null
    private var callTimerJob: Job? = null

    init {
        startClockTicker()
    }

    private fun getCurrentFormattedTime(): String {
        val sdf = SimpleDateFormat("h:mm a", Locale.getDefault())
        return sdf.format(Date())
    }

    private fun startClockTicker() {
        viewModelScope.launch {
            while (true) {
                delay(10000)
                _currentTime.value = getCurrentFormattedTime()
            }
        }
    }

    // ==========================================
    // VPN Operations
    // ==========================================

    fun toggleVpn() {
        if (_vpnState.value.isConnected) {
            disconnectVpn()
        } else {
            connectVpn()
        }
    }

    fun connectVpn() {
        if (_vpnState.value.isConnected || _vpnState.value.isConnecting) return

        viewModelScope.launch {
            _vpnState.update { it.copy(isConnecting = true) }
            delay(1200) // Realistic handshake / TLS key exchange simulation
            _vpnState.update {
                it.copy(
                    isConnecting = false,
                    isConnected = true,
                    connectedDurationSeconds = 0L,
                    currentVirtualIp = it.selectedServer.ipAddress
                )
            }
            startTrafficSimulation()
        }
    }

    fun disconnectVpn() {
        trafficSimulationJob?.cancel()
        trafficSimulationJob = null
        _vpnState.update {
            it.copy(
                isConnecting = false,
                isConnected = false,
                uploadSpeedKbps = 0.0,
                downloadSpeedKbps = 0.0
            )
        }
    }

    fun selectServer(server: VpnServer) {
        val wasConnected = _vpnState.value.isConnected
        _vpnState.update { it.copy(selectedServer = server) }
        if (wasConnected) {
            // Reconnect to new server
            disconnectVpn()
            connectVpn()
        }
    }

    fun toggleKillSwitch() {
        _vpnState.update { it.copy(killSwitchEnabled = !it.killSwitchEnabled) }
    }

    fun toggleAdBlocker() {
        _vpnState.update { it.copy(adBlockerEnabled = !it.adBlockerEnabled) }
    }

    fun toggleWebRtcProtection() {
        _vpnState.update { it.copy(webRtcProtectionEnabled = !it.webRtcProtectionEnabled) }
    }

    fun selectProtocol(protocolName: String) {
        _vpnState.update { it.copy(selectedProtocol = protocolName) }
    }

    private fun startTrafficSimulation() {
        trafficSimulationJob?.cancel()
        trafficSimulationJob = viewModelScope.launch {
            while (_vpnState.value.isConnected) {
                delay(1000)
                val dlSpike = Random.nextDouble(12.5, 94.8)
                val ulSpike = Random.nextDouble(4.1, 28.3)
                val addedBytes = ((dlSpike + ulSpike) * 1024 * 128).toLong()

                _vpnState.update { current ->
                    current.copy(
                        downloadSpeedKbps = dlSpike,
                        uploadSpeedKbps = ulSpike,
                        totalBytesEncrypted = current.totalBytesEncrypted + addedBytes,
                        connectedDurationSeconds = current.connectedDurationSeconds + 1L,
                        blockedTrackersCount = current.blockedTrackersCount + if (Random.nextInt(5) == 0) 1 else 0
                    )
                }
            }
        }
    }

    // ==========================================
    // Chrome Browser Operations
    // ==========================================

    fun toggleExtensionPopup() {
        _isExtensionPopupVisible.update { !it }
        if (_isExtensionPopupVisible.value) {
            _isChromeMenuVisible.value = false
        }
    }

    fun closeExtensionPopup() {
        _isExtensionPopupVisible.value = false
    }

    fun toggleChromeMenu() {
        _isChromeMenuVisible.update { !it }
        if (_isChromeMenuVisible.value) {
            _isExtensionPopupVisible.value = false
        }
    }

    fun closeChromeMenu() {
        _isChromeMenuVisible.value = false
    }

    fun toggleTabSwitcher() {
        _isTabSwitcherVisible.update { !it }
    }

    fun openNewTab(url: String = "https://www.google.com", isIncognito: Boolean = false) {
        val newTab = BrowserTab(
            id = UUID.randomUUID().toString(),
            title = if (url.contains("google.com")) "Google" else "New Tab",
            url = url,
            isIncognito = isIncognito
        )
        _tabs.update { it + newTab }
        _activeTabId.value = newTab.id
        _isTabSwitcherVisible.value = false
    }

    fun closeTab(tabId: String) {
        val currentTabs = _tabs.value
        if (currentTabs.size <= 1) {
            // Keep at least one tab
            val resetTab = BrowserTab(
                id = UUID.randomUUID().toString(),
                title = "Google",
                url = "https://www.google.com"
            )
            _tabs.value = listOf(resetTab)
            _activeTabId.value = resetTab.id
            return
        }

        val updatedTabs = currentTabs.filter { it.id != tabId }
        _tabs.value = updatedTabs
        if (_activeTabId.value == tabId) {
            _activeTabId.value = updatedTabs.last().id
        }
    }

    fun selectTab(tabId: String) {
        _activeTabId.value = tabId
        _isTabSwitcherVisible.value = false
    }

    fun updateCurrentTabUrl(newUrl: String) {
        val activeId = _activeTabId.value
        _tabs.update { list ->
            list.map { tab ->
                if (tab.id == activeId) {
                    val formattedUrl = when {
                        newUrl.startsWith("http://") || newUrl.startsWith("https://") -> newUrl
                        newUrl.contains(".") && !newUrl.contains(" ") -> "https://$newUrl"
                        else -> "https://www.google.com/search?q=${java.net.URLEncoder.encode(newUrl, "UTF-8")}"
                    }
                    tab.copy(url = formattedUrl, isLoading = true, progress = 0.2f)
                } else tab
            }
        }
    }

    fun setTabLoadingProgress(tabId: String, isLoading: Boolean, progress: Float) {
        _tabs.update { list ->
            list.map { tab ->
                if (tab.id == tabId) {
                    tab.copy(isLoading = isLoading, progress = progress)
                } else tab
            }
        }
    }

    fun updateTabDetails(tabId: String, title: String, url: String, canGoBack: Boolean, canGoForward: Boolean) {
        _tabs.update { list ->
            list.map { tab ->
                if (tab.id == tabId) {
                    tab.copy(
                        title = title.ifEmpty { "Web Page" },
                        url = url,
                        canGoBack = canGoBack,
                        canGoForward = canGoForward
                    )
                } else tab
            }
        }
    }

    // ==========================================
    // Virtual Phone OS Navigation
    // ==========================================

    fun openApp(app: PhoneApp) {
        _phoneState.update { current ->
            val updatedRecents = (listOf(app) + current.recentAppsList.filter { it != app }).take(6)
            current.copy(
                previousApp = current.currentApp,
                currentApp = app,
                showRecentApps = false,
                recentAppsList = updatedRecents
            )
        }
    }

    fun pressHome() {
        _isExtensionPopupVisible.value = false
        _isChromeMenuVisible.value = false
        _isTabSwitcherVisible.value = false
        _phoneState.update {
            it.copy(
                previousApp = it.currentApp,
                currentApp = PhoneApp.HOME,
                showRecentApps = false
            )
        }
    }

    fun pressBack() {
        if (_isExtensionPopupVisible.value) {
            _isExtensionPopupVisible.value = false
            return
        }
        if (_isChromeMenuVisible.value) {
            _isChromeMenuVisible.value = false
            return
        }
        if (_isTabSwitcherVisible.value) {
            _isTabSwitcherVisible.value = false
            return
        }
        if (_phoneState.value.showRecentApps) {
            _phoneState.update { it.copy(showRecentApps = false) }
            return
        }
        if (_phoneState.value.currentApp != PhoneApp.HOME) {
            _phoneState.update { it.copy(currentApp = PhoneApp.HOME) }
        }
    }

    fun toggleRecentApps() {
        _phoneState.update { it.copy(showRecentApps = !it.showRecentApps) }
    }

    fun toggleChassisMode() {
        _phoneState.update { it.copy(isChassisMode = !it.isChassisMode) }
    }

    fun cycleWallpaper() {
        _phoneState.update { it.copy(wallpaperIndex = (it.wallpaperIndex + 1) % 4) }
    }

    // ==========================================
    // Phone Dialer Simulation
    // ==========================================

    fun appendDialerDigit(digit: String) {
        _dialerNumber.update { it + digit }
    }

    fun backspaceDialer() {
        _dialerNumber.update { if (it.isNotEmpty()) it.dropLast(1) else "" }
    }

    fun clearDialer() {
        _dialerNumber.value = ""
    }

    fun startCall() {
        if (_dialerNumber.value.isNotEmpty()) {
            _callActive.value = true
            _callDuration.value = 0
            callTimerJob?.cancel()
            callTimerJob = viewModelScope.launch {
                while (_callActive.value) {
                    delay(1000)
                    _callDuration.update { it + 1 }
                }
            }
        }
    }

    fun endCall() {
        _callActive.value = false
        callTimerJob?.cancel()
        callTimerJob = null
    }
}
