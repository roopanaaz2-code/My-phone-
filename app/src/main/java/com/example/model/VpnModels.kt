package com.example.model

/**
 * Representation of a Green VPN server node.
 */
data class VpnServer(
    val id: String,
    val country: String,
    val city: String,
    val flagEmoji: String,
    val ipAddress: String,
    val pingMs: Int,
    val loadPercent: Int,
    val isPremium: Boolean = false,
    val protocol: String = "WireGuard"
)

/**
 * State of the Green VPN client and extension.
 */
data class VpnState(
    val isConnected: Boolean = false,
    val isConnecting: Boolean = false,
    val selectedServer: VpnServer = DEFAULT_SERVERS[0],
    val currentVirtualIp: String = "192.168.1.105", // Default local IP before VPN
    val realIp: String = "192.168.1.105",
    val uploadSpeedKbps: Double = 0.0,
    val downloadSpeedKbps: Double = 0.0,
    val totalBytesEncrypted: Long = 0L,
    val connectedDurationSeconds: Long = 0L,
    val killSwitchEnabled: Boolean = true,
    val adBlockerEnabled: Boolean = true,
    val webRtcProtectionEnabled: Boolean = true,
    val dnsCryptEnabled: Boolean = true,
    val blockedTrackersCount: Int = 1420,
    val selectedProtocol: String = "WireGuard (Quantum-Resistant)"
) {
    val displayIp: String
        get() = if (isConnected) selectedServer.ipAddress else realIp
}

val DEFAULT_SERVERS = listOf(
    VpnServer(
        id = "nl-ams-1",
        country = "Netherlands",
        city = "Amsterdam",
        flagEmoji = "🇳🇱",
        ipAddress = "185.220.101.42",
        pingMs = 18,
        loadPercent = 38
    ),
    VpnServer(
        id = "ch-zur-1",
        country = "Switzerland",
        city = "Zurich",
        flagEmoji = "🇨🇭",
        ipAddress = "179.43.148.91",
        pingMs = 24,
        loadPercent = 29
    ),
    VpnServer(
        id = "de-fra-1",
        country = "Germany",
        city = "Frankfurt",
        flagEmoji = "🇩🇪",
        ipAddress = "194.36.191.12",
        pingMs = 21,
        loadPercent = 45
    ),
    VpnServer(
        id = "us-nyc-1",
        country = "United States",
        city = "New York",
        flagEmoji = "🇺🇸",
        ipAddress = "198.54.130.68",
        pingMs = 72,
        loadPercent = 64
    ),
    VpnServer(
        id = "gb-lon-1",
        country = "United Kingdom",
        city = "London",
        flagEmoji = "🇬🇧",
        ipAddress = "185.125.204.15",
        pingMs = 29,
        loadPercent = 52
    ),
    VpnServer(
        id = "jp-tyo-1",
        country = "Japan",
        city = "Tokyo",
        flagEmoji = "🇯🇵",
        ipAddress = "103.152.220.8",
        pingMs = 145,
        loadPercent = 41
    ),
    VpnServer(
        id = "sg-sin-1",
        country = "Singapore",
        city = "Singapore",
        flagEmoji = "🇸🇬",
        ipAddress = "139.99.120.34",
        pingMs = 168,
        loadPercent = 35
    ),
    VpnServer(
        id = "ca-tor-1",
        country = "Canada",
        city = "Toronto",
        flagEmoji = "🇨🇦",
        ipAddress = "192.99.245.10",
        pingMs = 85,
        loadPercent = 48
    )
)

/**
 * Representation of a tab in the Chrome browser.
 */
data class BrowserTab(
    val id: String,
    val title: String,
    val url: String,
    val isLoading: Boolean = false,
    val progress: Float = 1.0f,
    val canGoBack: Boolean = false,
    val canGoForward: Boolean = false,
    val isIncognito: Boolean = false
)

/**
 * Quick bookmark entry in Chrome.
 */
data class ChromeBookmark(
    val title: String,
    val url: String,
    val iconEmoji: String,
    val description: String
)

val DEFAULT_BOOKMARKS = listOf(
    ChromeBookmark("Google", "https://www.google.com", "🔍", "Search the web"),
    ChromeBookmark("Green VPN Shield Test", "https://ipinfo.io", "🛡️", "Check your virtual IP & encryption"),
    ChromeBookmark("Wikipedia", "https://www.wikipedia.org", "📚", "The Free Encyclopedia"),
    ChromeBookmark("DuckDuckGo", "https://duckduckgo.com", "🦆", "Privacy-first search engine"),
    ChromeBookmark("GitHub", "https://github.com", "🐙", "Where software is built"),
    ChromeBookmark("Hacker News", "https://news.ycombinator.com", "⚡", "Tech news & discussion")
)

/**
 * Apps installed inside the Virtual Phone.
 */
enum class PhoneApp(val appName: String, val packageName: String) {
    HOME("Home", "com.virtual.home"),
    CHROME("Chrome", "com.android.chrome"),
    GREEN_VPN("Green VPN", "com.greenvpn.shield"),
    DIALER("Phone", "com.android.dialer"),
    MESSAGES("Messages", "com.android.mms"),
    SETTINGS("Settings", "com.android.settings"),
    APP_STORE("Extension Store", "com.google.vstore")
}

/**
 * Virtual Phone hardware & status bar settings.
 */
data class VirtualPhoneState(
    val currentApp: PhoneApp = PhoneApp.HOME,
    val previousApp: PhoneApp = PhoneApp.HOME,
    val isChassisMode: Boolean = true, // Frame around phone vs borderless
    val batteryPercent: Int = 94,
    val isCharging: Boolean = false,
    val carrierName: String = "GreenNet 5G",
    val wifiConnected: Boolean = true,
    val wallpaperIndex: Int = 0,
    val showRecentApps: Boolean = false,
    val showNotificationShade: Boolean = false,
    val recentAppsList: List<PhoneApp> = listOf(PhoneApp.CHROME, PhoneApp.GREEN_VPN, PhoneApp.SETTINGS)
)
