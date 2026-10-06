package com.example.ui

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CropPortrait
import androidx.compose.material.icons.filled.Extension
import androidx.compose.material.icons.filled.Fullscreen
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Wallpaper
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.PhoneApp
import com.example.ui.apps.AppStoreView
import com.example.ui.apps.DialerAppView
import com.example.ui.apps.MessagesAppView
import com.example.ui.apps.SettingsAppView
import com.example.ui.browser.ChromeBrowserView
import com.example.ui.components.PhoneFrameContainer
import com.example.ui.components.RecentAppsView
import com.example.ui.components.VirtualNavigationBar
import com.example.ui.components.VirtualStatusBar
import com.example.ui.theme.DarkBorder
import com.example.ui.theme.DarkSurface
import com.example.ui.theme.NeonGreen
import com.example.ui.theme.ObsidianBlack
import com.example.ui.vpn.GreenVpnAppView
import com.example.viewmodel.VirtualPhoneViewModel

/**
 * Main Virtual Phone screen holding the phone chassis, active apps, and OS navigation.
 */
@Composable
fun VirtualPhoneScreen(
    viewModel: VirtualPhoneViewModel,
    modifier: Modifier = Modifier
) {
    val phoneState by viewModel.phoneState.collectAsState()
    val vpnState by viewModel.vpnState.collectAsState()
    val tabs by viewModel.tabs.collectAsState()
    val activeTabId by viewModel.activeTabId.collectAsState()
    val isExtensionPopupVisible by viewModel.isExtensionPopupVisible.collectAsState()
    val currentTime by viewModel.currentTime.collectAsState()
    val dialerNumber by viewModel.dialerNumber.collectAsState()
    val callActive by viewModel.callActive.collectAsState()
    val callDuration by viewModel.callDuration.collectAsState()

    // Android Hardware Back Handler
    BackHandler {
        viewModel.pressBack()
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(ObsidianBlack)
    ) {
        // ----------------------------------------------------
        // Virtual Phone Master Control Bar (Device Mode & VPN)
        // ----------------------------------------------------
        Column(modifier = Modifier.fillMaxWidth()) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFF0A0F0D))
                    .padding(horizontal = 12.dp, vertical = 6.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
            // Brand & OS label
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(20.dp)
                        .clip(RoundedCornerShape(5.dp))
                        .background(NeonGreen),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(imageVector = Icons.Default.Security, contentDescription = null, tint = ObsidianBlack, modifier = Modifier.size(13.dp))
                }
                Text(
                    text = "Virtual Phone",
                    color = Color.White,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "• Chrome + Green VPN",
                    color = NeonGreen,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Medium
                )
            }

            // Quick Host Controls (Chassis Toggle & Wallpaper Cycle)
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // Wallpaper switcher button
                Row(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(Color(0xFF1B2420))
                        .clickable { viewModel.cycleWallpaper() }
                        .padding(horizontal = 8.dp, vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Icon(imageVector = Icons.Default.Wallpaper, contentDescription = "Wallpaper", tint = Color.White.copy(alpha = 0.8f), modifier = Modifier.size(12.dp))
                    Text(text = "Theme", color = Color.White.copy(alpha = 0.8f), fontSize = 10.sp)
                }

                // Chassis Frame / Fullscreen toggle
                Row(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(if (phoneState.isChassisMode) NeonGreen.copy(alpha = 0.2f) else Color(0xFF1B2420))
                        .border(1.dp, if (phoneState.isChassisMode) NeonGreen.copy(alpha = 0.5f) else Color.Transparent, RoundedCornerShape(8.dp))
                        .clickable { viewModel.toggleChassisMode() }
                        .padding(horizontal = 8.dp, vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Icon(
                        imageVector = if (phoneState.isChassisMode) Icons.Default.CropPortrait else Icons.Default.Fullscreen,
                        contentDescription = "Chassis Frame",
                        tint = if (phoneState.isChassisMode) NeonGreen else Color.White.copy(alpha = 0.8f),
                        modifier = Modifier.size(12.dp)
                    )
                    Text(
                        text = if (phoneState.isChassisMode) "Frame ON" else "Full OS",
                        color = if (phoneState.isChassisMode) NeonGreen else Color.White.copy(alpha = 0.8f),
                        fontSize = 10.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
            androidx.compose.material3.HorizontalDivider(color = DarkBorder)
        }

        // ----------------------------------------------------
        // Virtual Phone Screen Enclosure
        // ----------------------------------------------------
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        ) {
            PhoneFrameContainer(isChassisMode = phoneState.isChassisMode) {
                Column(modifier = Modifier.fillMaxSize()) {
                    // Top Virtual Status Bar
                    VirtualStatusBar(
                        phoneState = phoneState,
                        vpnState = vpnState,
                        currentTime = currentTime,
                        onVpnBadgeClick = {
                            if (phoneState.currentApp != PhoneApp.CHROME) {
                                viewModel.openApp(PhoneApp.GREEN_VPN)
                            } else {
                                viewModel.toggleExtensionPopup()
                            }
                        }
                    )

                    // Active Application Body
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f)
                    ) {
                        when (phoneState.currentApp) {
                            PhoneApp.HOME -> {
                                HomeScreen(
                                    phoneState = phoneState,
                                    vpnState = vpnState,
                                    currentTime = currentTime,
                                    onOpenApp = { viewModel.openApp(it) },
                                    onToggleVpn = { viewModel.toggleVpn() },
                                    onOpenChromeSearch = {
                                        viewModel.openApp(PhoneApp.CHROME)
                                    }
                                )
                            }
                            PhoneApp.CHROME -> {
                                ChromeBrowserView(
                                    tabs = tabs,
                                    activeTabId = activeTabId,
                                    vpnState = vpnState,
                                    isExtensionPopupVisible = isExtensionPopupVisible,
                                    onToggleExtensionPopup = { viewModel.toggleExtensionPopup() },
                                    onCloseExtensionPopup = { viewModel.closeExtensionPopup() },
                                    onToggleVpn = { viewModel.toggleVpn() },
                                    onSelectServer = { viewModel.selectServer(it) },
                                    onToggleAdBlocker = { viewModel.toggleAdBlocker() },
                                    onToggleWebRtc = { viewModel.toggleWebRtcProtection() },
                                    onToggleKillSwitch = { viewModel.toggleKillSwitch() },
                                    onOpenFullVpnApp = {
                                        viewModel.closeExtensionPopup()
                                        viewModel.openApp(PhoneApp.GREEN_VPN)
                                    },
                                    onNewTab = { viewModel.openNewTab(it) },
                                    onCloseTab = { viewModel.closeTab(it) },
                                    onSelectTab = { viewModel.selectTab(it) },
                                    onNavigateUrl = { viewModel.updateCurrentTabUrl(it) },
                                    onUpdateTabDetails = { id, title, url, canBack, canFwd ->
                                        viewModel.updateTabDetails(id, title, url, canBack, canFwd)
                                    },
                                    onUpdateTabProgress = { id, loading, progress ->
                                        viewModel.setTabLoadingProgress(id, loading, progress)
                                    }
                                )
                            }
                            PhoneApp.GREEN_VPN -> {
                                GreenVpnAppView(
                                    vpnState = vpnState,
                                    onToggleVpn = { viewModel.toggleVpn() },
                                    onSelectServer = { viewModel.selectServer(it) },
                                    onToggleAdBlocker = { viewModel.toggleAdBlocker() },
                                    onToggleWebRtc = { viewModel.toggleWebRtcProtection() },
                                    onToggleKillSwitch = { viewModel.toggleKillSwitch() },
                                    onSelectProtocol = { viewModel.selectProtocol(it) },
                                    onOpenChrome = { viewModel.openApp(PhoneApp.CHROME) }
                                )
                            }
                            PhoneApp.DIALER -> {
                                DialerAppView(
                                    dialerNumber = dialerNumber,
                                    callActive = callActive,
                                    callDuration = callDuration,
                                    onAppendDigit = { viewModel.appendDialerDigit(it) },
                                    onBackspace = { viewModel.backspaceDialer() },
                                    onStartCall = { viewModel.startCall() },
                                    onEndCall = { viewModel.endCall() }
                                )
                            }
                            PhoneApp.MESSAGES -> {
                                MessagesAppView()
                            }
                            PhoneApp.SETTINGS -> {
                                SettingsAppView(
                                    phoneState = phoneState,
                                    vpnState = vpnState,
                                    onToggleChassisMode = { viewModel.toggleChassisMode() },
                                    onCycleWallpaper = { viewModel.cycleWallpaper() }
                                )
                            }
                            PhoneApp.APP_STORE -> {
                                AppStoreView(
                                    onOpenChrome = { viewModel.openApp(PhoneApp.CHROME) }
                                )
                            }
                        }

                        // Recent Apps Multi-Tasking Overlay
                        if (phoneState.showRecentApps) {
                            RecentAppsView(
                                recentApps = phoneState.recentAppsList,
                                onSelectApp = { viewModel.openApp(it) },
                                onCloseRecents = { viewModel.toggleRecentApps() }
                            )
                        }
                    }

                    // Bottom Virtual 3-Button Navigation Bar
                    VirtualNavigationBar(
                        onBack = { viewModel.pressBack() },
                        onHome = { viewModel.pressHome() },
                        onRecents = { viewModel.toggleRecentApps() }
                    )
                }
            }
        }
    }
}
}
