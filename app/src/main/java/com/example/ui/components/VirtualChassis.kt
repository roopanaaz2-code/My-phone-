package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
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
import androidx.compose.material.icons.filled.BatteryChargingFull
import androidx.compose.material.icons.filled.BatteryFull
import androidx.compose.material.icons.filled.Circle
import androidx.compose.material.icons.filled.CropSquare
import androidx.compose.material.icons.filled.NetworkCell
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.VpnKey
import androidx.compose.material.icons.filled.Wifi
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.VirtualPhoneState
import com.example.model.VpnState
import com.example.ui.theme.DarkBorder
import com.example.ui.theme.NeonGreen
import com.example.ui.theme.ObsidianBlack
import com.example.ui.theme.TitaniumBezel
import com.example.ui.theme.TitaniumBezelHighlight

/**
 * Virtual Phone status bar displaying time, VPN key, network, battery.
 */
@Composable
fun VirtualStatusBar(
    phoneState: VirtualPhoneState,
    vpnState: VpnState,
    currentTime: String,
    onVpnBadgeClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val infiniteTransition = rememberInfiniteTransition(label = "vpn_pulse")
    val vpnGlowAlpha by infiniteTransition.animateFloat(
        initialValue = 0.4f,
        targetValue = 1.0f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "vpnGlow"
    )

    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(28.dp)
            .background(Color.Black.copy(alpha = 0.5f))
            .padding(horizontal = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        // Left: Time & Carrier
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Text(
                text = currentTime,
                color = Color.White,
                fontSize = 12.sp,
                fontWeight = FontWeight.SemiBold,
                fontFamily = FontFamily.SansSerif
            )
            Text(
                text = "• ${phoneState.carrierName}",
                color = Color.White.copy(alpha = 0.6f),
                fontSize = 10.sp
            )
        }

        // Center Punch-hole space (accommodates top camera cutout)
        Spacer(modifier = Modifier.width(36.dp))

        // Right: VPN status badge, WiFi, Cell, Battery
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            // Glowing Green VPN Badge in status bar!
            AnimatedVisibility(
                visible = vpnState.isConnected,
                enter = fadeIn(),
                exit = fadeOut()
            ) {
                Row(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(NeonGreen.copy(alpha = 0.2f * vpnGlowAlpha))
                        .border(1.dp, NeonGreen.copy(alpha = 0.8f * vpnGlowAlpha), RoundedCornerShape(8.dp))
                        .clickable { onVpnBadgeClick() }
                        .padding(horizontal = 4.dp, vertical = 1.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(2.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.VpnKey,
                        contentDescription = "VPN Active",
                        tint = NeonGreen,
                        modifier = Modifier.size(10.dp)
                    )
                    Text(
                        text = "VPN",
                        color = NeonGreen,
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            // Wi-Fi
            Icon(
                imageVector = Icons.Default.Wifi,
                contentDescription = "Wi-Fi",
                tint = Color.White.copy(alpha = 0.85f),
                modifier = Modifier.size(13.dp)
            )

            // 5G Network
            Icon(
                imageVector = Icons.Default.NetworkCell,
                contentDescription = "Cellular 5G",
                tint = Color.White.copy(alpha = 0.85f),
                modifier = Modifier.size(13.dp)
            )

            // Battery
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(2.dp)
            ) {
                Text(
                    text = "${phoneState.batteryPercent}%",
                    color = Color.White.copy(alpha = 0.85f),
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Medium
                )
                Icon(
                    imageVector = if (phoneState.isCharging) Icons.Default.BatteryChargingFull else Icons.Default.BatteryFull,
                    contentDescription = "Battery",
                    tint = if (phoneState.batteryPercent > 20) NeonGreen else Color(0xFFFF5252),
                    modifier = Modifier.size(13.dp)
                )
            }
        }
    }
}

/**
 * Android 3-Button Navigation Bar at the bottom of the virtual phone.
 */
@Composable
fun VirtualNavigationBar(
    onBack: () -> Unit,
    onHome: () -> Unit,
    onRecents: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(44.dp)
            .background(Color.Black.copy(alpha = 0.75f))
            .padding(horizontal = 32.dp),
        horizontalArrangement = Arrangement.SpaceAround,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Back Button (Triangle / Arrow)
        IconButton(
            onClick = onBack,
            modifier = Modifier
                .testTag("nav_back_button")
                .size(40.dp)
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "Back",
                tint = Color.White.copy(alpha = 0.75f),
                modifier = Modifier.size(18.dp)
            )
        }

        // Home Button (Circle)
        IconButton(
            onClick = onHome,
            modifier = Modifier
                .testTag("nav_home_button")
                .size(40.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Circle,
                contentDescription = "Home",
                tint = Color.White.copy(alpha = 0.85f),
                modifier = Modifier.size(16.dp)
            )
        }

        // Recents Button (Square)
        IconButton(
            onClick = onRecents,
            modifier = Modifier
                .testTag("nav_recents_button")
                .size(40.dp)
        ) {
            Icon(
                imageVector = Icons.Default.CropSquare,
                contentDescription = "Recent Apps",
                tint = Color.White.copy(alpha = 0.75f),
                modifier = Modifier.size(16.dp)
            )
        }
    }
}

/**
 * Top Camera punch hole & speaker earpiece cutout.
 */
@Composable
fun PunchHoleCamera(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Speaker grill slit
        Box(
            modifier = Modifier
                .width(44.dp)
                .height(3.dp)
                .clip(RoundedCornerShape(2.dp))
                .background(Color(0xFF1E2022))
        )
        Spacer(modifier = Modifier.height(4.dp))
        // Selfie camera lens circle
        Box(
            modifier = Modifier
                .size(11.dp)
                .clip(CircleShape)
                .background(Color(0xFF0F1113))
                .border(1.dp, Color(0xFF1F2327), CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Box(
                modifier = Modifier
                    .size(4.dp)
                    .clip(CircleShape)
                    .background(Color(0xFF003828))
            )
        }
    }
}

/**
 * Chassis frame container rendering a realistic smartphone bezel or full screen.
 */
@Composable
fun PhoneFrameContainer(
    isChassisMode: Boolean,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    if (!isChassisMode) {
        // Full screen borderless OS mode
        Box(
            modifier = modifier
                .fillMaxSize()
                .background(ObsidianBlack)
        ) {
            content()
        }
    } else {
        // Beautiful Realistic Smartphone Hardware Chassis
        BoxWithConstraints(
            modifier = modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color(0xFF070B09),
                            Color(0xFF0F1813),
                            Color(0xFF070B09)
                        )
                    )
                )
                .padding(horizontal = 8.dp, vertical = 6.dp),
            contentAlignment = Alignment.Center
        ) {
            // Outer Titanium Phone Frame with bezel & side buttons
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .clip(RoundedCornerShape(32.dp))
                    .border(
                        width = 4.dp,
                        brush = Brush.linearGradient(
                            colors = listOf(
                                TitaniumBezelHighlight,
                                TitaniumBezel,
                                Color(0xFF15181A),
                                TitaniumBezelHighlight
                            )
                        ),
                        shape = RoundedCornerShape(32.dp)
                    )
                    .background(Color.Black),
                contentAlignment = Alignment.TopCenter
            ) {
                // Phone Screen content with inner rounded corners
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(4.dp)
                        .clip(RoundedCornerShape(28.dp))
                ) {
                    content()
                }

                // Punch-hole camera overlay at top
                PunchHoleCamera(
                    modifier = Modifier
                        .align(Alignment.TopCenter)
                        .padding(top = 4.dp)
                )
            }
        }
    }
}
