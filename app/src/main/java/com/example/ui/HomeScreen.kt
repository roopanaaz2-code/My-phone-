package com.example.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material.icons.filled.Extension
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.model.PhoneApp
import com.example.model.VirtualPhoneState
import com.example.model.VpnState
import com.example.ui.theme.DarkBorder
import com.example.ui.theme.DarkSurfaceElevated
import com.example.ui.theme.NeonGreen
import com.example.ui.theme.ObsidianBlack
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * Interactive Home Screen launcher for the Virtual Phone.
 */
@Composable
fun HomeScreen(
    phoneState: VirtualPhoneState,
    vpnState: VpnState,
    currentTime: String,
    onOpenApp: (PhoneApp) -> Unit,
    onToggleVpn: () -> Unit,
    onOpenChromeSearch: () -> Unit,
    modifier: Modifier = Modifier
) {
    val dateString = SimpleDateFormat("EEEE, MMMM d", Locale.getDefault()).format(Date())

    Box(modifier = modifier.fillMaxSize()) {
        // Dynamic Wallpaper
        when (phoneState.wallpaperIndex) {
            0 -> {
                // Generated high-fidelity wallpaper asset
                Image(
                    painter = painterResource(id = R.drawable.vp_wallpaper),
                    contentDescription = "Wallpaper",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
                // Dark subtle overlay for contrast
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Black.copy(alpha = 0.35f))
                )
            }
            1 -> {
                // Cyber Emerald Gradient
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            Brush.verticalGradient(
                                colors = listOf(
                                    Color(0xFF071910),
                                    Color(0xFF0F3622),
                                    Color(0xFF040A07)
                                )
                            )
                        )
                )
            }
            2 -> {
                // Deep Midnight Void
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            Brush.radialGradient(
                                colors = listOf(
                                    Color(0xFF142436),
                                    Color(0xFF0A0F15),
                                    Color(0xFF040608)
                                )
                            )
                        )
                )
            }
            else -> {
                // Obsidian Matrix
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            Brush.verticalGradient(
                                colors = listOf(
                                    Color(0xFF15181C),
                                    Color(0xFF0B0D0F)
                                )
                            )
                        )
                )
            }
        }

        // Home Screen Content
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // Upper Section: Clock & Widgets
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(16.dp))

                // Big Digital Clock
                Text(
                    text = currentTime,
                    color = Color.White,
                    fontSize = 42.sp,
                    fontWeight = FontWeight.Light,
                    fontFamily = FontFamily.SansSerif
                )
                Text(
                    text = dateString,
                    color = Color.White.copy(alpha = 0.8f),
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Medium
                )

                Spacer(modifier = Modifier.height(18.dp))

                // Green VPN Home Screen Status Widget
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(18.dp))
                        .clickable { onOpenApp(PhoneApp.GREEN_VPN) }
                        .testTag("home_vpn_widget"),
                    colors = CardDefaults.cardColors(
                        containerColor = if (vpnState.isConnected) Color(0xCC0F291C) else Color(0xCC1A1F1D)
                    ),
                    border = androidx.compose.foundation.BorderStroke(
                        1.dp,
                        if (vpnState.isConnected) NeonGreen.copy(alpha = 0.7f) else DarkBorder
                    )
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 14.dp, vertical = 10.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(34.dp)
                                    .clip(CircleShape)
                                    .background(if (vpnState.isConnected) NeonGreen else Color(0xFF33423A)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Shield,
                                    contentDescription = null,
                                    tint = if (vpnState.isConnected) ObsidianBlack else Color.White,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                            Column {
                                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                                    Text(
                                        text = if (vpnState.isConnected) "Green VPN Active" else "Green VPN Disconnected",
                                        color = if (vpnState.isConnected) NeonGreen else Color.White,
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Text(text = "• Chrome Shield", color = Color.White.copy(alpha = 0.6f), fontSize = 10.sp)
                                }
                                Text(
                                    text = if (vpnState.isConnected) {
                                        "${vpnState.selectedServer.flagEmoji} ${vpnState.selectedServer.city} (${vpnState.selectedServer.ipAddress})"
                                    } else {
                                        "Tap to encrypt Chrome browser traffic"
                                    },
                                    color = Color.White.copy(alpha = 0.8f),
                                    fontSize = 10.sp
                                )
                            }
                        }

                        // Quick Toggle Button inside Widget
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(12.dp))
                                .background(if (vpnState.isConnected) NeonGreen.copy(alpha = 0.2f) else Color(0xFF26332C))
                                .clickable { onToggleVpn() }
                                .padding(horizontal = 10.dp, vertical = 6.dp)
                        ) {
                            Text(
                                text = if (vpnState.isConnected) "DISCONNECT" else "CONNECT",
                                color = if (vpnState.isConnected) NeonGreen else Color.White,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Google Search Bar Widget
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(44.dp)
                        .clip(RoundedCornerShape(22.dp))
                        .background(Color.Black.copy(alpha = 0.5f))
                        .border(1.dp, Color.White.copy(alpha = 0.15f), RoundedCornerShape(22.dp))
                        .clickable { onOpenChromeSearch() }
                        .padding(horizontal = 14.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Text(text = "G", color = Color(0xFF4285F4), fontSize = 18.sp, fontWeight = FontWeight.Black)
                    Text(
                        text = "Search or type URL...",
                        color = Color.White.copy(alpha = 0.7f),
                        fontSize = 12.sp,
                        modifier = Modifier.weight(1f)
                    )
                    // Chrome extension badge indicator on search bar
                    Row(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(if (vpnState.isConnected) NeonGreen.copy(alpha = 0.2f) else Color.Transparent)
                            .padding(horizontal = 4.dp, vertical = 2.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(2.dp)
                    ) {
                        Icon(imageVector = Icons.Default.Extension, contentDescription = null, tint = if (vpnState.isConnected) NeonGreen else Color.White.copy(alpha = 0.5f), modifier = Modifier.size(12.dp))
                        Text(text = "VPN", color = if (vpnState.isConnected) NeonGreen else Color.White.copy(alpha = 0.5f), fontSize = 9.sp, fontWeight = FontWeight.Bold)
                    }
                    Icon(imageVector = Icons.Default.Search, contentDescription = "Search", tint = Color.White.copy(alpha = 0.8f), modifier = Modifier.size(18.dp))
                }
            }

            // Middle Section: App Grid (Installed Apps on Virtual Phone)
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    // Chrome App (with Green VPN Extension badge!)
                    AppGridIcon(
                        title = "Chrome",
                        icon = Icons.Default.Language,
                        bgColor = Color(0xFF1E2833),
                        iconTint = Color(0xFF4285F4),
                        badgeText = "VPN 🧩",
                        isVpnActive = vpnState.isConnected,
                        onClick = { onOpenApp(PhoneApp.CHROME) },
                        testTag = "app_chrome"
                    )

                    // Green VPN Standalone App
                    AppGridIcon(
                        title = "Green VPN",
                        icon = Icons.Default.Security,
                        bgColor = Color(0xFF0F2B1C),
                        iconTint = NeonGreen,
                        badgeText = if (vpnState.isConnected) "ON" else null,
                        isVpnActive = vpnState.isConnected,
                        onClick = { onOpenApp(PhoneApp.GREEN_VPN) },
                        testTag = "app_green_vpn"
                    )

                    // Extension Store App
                    AppGridIcon(
                        title = "Extensions",
                        icon = Icons.Default.Extension,
                        bgColor = Color(0xFF2B2135),
                        iconTint = Color(0xFFCE93D8),
                        badgeText = "1",
                        isVpnActive = false,
                        onClick = { onOpenApp(PhoneApp.APP_STORE) },
                        testTag = "app_store"
                    )

                    // Settings App
                    AppGridIcon(
                        title = "Settings",
                        icon = Icons.Default.Settings,
                        bgColor = Color(0xFF25282A),
                        iconTint = Color(0xFFCFD8DC),
                        badgeText = null,
                        isVpnActive = false,
                        onClick = { onOpenApp(PhoneApp.SETTINGS) },
                        testTag = "app_settings"
                    )
                }
            }

            // Bottom Dock Section
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(26.dp))
                    .background(Color.Black.copy(alpha = 0.55f))
                    .border(1.dp, Color.White.copy(alpha = 0.12f), RoundedCornerShape(26.dp))
                    .padding(vertical = 10.dp, horizontal = 14.dp),
                contentAlignment = Alignment.Center
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceAround,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Phone / Dialer
                    DockIcon(
                        icon = Icons.Default.Call,
                        bgColor = Color(0xFF1B3B2B),
                        iconTint = Color(0xFF00E676),
                        onClick = { onOpenApp(PhoneApp.DIALER) }
                    )

                    // Chrome Browser (Primary app requested)
                    DockIcon(
                        icon = Icons.Default.Language,
                        bgColor = Color(0xFF182A3A),
                        iconTint = Color(0xFF4FC3F7),
                        hasVpnBadge = true,
                        isVpnConnected = vpnState.isConnected,
                        onClick = { onOpenApp(PhoneApp.CHROME) }
                    )

                    // Green VPN (Extension & System app)
                    DockIcon(
                        icon = Icons.Default.Shield,
                        bgColor = if (vpnState.isConnected) NeonGreen else Color(0xFF0E281A),
                        iconTint = if (vpnState.isConnected) ObsidianBlack else NeonGreen,
                        hasVpnBadge = false,
                        isVpnConnected = vpnState.isConnected,
                        onClick = { onOpenApp(PhoneApp.GREEN_VPN) }
                    )

                    // Messages
                    DockIcon(
                        icon = Icons.Default.Chat,
                        bgColor = Color(0xFF1D2838),
                        iconTint = Color(0xFF64B5F6),
                        onClick = { onOpenApp(PhoneApp.MESSAGES) }
                    )
                }
            }
        }
    }
}

