package com.example.ui.browser

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.view.ViewGroup
import android.webkit.WebChromeClient
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Extension
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.VpnKey
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import com.example.model.BrowserTab
import com.example.model.DEFAULT_BOOKMARKS
import com.example.model.VpnServer
import com.example.model.VpnState
import com.example.ui.theme.ChromeBorder
import com.example.ui.theme.ChromeDarkBg
import com.example.ui.theme.ChromeOmniboxBg
import com.example.ui.theme.ChromeTabActive
import com.example.ui.theme.ChromeTabInactive
import com.example.ui.theme.DarkBorder
import com.example.ui.theme.DarkSurface
import com.example.ui.theme.DarkSurfaceElevated
import com.example.ui.theme.NeonGreen
import com.example.ui.theme.ObsidianBlack

/**
 * Full Chrome Browser experience inside the Virtual Phone.
 * Contains tabs, omnibox, extensions button with Green VPN extension installed, and WebView.
 */
@SuppressLint("SetJavaScriptEnabled")
@Composable
fun ChromeBrowserView(
    tabs: List<BrowserTab>,
    activeTabId: String,
    vpnState: VpnState,
    isExtensionPopupVisible: Boolean,
    onToggleExtensionPopup: () -> Unit,
    onCloseExtensionPopup: () -> Unit,
    onToggleVpn: () -> Unit,
    onSelectServer: (VpnServer) -> Unit,
    onToggleAdBlocker: () -> Unit,
    onToggleWebRtc: () -> Unit,
    onToggleKillSwitch: () -> Unit,
    onOpenFullVpnApp: () -> Unit,
    onNewTab: (String) -> Unit,
    onCloseTab: (String) -> Unit,
    onSelectTab: (String) -> Unit,
    onNavigateUrl: (String) -> Unit,
    onUpdateTabDetails: (String, String, String, Boolean, Boolean) -> Unit,
    onUpdateTabProgress: (String, Boolean, Float) -> Unit,
    modifier: Modifier = Modifier
) {
    val activeTab = tabs.find { it.id == activeTabId } ?: tabs.firstOrNull() ?: BrowserTab("default", "Google", "https://www.google.com")
    var urlInputText by remember(activeTab.url) { mutableStateOf(activeTab.url) }
    var isEditingUrl by remember { mutableStateOf(false) }
    var showChromeMenu by remember { mutableStateOf(false) }
    var showBookmarksBar by remember { mutableStateOf(true) }
    var webViewInstance by remember { mutableStateOf<WebView?>(null) }
    val focusManager = LocalFocusManager.current

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(ChromeDarkBg)
    ) {
        // ----------------------------------------------------
        // Chrome Top Tabs Strip
        // ----------------------------------------------------
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(34.dp)
                .background(Color(0xFF141414))
                .horizontalScroll(rememberScrollState())
                .padding(horizontal = 4.dp, vertical = 2.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            tabs.forEach { tab ->
                val isActive = tab.id == activeTabId
                Row(
                    modifier = Modifier
                        .height(30.dp)
                        .clip(RoundedCornerShape(topStart = 8.dp, topEnd = 8.dp))
                        .background(if (isActive) ChromeTabActive else ChromeTabInactive)
                        .clickable { onSelectTab(tab.id) }
                        .padding(horizontal = 8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Icon(
                        imageVector = if (tab.isIncognito) Icons.Default.Shield else Icons.Default.Language,
                        contentDescription = "Tab Favicon",
                        tint = if (isActive) NeonGreen else Color.White.copy(alpha = 0.5f),
                        modifier = Modifier.size(12.dp)
                    )
                    Text(
                        text = tab.title,
                        color = if (isActive) Color.White else Color.White.copy(alpha = 0.6f),
                        fontSize = 11.sp,
                        fontWeight = if (isActive) FontWeight.SemiBold else FontWeight.Normal,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.width(80.dp)
                    )
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Close Tab",
                        tint = Color.White.copy(alpha = 0.6f),
                        modifier = Modifier
                            .size(14.dp)
                            .clickable { onCloseTab(tab.id) }
                    )
                }
                Spacer(modifier = Modifier.width(2.dp))
            }

            // New Tab (+) Button
            IconButton(
                onClick = { onNewTab("https://www.google.com") },
                modifier = Modifier
                    .size(26.dp)
                    .testTag("chrome_new_tab_button")
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "New Tab",
                    tint = Color.White.copy(alpha = 0.8f),
                    modifier = Modifier.size(16.dp)
                )
            }
        }

        // ----------------------------------------------------
        // Chrome Omnibox Toolbar (Address bar + Extension button)
        // ----------------------------------------------------
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(46.dp)
                .background(ChromeDarkBg)
                .padding(horizontal = 4.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(2.dp)
        ) {
            // Back button
            IconButton(
                onClick = { webViewInstance?.goBack() },
                enabled = webViewInstance?.canGoBack() == true,
                modifier = Modifier.size(32.dp)
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back",
                    tint = if (webViewInstance?.canGoBack() == true) Color.White else Color.White.copy(alpha = 0.3f),
                    modifier = Modifier.size(16.dp)
                )
            }

            // Forward button
            IconButton(
                onClick = { webViewInstance?.goForward() },
                enabled = webViewInstance?.canGoForward() == true,
                modifier = Modifier.size(32.dp)
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                    contentDescription = "Forward",
                    tint = if (webViewInstance?.canGoForward() == true) Color.White else Color.White.copy(alpha = 0.3f),
                    modifier = Modifier.size(16.dp)
                )
            }

            // Reload button
            IconButton(
                onClick = { webViewInstance?.reload() },
                modifier = Modifier.size(32.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Refresh,
                    contentDescription = "Reload",
                    tint = Color.White.copy(alpha = 0.85f),
                    modifier = Modifier.size(16.dp)
                )
            }

            // Omnibox URL Box
            Box(
                modifier = Modifier
                    .weight(1f)
                    .height(34.dp)
                    .clip(RoundedCornerShape(18.dp))
                    .background(ChromeOmniboxBg)
                    .border(
                        1.dp,
                        if (vpnState.isConnected) NeonGreen.copy(alpha = 0.5f) else ChromeBorder,
                        RoundedCornerShape(18.dp)
                    )
                    .padding(horizontal = 8.dp),
                contentAlignment = Alignment.CenterStart
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    // Lock icon indicating HTTPS & Green VPN encrypted tunnel
                    Icon(
                        imageVector = Icons.Default.Lock,
                        contentDescription = "Security Status",
                        tint = if (vpnState.isConnected) NeonGreen else Color.White.copy(alpha = 0.7f),
                        modifier = Modifier.size(13.dp)
                    )

                    BasicTextField(
                        value = urlInputText,
                        onValueChange = { urlInputText = it },
                        modifier = Modifier
                            .weight(1f)
                            .testTag("chrome_url_input")
                            .onFocusChanged { isEditingUrl = it.isFocused },
                        textStyle = TextStyle(
                            color = Color.White,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Normal
                        ),
                        singleLine = true,
                        cursorBrush = SolidColor(NeonGreen),
                        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Go),
                        keyboardActions = KeyboardActions(onGo = {
                            focusManager.clearFocus()
                            onNavigateUrl(urlInputText)
                        })
                    )

                    if (isEditingUrl && urlInputText.isNotEmpty()) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Clear",
                            tint = Color.White.copy(alpha = 0.6f),
                            modifier = Modifier
                                .size(14.dp)
                                .clickable { urlInputText = "" }
                        )
                    }
                }
            }

            // ----------------------------------------------------
            // 🧩 GREEN VPN EXTENSION BUTTON (Installed in Chrome!)
            // ----------------------------------------------------
            Box(contentAlignment = Alignment.TopEnd) {
                IconButton(
                    onClick = { onToggleExtensionPopup() },
                    modifier = Modifier
                        .size(34.dp)
                        .testTag("chrome_extension_button")
                        .clip(RoundedCornerShape(8.dp))
                        .background(if (vpnState.isConnected) NeonGreen.copy(alpha = 0.15f) else Color.Transparent)
                ) {
                    Icon(
                        imageVector = Icons.Default.Extension,
                        contentDescription = "Chrome Extensions - Green VPN",
                        tint = if (vpnState.isConnected) NeonGreen else Color.White.copy(alpha = 0.9f),
                        modifier = Modifier.size(18.dp)
                    )
                }

                // Green VPN Badge on Extension Icon!
                Box(
                    modifier = Modifier
                        .padding(top = 4.dp, end = 4.dp)
                        .size(8.dp)
                        .clip(CircleShape)
                        .background(if (vpnState.isConnected) NeonGreen else if (vpnState.isConnecting) Color(0xFFFFB300) else Color(0xFF757575))
                        .border(1.dp, ChromeDarkBg, CircleShape)
                )
            }

            // Chrome 3-Dots Menu Button
            Box {
                IconButton(
                    onClick = { showChromeMenu = !showChromeMenu },
                    modifier = Modifier.size(30.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.MoreVert,
                        contentDescription = "Chrome Menu",
                        tint = Color.White.copy(alpha = 0.85f),
                        modifier = Modifier.size(18.dp)
                    )
                }

                DropdownMenu(
                    expanded = showChromeMenu,
                    onDismissRequest = { showChromeMenu = false },
                    modifier = Modifier.background(DarkSurfaceElevated)
                ) {
                    DropdownMenuItem(
                        text = { Text("New Tab", color = Color.White, fontSize = 12.sp) },
                        onClick = {
                            showChromeMenu = false
                            onNewTab("https://www.google.com")
                        }
                    )
                    DropdownMenuItem(
                        text = { Text("New Incognito Tab", color = Color.White, fontSize = 12.sp) },
                        onClick = {
                            showChromeMenu = false
                            onNewTab("https://duckduckgo.com")
                        }
                    )
                    DropdownMenuItem(
                        text = {
                            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                Icon(imageVector = Icons.Default.Extension, contentDescription = null, tint = NeonGreen, modifier = Modifier.size(14.dp))
                                Text("Green VPN Extension", color = NeonGreen, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                            }
                        },
                        onClick = {
                            showChromeMenu = false
                            onToggleExtensionPopup()
                        }
                    )
                    DropdownMenuItem(
                        text = { Text("Green VPN IP Leak Test", color = Color.White, fontSize = 12.sp) },
                        onClick = {
                            showChromeMenu = false
                            onNavigateUrl("https://ipinfo.io")
                        }
                    )
                    HorizontalDivider(color = DarkBorder)
                    DropdownMenuItem(
                        text = { Text("Toggle Bookmarks Bar", color = Color.White.copy(alpha = 0.8f), fontSize = 12.sp) },
                        onClick = {
                            showBookmarksBar = !showBookmarksBar
                            showChromeMenu = false
                        }
                    )
                }
            }
        }

        // Web Page Loading Progress Bar
        if (activeTab.isLoading) {
            LinearProgressIndicator(
                progress = { activeTab.progress },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(2.dp),
                color = NeonGreen,
                trackColor = Color.Transparent
            )
        }

        // ----------------------------------------------------
        // Green VPN Tunnel Status Pill Banner
        // ----------------------------------------------------
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(if (vpnState.isConnected) Color(0xFF0F261B) else Color(0xFF221A1A))
                .clickable { onToggleExtensionPopup() }
                .padding(horizontal = 10.dp, vertical = 3.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Icon(
                    imageVector = if (vpnState.isConnected) Icons.Default.Security else Icons.Default.VpnKey,
                    contentDescription = "VPN Status",
                    tint = if (vpnState.isConnected) NeonGreen else Color(0xFFFF7043),
                    modifier = Modifier.size(12.dp)
                )
                Text(
                    text = if (vpnState.isConnected) {
                        "Green VPN Active: ${vpnState.selectedServer.flagEmoji} ${vpnState.selectedServer.city} (${vpnState.selectedServer.ipAddress})"
                    } else {
                        "⚠️ Green VPN Extension Disconnected • Real IP exposed"
                    },
                    color = if (vpnState.isConnected) NeonGreen else Color(0xFFFFAB91),
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Medium
                )
            }
            Text(
                text = if (vpnState.isConnected) "AES-256 ✓" else "Click to Protect ↗",
                color = if (vpnState.isConnected) NeonGreen else Color(0xFFFFB74D),
                fontSize = 9.sp,
                fontWeight = FontWeight.Bold
            )
        }

        // ----------------------------------------------------
        // Chrome Bookmarks Quick Bar
        // ----------------------------------------------------
        if (showBookmarksBar) {
            LazyRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFF181A19))
                    .padding(horizontal = 6.dp, vertical = 3.dp),
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                items(DEFAULT_BOOKMARKS) { bookmark ->
                    Row(
                        modifier = Modifier
                            .clip(RoundedCornerShape(12.dp))
                            .background(DarkSurfaceElevated)
                            .border(0.8.dp, DarkBorder, RoundedCornerShape(12.dp))
                            .clickable {
                                urlInputText = bookmark.url
                                onNavigateUrl(bookmark.url)
                            }
                            .padding(horizontal = 8.dp, vertical = 3.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Text(text = bookmark.iconEmoji, fontSize = 11.sp)
                        Text(
                            text = bookmark.title,
                            color = Color.White.copy(alpha = 0.9f),
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }
        }

        // ----------------------------------------------------
        // Main Web Content (Android WebView + Extensions Overlay)
        // ----------------------------------------------------
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        ) {
            // Real WebView embedding
            AndroidView(
                modifier = Modifier
                    .fillMaxSize()
                    .testTag("chrome_webview"),
                factory = { context ->
                    WebView(context).apply {
                        layoutParams = ViewGroup.LayoutParams(
                            ViewGroup.LayoutParams.MATCH_PARENT,
                            ViewGroup.LayoutParams.MATCH_PARENT
                        )
                        settings.apply {
                            javaScriptEnabled = true
                            domStorageEnabled = true
                            databaseEnabled = true
                            useWideViewPort = true
                            loadWithOverviewMode = true
                            setSupportZoom(true)
                            builtInZoomControls = true
                            displayZoomControls = false
                            cacheMode = WebSettings.LOAD_DEFAULT
                            userAgentString = "Mozilla/5.0 (Linux; Android 14; VirtualPixel) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/126.0.0.0 Mobile Safari/537.36 GreenVPN/4.2"
                        }

                        webViewClient = object : WebViewClient() {
                            override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                                super.onPageStarted(view, url, favicon)
                                url?.let {
                                    urlInputText = it
                                    onUpdateTabProgress(activeTab.id, true, 0.3f)
                                }
                            }

                            override fun onPageFinished(view: WebView?, url: String?) {
                                super.onPageFinished(view, url)
                                val currentTitle = view?.title ?: activeTab.title
                                val currentUrl = url ?: activeTab.url
                                onUpdateTabDetails(
                                    activeTab.id,
                                    currentTitle,
                                    currentUrl,
                                    view?.canGoBack() == true,
                                    view?.canGoForward() == true
                                )
                                onUpdateTabProgress(activeTab.id, false, 1.0f)
                            }
                        }

                        webChromeClient = object : WebChromeClient() {
                            override fun onProgressChanged(view: WebView?, newProgress: Int) {
                                super.onProgressChanged(view, newProgress)
                                onUpdateTabProgress(
                                    activeTab.id,
                                    newProgress < 100,
                                    newProgress / 100f
                                )
                            }

                            override fun onReceivedTitle(view: WebView?, title: String?) {
                                super.onReceivedTitle(view, title)
                                title?.let {
                                    onUpdateTabDetails(
                                        activeTab.id,
                                        it,
                                        view?.url ?: activeTab.url,
                                        view?.canGoBack() == true,
                                        view?.canGoForward() == true
                                    )
                                }
                            }
                        }

                        loadUrl(activeTab.url)
                        webViewInstance = this
                    }
                },
                update = { webView ->
                    webViewInstance = webView
                    if (webView.url != activeTab.url && !activeTab.isLoading) {
                        webView.loadUrl(activeTab.url)
                    }
                }
            )

            // ----------------------------------------------------
            // GREEN VPN CHROME EXTENSION POPUP OVERLAY
            // ----------------------------------------------------
            if (isExtensionPopupVisible) {
                Box(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(top = 4.dp, end = 8.dp)
                ) {
                    GreenVpnExtensionPopup(
                        vpnState = vpnState,
                        onToggleVpn = onToggleVpn,
                        onSelectServer = onSelectServer,
                        onToggleAdBlocker = onToggleAdBlocker,
                        onToggleWebRtc = onToggleWebRtc,
                        onToggleKillSwitch = onToggleKillSwitch,
                        onClose = onCloseExtensionPopup,
                        onOpenFullApp = onOpenFullVpnApp
                    )
                }
            }
        }
    }
}
