package com.example.ui.vpn

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.OpenInBrowser
import androidx.compose.material.icons.filled.PowerSettingsNew
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material.icons.filled.Upload
import androidx.compose.material.icons.filled.VpnKey
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.DEFAULT_SERVERS
import com.example.model.VpnServer
import com.example.model.VpnState
import com.example.ui.theme.DarkBorder
import com.example.ui.theme.DarkSurface
import com.example.ui.theme.DarkSurfaceElevated
import com.example.ui.theme.EmeraldDeep
import com.example.ui.theme.NeonGreen
import com.example.ui.theme.NeonGreenGlow
import com.example.ui.theme.ObsidianBlack
import java.util.Locale

/**
 * Standalone Green VPN Application view inside the Virtual Phone.
 */
@Composable
fun GreenVpnAppView(
    vpnState: VpnState,
    onToggleVpn: () -> Unit,
    onSelectServer: (VpnServer) -> Unit,
    onToggleAdBlocker: () -> Unit,
    onToggleWebRtc: () -> Unit,
    onToggleKillSwitch: () -> Unit,
    onSelectProtocol: (String) -> Unit,
    onOpenChrome: () -> Unit,
    modifier: Modifier = Modifier
) {
    var showServerDialog by remember { mutableStateOf(false) }
    var showProtocolDialog by remember { mutableStateOf(false) }

    val infiniteTransition = rememberInfiniteTransition(label = "vpn_anim")
    val pulseSize by infiniteTransition.animateFloat(
        initialValue = 120f,
        targetValue = 138f,
        animationSpec = infiniteRepeatable(
            animation = tween(1400, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulseSize"
    )

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(ObsidianBlack)
            .padding(horizontal = 16.dp, vertical = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // App Header
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(32.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(NeonGreen),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Shield,
                            contentDescription = "Shield",
                            tint = ObsidianBlack,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                    Column {
                        Text(
                            text = "GREEN VPN",
                            color = Color.White,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Black,
                            letterSpacing = 1.sp
                        )
                        Text(
                            text = "Virtual Phone Privacy Suite",
                            color = NeonGreen,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }

                // Extension Link badge
                Button(
                    onClick = onOpenChrome,
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1B3D2B)),
                    shape = RoundedCornerShape(12.dp),
                    contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 10.dp, vertical = 4.dp),
                    modifier = Modifier.testTag("open_chrome_from_vpn")
                ) {
                    Icon(imageVector = Icons.Default.OpenInBrowser, contentDescription = null, tint = NeonGreen, modifier = Modifier.size(14.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(text = "Open Chrome", color = NeonGreen, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                }
            }
        }

        // Connection Power Core
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 12.dp),
                contentAlignment = Alignment.Center
            ) {
                if (vpnState.isConnected) {
                    // Pulsing Outer Rings
                    Box(
                        modifier = Modifier
                            .size(pulseSize.dp)
                            .clip(CircleShape)
                            .background(NeonGreenGlow)
                    )
                    Box(
                        modifier = Modifier
                            .size((pulseSize - 20).dp)
                            .clip(CircleShape)
                            .background(NeonGreen.copy(alpha = 0.25f))
                    )
                }

                // Main circular power button
                Box(
                    modifier = Modifier
                        .size(105.dp)
                        .clip(CircleShape)
                        .background(
                            Brush.radialGradient(
                                colors = if (vpnState.isConnected) {
                                    listOf(NeonGreen, Color(0xFF009640))
                                } else if (vpnState.isConnecting) {
                                    listOf(Color(0xFFFFB300), Color(0xFFF57C00))
                                } else {
                                    listOf(Color(0xFF233129), Color(0xFF141F1A))
                                }
                            )
                        )
                        .border(
                            3.dp,
                            if (vpnState.isConnected) NeonGreen else DarkBorder,
                            CircleShape
                        )
                        .clickable { onToggleVpn() }
                        .testTag("vpn_main_power_button"),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(
                            imageVector = Icons.Default.PowerSettingsNew,
                            contentDescription = "Toggle VPN",
                            tint = if (vpnState.isConnected) ObsidianBlack else Color.White,
                            modifier = Modifier.size(46.dp)
                        )
                    }
                }
            }

            Text(
                text = when {
                    vpnState.isConnected -> "PROTECTED & ENCRYPTED"
                    vpnState.isConnecting -> "ESTABLISHING SECURE TUNNEL..."
                    else -> "UNPROTECTED - TAP TO CONNECT"
                },
                color = when {
                    vpnState.isConnected -> NeonGreen
                    vpnState.isConnecting -> Color(0xFFFFB300)
                    else -> Color.White.copy(alpha = 0.6f)
                },
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.sp
            )

            if (vpnState.isConnected) {
                val mins = vpnState.connectedDurationSeconds / 60
                val secs = vpnState.connectedDurationSeconds % 60
                Text(
                    text = String.format(Locale.US, "Connected: %02d:%02d", mins, secs),
                    color = Color.White.copy(alpha = 0.7f),
                    fontSize = 11.sp
                )
            }
        }

        // Selected Server Card
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { showServerDialog = !showServerDialog }
                    .testTag("vpn_server_selector_card"),
                colors = CardDefaults.cardColors(containerColor = DarkSurfaceElevated),
                shape = RoundedCornerShape(16.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, if (vpnState.isConnected) NeonGreen.copy(alpha = 0.4f) else DarkBorder)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Text(text = vpnState.selectedServer.flagEmoji, fontSize = 28.sp)
                        Column {
                            Text(
                                text = "${vpnState.selectedServer.city}, ${vpnState.selectedServer.country}",
                                color = Color.White,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "IP: ${vpnState.displayIp} • Ping: ${vpnState.selectedServer.pingMs} ms",
                                color = if (vpnState.isConnected) NeonGreen else Color.White.copy(alpha = 0.5f),
                                fontSize = 11.sp
                            )
                        }
                    }
                    Icon(
                        imageVector = Icons.Default.ArrowDropDown,
                        contentDescription = "Select Server",
                        tint = NeonGreen
                    )
                }
            }
        }

        // Server Dropdown List
        item {
            AnimatedVisibility(visible = showServerDialog) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(14.dp))
                        .background(Color(0xFF0F1512))
                        .border(1.dp, DarkBorder, RoundedCornerShape(14.dp))
                ) {
                    Text(
                        text = "SELECT SECURE GATEWAY",
                        color = Color.White.copy(alpha = 0.5f),
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(12.dp)
                    )
                    DEFAULT_SERVERS.forEach { server ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    onSelectServer(server)
                                    showServerDialog = false
                                }
                                .padding(horizontal = 14.dp, vertical = 10.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                Text(text = server.flagEmoji, fontSize = 20.sp)
                                Column {
                                    Text(
                                        text = "${server.city}, ${server.country}",
                                        color = if (server.id == vpnState.selectedServer.id) NeonGreen else Color.White,
                                        fontSize = 13.sp,
                                        fontWeight = if (server.id == vpnState.selectedServer.id) FontWeight.Bold else FontWeight.Normal
                                    )
                                    Text(
                                        text = "Load: ${server.loadPercent}% • ${server.protocol}",
                                        color = Color.White.copy(alpha = 0.45f),
                                        fontSize = 10.sp
                                    )
                                }
                            }
                            Text(
                                text = "${server.pingMs} ms",
                                color = if (server.pingMs < 40) NeonGreen else Color.White.copy(alpha = 0.6f),
                                fontSize = 12.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }
                }
            }
        }

        // Live Speed Metrics Card
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                // Download Speed Card
                Card(
                    modifier = Modifier.weight(1f),
                    colors = CardDefaults.cardColors(containerColor = DarkSurfaceElevated),
                    shape = RoundedCornerShape(14.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, DarkBorder)
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                            Icon(imageVector = Icons.Default.Download, contentDescription = null, tint = NeonGreen, modifier = Modifier.size(16.dp))
                            Text(text = "DOWNLOAD", color = Color.White.copy(alpha = 0.5f), fontSize = 10.sp, fontWeight = FontWeight.Bold)
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = String.format(Locale.US, "%.1f", vpnState.downloadSpeedKbps),
                            color = Color.White,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Black
                        )
                        Text(text = "Mb / sec", color = NeonGreen, fontSize = 10.sp)
                    }
                }

                // Upload Speed Card
                Card(
                    modifier = Modifier.weight(1f),
                    colors = CardDefaults.cardColors(containerColor = DarkSurfaceElevated),
                    shape = RoundedCornerShape(14.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, DarkBorder)
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                            Icon(imageVector = Icons.Default.Upload, contentDescription = null, tint = Color(0xFF00E5FF), modifier = Modifier.size(16.dp))
                            Text(text = "UPLOAD", color = Color.White.copy(alpha = 0.5f), fontSize = 10.sp, fontWeight = FontWeight.Bold)
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = String.format(Locale.US, "%.1f", vpnState.uploadSpeedKbps),
                            color = Color.White,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Black
                        )
                        Text(text = "Mb / sec", color = Color(0xFF00E5FF), fontSize = 10.sp)
                    }
                }
            }
        }

        // Security Shield Diagnostics & Toggles
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = DarkSurfaceElevated),
                shape = RoundedCornerShape(16.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, DarkBorder)
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Text(
                        text = "TUNNEL & CHROME EXTENSION SECURITY",
                        color = Color.White.copy(alpha = 0.5f),
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    VpnFeatureRow(
                        title = "Ad & Tracker Blocker",
                        desc = "${vpnState.blockedTrackersCount} malicious trackers prevented",
                        checked = vpnState.adBlockerEnabled,
                        onToggle = onToggleAdBlocker
                    )
                    HorizontalDivider(color = DarkBorder, modifier = Modifier.padding(vertical = 6.dp))

                    VpnFeatureRow(
                        title = "Kill Switch (System & Browser)",
                        desc = "Instantly halts unencrypted traffic",
                        checked = vpnState.killSwitchEnabled,
                        onToggle = onToggleKillSwitch
                    )
                    HorizontalDivider(color = DarkBorder, modifier = Modifier.padding(vertical = 6.dp))

                    VpnFeatureRow(
                        title = "WebRTC & IPv6 Leak Protection",
                        desc = "Ensures browser never reveals original IP",
                        checked = vpnState.webRtcProtectionEnabled,
                        onToggle = onToggleWebRtc
                    )
                }
            }
        }

        // Chrome Extension Integration Promo Card
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onOpenChrome() },
                colors = CardDefaults.cardColors(containerColor = Color(0xFF10261A)),
                shape = RoundedCornerShape(16.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, NeonGreen.copy(alpha = 0.5f))
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .clip(RoundedCornerShape(10.dp))
                                .background(NeonGreen),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(text = "🧩", fontSize = 18.sp)
                        }
                        Column {
                            Text(
                                text = "Chrome Extension Active",
                                color = Color.White,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "Installed in Chrome toolbar • Click to surf web",
                                color = NeonGreen,
                                fontSize = 10.sp
                            )
                        }
                    }
                    Icon(
                        imageVector = Icons.Default.CheckCircle,
                        contentDescription = "Active",
                        tint = NeonGreen,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        }
    }
}

@Composable
private fun VpnFeatureRow(
    title: String,
    desc: String,
    checked: Boolean,
    onToggle: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(text = title, color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
            Text(text = desc, color = Color.White.copy(alpha = 0.55f), fontSize = 10.sp)
        }
        Switch(
            checked = checked,
            onCheckedChange = { onToggle() },
            colors = SwitchDefaults.colors(
                checkedThumbColor = NeonGreen,
                checkedTrackColor = Color(0xFF0F3D26),
                uncheckedThumbColor = Color.Gray,
                uncheckedTrackColor = DarkBorder
            )
        )
    }
}