@Composable
private fun AppGridIcon(
    title: String,
    icon: ImageVector,
    bgColor: Color,
    iconTint: Color,
    badgeText: String?,
    isVpnActive: Boolean,
    onClick: () -> Unit,
    testTag: String
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .clickable { onClick() }
            .testTag(testTag)
    ) {
        Box(contentAlignment = Alignment.TopEnd) {
            Box(
                modifier = Modifier
                    .size(54.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(bgColor)
                    .border(1.dp, if (isVpnActive && badgeText != null) NeonGreen.copy(alpha = 0.6f) else Color.White.copy(alpha = 0.1f), RoundedCornerShape(16.dp)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = title,
                    tint = iconTint,
                    modifier = Modifier.size(28.dp)
                )
            }

            // Top-right notification/extension badge
            if (badgeText != null) {
                Box(
                    modifier = Modifier
                        .padding(top = 2.dp, end = 2.dp)
                        .clip(RoundedCornerShape(6.dp))
                        .background(NeonGreen)
                        .padding(horizontal = 4.dp, vertical = 1.dp)
                ) {
                    Text(
                        text = badgeText,
                        color = ObsidianBlack,
                        fontSize = 8.sp,
                        fontWeight = FontWeight.ExtraBold
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(6.dp))
        Text(
            text = title,
            color = Color.White,
            fontSize = 11.sp,
            fontWeight = FontWeight.Medium
        )
    }
}

@Composable
private fun DockIcon(
    icon: ImageVector,
    bgColor: Color,
    iconTint: Color,
    hasVpnBadge: Boolean = false,
    isVpnConnected: Boolean = false,
    onClick: () -> Unit
) {
    Box(contentAlignment = Alignment.TopEnd) {
        Box(
            modifier = Modifier
                .size(48.dp)
                .clip(CircleShape)
                .background(bgColor)
                .clickable { onClick() },
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = iconTint,
                modifier = Modifier.size(24.dp)
            )
        }

        if (hasVpnBadge) {
            Box(
                modifier = Modifier
                    .size(14.dp)
                    .clip(CircleShape)
                    .background(if (isVpnConnected) NeonGreen else Color(0xFF757575))
                    .border(1.5.dp, Color.Black, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "V",
                    color = ObsidianBlack,
                    fontSize = 8.sp,
                    fontWeight = FontWeight.Black
                )
            }
        }
    }
}
