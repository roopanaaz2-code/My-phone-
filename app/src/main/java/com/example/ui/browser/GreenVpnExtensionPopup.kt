package com.example.ui.browser

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
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.PowerSettingsNew
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material.icons.filled.Upload
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.draw.shadow
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
import com.example.ui.theme.NeonGreen
import com.example.ui.theme.NeonGreenGlow
import com.example.ui.theme.ObsidianBlack
import java.util.Locale

/**
 * Authentic Chrome Browser Extension Popup for "Green VPN".
 * Styled exactly like a Chrome toolbar extension card.
 */
@Composable
fun GreenVpnExtensionPopup(
    vpnState: VpnState,
    onToggleVpn: () -> Unit,
    onSelectServer: (VpnServer) -> Unit,
    onToggleAdBlocker: () -> Unit,
    onToggleWebRtc: () -> Unit,
    onToggleKillSwitch: () -> Unit,
    onClose: () -> Unit,
    onOpenFullApp: () -> Unit,
    modifier: Modifier = Modifier
) {
    var showServerSelector by remember { mutableStateOf(false) }

    val infiniteTransition = rememberInfiniteTransition(label = "pulse")
    val pulseScale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 1.08f,
        animationSpec = infiniteRepeatable(
            animation = tween(1200, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulseScale"
    )

    Card(
        modifier = modifier
            .fillMaxWidth(0.92f)
            .shadow(16.dp, RoundedCornerShape(18.dp))
            .border(1.5.dp, if (vpnState.isConnected) NeonGreen.copy(alpha = 0.6f) else DarkBorder, RoundedCornerShape(18.dp)),
        colors = CardDefaults.cardColors(
            containerColor = DarkSurface
        ),
        shape = RoundedCornerShape(18.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Chrome Extension Header Bar
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(28.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(if (vpnState.isConnected) NeonGreen else Color(0xFF2C3E35)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Security,
                            contentDescription = "Green VPN Extension",
                            tint = if (vpnState.isConnected) ObsidianBlack else Color.White,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                            Text(
                                text = "Green VPN",
                                color = Color.White,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(4.dp))
                                    .background(NeonGreen.copy(alpha = 0.2f))
                                    .padding(horizontal = 4.dp, vertical = 1.dp)
                            ) {
                                Text(
                                    text = "EXTENSION",
                                    color = NeonGreen,
                                    fontSize = 8.sp,
                                    fontWeight = FontWeight.ExtraBold
                                )
                            }
                        }
                        Text(
                            text = "Chrome Privacy Shield v4.2",
                            color = Color.White.copy(alpha = 0.55f),
                            fontSize = 10.sp
                        )
                    }
                }

                IconButton(
                    onClick = onClose,
                    modifier = Modifier.size(26.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Close Extension",
                        tint = Color.White.copy(alpha = 0.7f),
                        modifier = Modifier.size(18.dp)
                    )
                }
            }

            HorizontalDivider(
                modifier = Modifier.padding(vertical = 10.dp),
                color = DarkBorder
            )

            // Main Power Button with neon glow
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.padding(vertical = 8.dp)
            ) {
                // Background glow when connected
                if (vpnState.isConnected) {
                    Box(
                        modifier = Modifier
                            .size((96 * pulseScale).dp)
                            .clip(CircleShape)
                            .background(NeonGreenGlow)
                    )
                }

                Box(
                    modifier = Modifier
                        .testTag("extension_power_button")
                        .size(80.dp)
                        .clip(CircleShape)
                        .background(
                            Brush.radialGradient(
                                colors = if (vpnState.isConnected) {
                                    listOf(NeonGreen, Color(0xFF00A850))
                                } else if (vpnState.isConnecting) {
                                    listOf(Color(0xFFFFB300), Color(0xFFF57C00))
                                } else {
                                    listOf(Color(0xFF2C3E35), Color(0xFF1B2822))
                                }
                            )
                        )
                        .border(
                            2.dp,
                            if (vpnState.isConnected) NeonGreen else DarkBorder,
                            CircleShape
                        )
                        .clickable { onToggleVpn() },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.PowerSettingsNew,
                        contentDescription = "Power VPN",
                        tint = if (vpnState.isConnected) ObsidianBlack else Color.White,
                        modifier = Modifier.size(38.dp)
                    )
                }
            }

            // Connection Status Text
            Text(
                text = when {
                    vpnState.isConnected -> "CONNECTED & ENCRYPTED"
                    vpnState.isConnecting -> "CONNECTING..."
                    else -> "DISCONNECTED"
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

            // Server Location Selector Card
            Spacer(modifier = Modifier.height(10.dp))
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { showServerSelector = !showServerSelector },
                colors = CardDefaults.cardColors(containerColor = DarkSurfaceElevated),
                shape = RoundedCornerShape(12.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, DarkBorder)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(text = vpnState.selectedServer.flagEmoji, fontSize = 20.sp)
                        Column {
                            Text(
                                text = "${vpnState.selectedServer.city}, ${vpnState.selectedServer.country}",
                                color = Color.White,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                            Text(
                                text = "Ping: ${vpnState.selectedServer.pingMs} ms • Load: ${vpnState.selectedServer.loadPercent}%",
                                color = NeonGreen,
                                fontSize = 10.sp
                            )
                        }
                    }
                    Icon(
                        imageVector = Icons.Default.ArrowDropDown,
                        contentDescription = "Change Server",
                        tint = Color.White.copy(alpha = 0.7f)
                    )
                }
            }

            // Expanded Server Selection List
            AnimatedVisibility(visible = showServerSelector) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(Color(0xFF0F1512))
                        .border(1.dp, DarkBorder, RoundedCornerShape(10.dp))
                ) {
                    DEFAULT_SERVERS.forEach { server ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    onSelectServer(server)
                                    showServerSelector = false
                                }
                                .padding(horizontal = 12.dp, vertical = 8.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Text(text = server.flagEmoji, fontSize = 18.sp)
                                Text(
                                    text = "${server.city}, ${server.country}",
                                    color = if (server.id == vpnState.selectedServer.id) NeonGreen else Color.White,
                                    fontSize = 11.sp,
                                    fontWeight = if (server.id == vpnState.selectedServer.id) FontWeight.Bold else FontWeight.Normal
                                )
                            }
                            Text(
                                text = "${server.pingMs}ms",
                                color = Color.White.copy(alpha = 0.5f),
                                fontSize = 10.sp
                            )
                        }
                    }
                }
            }

            // Live IP & Network Details
            Spacer(modifier = Modifier.height(10.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(10.dp))
                    .background(DarkSurfaceElevated)
                    .padding(8.dp),
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(text = "Virtual IP", color = Color.White.copy(alpha = 0.5f), fontSize = 9.sp)
                    Text(
                        text = vpnState.displayIp,
                        color = if (vpnState.isConnected) NeonGreen else Color.White,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(text = "Trackers Blocked", color = Color.White.copy(alpha = 0.5f), fontSize = 9.sp)
                    Text(
                        text = "${vpnState.blockedTrackersCount}",
                        color = Color(0xFF64FFDA),
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(text = "Protocol", color = Color.White.copy(alpha = 0.5f), fontSize = 9.sp)
                    Text(
                        text = "WireGuard",
                        color = Color.White,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            // Real-time Traffic Speed (if connected)
            if (vpnState.isConnected) {
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(8.dp))
                        .background(Color(0xFF0F1512))
                        .padding(horizontal = 12.dp, vertical = 6.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                        Icon(imageVector = Icons.Default.Download, contentDescription = "Download", tint = NeonGreen, modifier = Modifier.size(12.dp))
                        Text(
                            text = String.format(Locale.US, "%.1f Mb/s", vpnState.downloadSpeedKbps),
                            color = Color.White,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                        Icon(imageVector = Icons.Default.Upload, contentDescription = "Upload", tint = Color(0xFF00E5FF), modifier = Modifier.size(12.dp))
                        Text(
                            text = String.format(Locale.US, "%.1f Mb/s", vpnState.uploadSpeedKbps),
                            color = Color.White,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }

            // Protection Toggles in Chrome Extension
            Spacer(modifier = Modifier.height(8.dp))
            ExtensionToggleRow(
                title = "Ad & Tracker Shield",
                subtitle = "Blocks ads in Chrome tabs",
                isChecked = vpnState.adBlockerEnabled,
                onCheckedChange = { onToggleAdBlocker() }
            )
            ExtensionToggleRow(
                title = "WebRTC Leak Shield",
                subtitle = "Prevents browser IP leaks",
                isChecked = vpnState.webRtcProtectionEnabled,
                onCheckedChange = { onToggleWebRtc() }
            )
            ExtensionToggleRow(
                title = "Chrome Kill Switch",
                subtitle = "Pause web traffic if tunnel drops",
                isChecked = vpnState.killSwitchEnabled,
                onCheckedChange = { onToggleKillSwitch() }
            )

            // Open Full Standalone App link
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color(0xFF1E2B23))
                    .clickable { onOpenFullApp() }
                    .padding(vertical = 6.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Launch Standalone Green VPN App ↗",
                    color = NeonGreen,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    }
}

@Composable
private fun ExtensionToggleRow(
    title: String,
    subtitle: String,
    isChecked: Boolean,
    onCheckedChange: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 2.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(text = title, color = Color.White, fontSize = 11.sp, fontWeight = FontWeight.Medium)
            Text(text = subtitle, color = Color.White.copy(alpha = 0.5f), fontSize = 9.sp)
        }
        Switch(
            checked = isChecked,
            onCheckedChange = { onCheckedChange() },
            modifier = Modifier.size(36.dp),
            colors = SwitchDefaults.colors(
                checkedThumbColor = NeonGreen,
                checkedTrackColor = Color(0xFF0F3D26),
                uncheckedThumbColor = Color.Gray,
                uncheckedTrackColor = DarkBorder
            )
        )
    }
}
