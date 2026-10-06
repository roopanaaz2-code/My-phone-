package com.example.ui.apps

import androidx.compose.animation.AnimatedVisibility
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
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.Backspace
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.CallEnd
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Extension
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.OpenInBrowser
import androidx.compose.material.icons.filled.PhoneAndroid
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material.icons.filled.VpnKey
import androidx.compose.material.icons.filled.Wallpaper
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.VirtualPhoneState
import com.example.model.VpnState
import com.example.ui.theme.DarkBorder
import com.example.ui.theme.DarkSurface
import com.example.ui.theme.DarkSurfaceElevated
import com.example.ui.theme.NeonGreen
import com.example.ui.theme.ObsidianBlack
import java.util.Locale

// ==========================================
// 1. DIALER APP
// ==========================================
@Composable
fun DialerAppView(
    dialerNumber: String,
    callActive: Boolean,
    callDuration: Int,
    onAppendDigit: (String) -> Unit,
    onBackspace: () -> Unit,
    onStartCall: () -> Unit,
    onEndCall: () -> Unit,
    modifier: Modifier = Modifier
) {
    if (callActive) {
        // Active Call Screen
        Column(
            modifier = modifier
                .fillMaxSize()
                .background(ObsidianBlack)
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(top = 40.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(80.dp)
                        .clip(CircleShape)
                        .background(Color(0xFF1E2822)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(imageVector = Icons.Default.Call, contentDescription = null, tint = NeonGreen, modifier = Modifier.size(40.dp))
                }
                Spacer(modifier = Modifier.height(16.dp))
                Text(text = dialerNumber, color = Color.White, fontSize = 22.sp, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(4.dp))
                val mins = callDuration / 60
                val secs = callDuration % 60
                Text(
                    text = String.format(Locale.US, "Connected • %02d:%02d", mins, secs),
                    color = NeonGreen,
                    fontSize = 14.sp
                )
                Text(text = "Encrypted Voice over Green VPN", color = Color.White.copy(alpha = 0.5f), fontSize = 11.sp)
            }

            // Call Action Controls
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                IconButton(onClick = {}, modifier = Modifier.size(54.dp).clip(CircleShape).background(DarkSurfaceElevated)) {
                    Icon(imageVector = Icons.Default.Mic, contentDescription = "Mute", tint = Color.White)
                }
                IconButton(onClick = {}, modifier = Modifier.size(54.dp).clip(CircleShape).background(DarkSurfaceElevated)) {
                    Icon(imageVector = Icons.Default.VolumeUp, contentDescription = "Speaker", tint = Color.White)
                }
                IconButton(
                    onClick = onEndCall,
                    modifier = Modifier
                        .size(54.dp)
                        .clip(CircleShape)
                        .background(Color(0xFFFF3B30))
                        .testTag("end_call_button")
                ) {
                    Icon(imageVector = Icons.Default.CallEnd, contentDescription = "Hang Up", tint = Color.White)
                }
            }
        }
    } else {
        // Keypad View
        Column(
            modifier = modifier
                .fillMaxSize()
                .background(ObsidianBlack)
                .padding(horizontal = 24.dp, vertical = 12.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // Number Display
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = if (dialerNumber.isEmpty()) "Enter number or *#06#" else dialerNumber,
                    color = if (dialerNumber.isEmpty()) Color.White.copy(alpha = 0.4f) else Color.White,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold
                )
                if (dialerNumber == "*#06#") {
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(text = "IMEI: 356984102948194", color = NeonGreen, fontSize = 12.sp)
                }
            }

            // Keypad Grid
            Column(
                verticalArrangement = Arrangement.spacedBy(10.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                val keys = listOf(
                    listOf("1", "2", "3"),
                    listOf("4", "5", "6"),
                    listOf("7", "8", "9"),
                    listOf("*", "0", "#")
                )
                keys.forEach { row ->
                    Row(horizontalArrangement = Arrangement.spacedBy(18.dp)) {
                        row.forEach { digit ->
                            Box(
                                modifier = Modifier
                                    .size(62.dp)
                                    .clip(CircleShape)
                                    .background(DarkSurfaceElevated)
                                    .clickable { onAppendDigit(digit) },
                                contentAlignment = Alignment.Center
                            ) {
                                Text(text = digit, color = Color.White, fontSize = 22.sp, fontWeight = FontWeight.SemiBold)
                            }
                        }
                    }
                }
            }

            // Bottom Call & Backspace Row
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 12.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Spacer(modifier = Modifier.width(50.dp))
                // Green Call Button
                Box(
                    modifier = Modifier
                        .size(64.dp)
                        .clip(CircleShape)
                        .background(NeonGreen)
                        .clickable { onStartCall() }
                        .testTag("dialer_call_button"),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(imageVector = Icons.Default.Call, contentDescription = "Call", tint = ObsidianBlack, modifier = Modifier.size(28.dp))
                }
                Spacer(modifier = Modifier.width(16.dp))
                // Backspace
                IconButton(onClick = onBackspace, modifier = Modifier.size(50.dp)) {
                    Icon(imageVector = Icons.Default.Backspace, contentDescription = "Backspace", tint = Color.White.copy(alpha = 0.7f))
                }
            }
        }
    }
}

// ==========================================
// 2. MESSAGES APP
// ==========================================
data class VirtualMessage(val id: String, val sender: String, val text: String, val time: String, val isFromMe: Boolean)

@Composable
fun MessagesAppView(modifier: Modifier = Modifier) {
    val messages = remember {
        mutableStateListOf(
            VirtualMessage("1", "Green VPN Security", "Welcome to Green VPN! Your Chrome browser extension is active with AES-256 quantum-resistant encryption.", "10:14 AM", false),
            VirtualMessage("2", "System Guard", "WebRTC leak shield & Adblocker running smoothly. Zero IP leaks detected in active tabs.", "10:18 AM", false)
        )
    }
    var inputText by remember { mutableStateOf("") }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(ObsidianBlack)
    ) {
        // Messages Header
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(DarkSurface)
                .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Box(modifier = Modifier.size(36.dp).clip(CircleShape).background(NeonGreen), contentAlignment = Alignment.Center) {
                Text(text = "🛡️", fontSize = 18.sp)
            }
            Column {
                Text(text = "Green VPN Security Alert", color = Color.White, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                Text(text = "End-to-End Encrypted Tunnel", color = NeonGreen, fontSize = 10.sp)
            }
        }

        // Messages List
        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(messages) { msg ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = if (msg.isFromMe) Arrangement.End else Arrangement.Start
                ) {
                    Card(
                        colors = CardDefaults.cardColors(
                            containerColor = if (msg.isFromMe) NeonGreen else DarkSurfaceElevated
                        ),
                        shape = RoundedCornerShape(14.dp),
                        modifier = Modifier.fillMaxWidth(0.82f)
                    ) {
                        Column(modifier = Modifier.padding(10.dp)) {
                            if (!msg.isFromMe) {
                                Text(text = msg.sender, color = NeonGreen, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                            }
                            Text(
                                text = msg.text,
                                color = if (msg.isFromMe) ObsidianBlack else Color.White,
                                fontSize = 12.sp
                            )
                            Text(
                                text = msg.time,
                                color = if (msg.isFromMe) ObsidianBlack.copy(alpha = 0.6f) else Color.White.copy(alpha = 0.5f),
                                fontSize = 9.sp,
                                modifier = Modifier.align(Alignment.End)
                            )
                        }
                    }
                }
            }
        }

        // Send Input Bar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(DarkSurface)
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            OutlinedTextField(
                value = inputText,
                onValueChange = { inputText = it },
                placeholder = { Text("Encrypted message...", color = Color.Gray, fontSize = 12.sp) },
                modifier = Modifier.weight(1f),
                textStyle = androidx.compose.ui.text.TextStyle(color = Color.White, fontSize = 12.sp)
            )
            IconButton(
                onClick = {
                    if (inputText.isNotEmpty()) {
                        messages.add(VirtualMessage(System.currentTimeMillis().toString(), "Me", inputText, "Now", true))
                        inputText = ""
                    }
                },
                modifier = Modifier.clip(CircleShape).background(NeonGreen)
            ) {
                Icon(imageVector = Icons.AutoMirrored.Filled.Send, contentDescription = "Send", tint = ObsidianBlack, modifier = Modifier.size(16.dp))
            }
        }
    }
}

// ==========================================
// 3. SETTINGS APP
// ==========================================
@Composable
fun SettingsAppView(
    phoneState: VirtualPhoneState,
    vpnState: VpnState,
    onToggleChassisMode: () -> Unit,
    onCycleWallpaper: () -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(ObsidianBlack)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item {
            Text(text = "Settings", color = Color.White, fontSize = 20.sp, fontWeight = FontWeight.Bold)
            Text(text = "Virtual Phone OS 15.0 Configuration", color = Color.White.copy(alpha = 0.5f), fontSize = 11.sp)
        }

        // Hardware & Display Settings
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = DarkSurfaceElevated),
                shape = RoundedCornerShape(14.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, DarkBorder)
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Text(text = "DISPLAY & CHASSIS", color = Color.White.copy(alpha = 0.5f), fontSize = 10.sp, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(10.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(text = "Phone Chassis Frame", color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                            Text(text = if (phoneState.isChassisMode) "Titanium Bezel & Camera cutout ON" else "Fullscreen borderless OS mode", color = Color.White.copy(alpha = 0.5f), fontSize = 10.sp)
                        }
                        Switch(
                            checked = phoneState.isChassisMode,
                            onCheckedChange = { onToggleChassisMode() },
                            colors = SwitchDefaults.colors(checkedThumbColor = NeonGreen, checkedTrackColor = Color(0xFF0F3D26))
                        )
                    }

                    HorizontalDivider(color = DarkBorder, modifier = Modifier.padding(vertical = 10.dp))

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onCycleWallpaper() },
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            Text(text = "Wallpaper Theme", color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                            Text(text = "Tap to switch background pattern", color = Color.White.copy(alpha = 0.5f), fontSize = 10.sp)
                        }
                        Icon(imageVector = Icons.Default.Wallpaper, contentDescription = null, tint = NeonGreen)
                    }
                }
            }
        }

        // Green VPN & Network Status
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = DarkSurfaceElevated),
                shape = RoundedCornerShape(14.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, DarkBorder)
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Text(text = "NETWORK & GREEN VPN", color = Color.White.copy(alpha = 0.5f), fontSize = 10.sp, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(8.dp))

                    SettingInfoRow("Carrier", phoneState.carrierName)
                    SettingInfoRow("VPN Status", if (vpnState.isConnected) "Connected (${vpnState.selectedServer.city})" else "Disconnected")
                    SettingInfoRow("Virtual IP", vpnState.displayIp)
                    SettingInfoRow("Protocol", vpnState.selectedProtocol)
                    SettingInfoRow("Chrome Extension", "Installed & Active (v4.2.0)")
                }
            }
        }

        // Device About Info
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = DarkSurfaceElevated),
                shape = RoundedCornerShape(14.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, DarkBorder)
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Text(text = "ABOUT VIRTUAL PHONE", color = Color.White.copy(alpha = 0.5f), fontSize = 10.sp, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(8.dp))

                    SettingInfoRow("Model", "Virtual Pixel 9 Pro (Titanium)")
                    SettingInfoRow("OS Version", "VirtualOS 15.0 (Build 2026.10)")
                    SettingInfoRow("RAM", "16 GB LPDDR5X")
                    SettingInfoRow("Storage", "512 GB (Encrypted)")
                    SettingInfoRow("Virtual IMEI", "356984102948194")
                }
            }
        }
    }
}

@Composable
private fun SettingInfoRow(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = label, color = Color.White.copy(alpha = 0.6f), fontSize = 11.sp)
        Text(text = value, color = Color.White, fontSize = 11.sp, fontWeight = FontWeight.SemiBold)
    }
}

// ==========================================
// 4. EXTENSION STORE / APP STORE
// ==========================================
@Composable
fun AppStoreView(
    onOpenChrome: () -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(ObsidianBlack)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item {
            Text(text = "Extension & App Store", color = Color.White, fontSize = 20.sp, fontWeight = FontWeight.Bold)
            Text(text = "Chrome Extensions & System Tools", color = Color.White.copy(alpha = 0.5f), fontSize = 11.sp)
        }

        // Green VPN Extension Feature Card
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = Color(0xFF0F261B)),
                shape = RoundedCornerShape(16.dp),
                border = androidx.compose.foundation.BorderStroke(1.5.dp, NeonGreen)
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(horizontalArrangement = Arrangement.spacedBy(10.dp), verticalAlignment = Alignment.CenterVertically) {
                            Box(modifier = Modifier.size(42.dp).clip(RoundedCornerShape(10.dp)).background(NeonGreen), contentAlignment = Alignment.Center) {
                                Icon(imageVector = Icons.Default.Security, contentDescription = null, tint = ObsidianBlack, modifier = Modifier.size(24.dp))
                            }
                            Column {
                                Text(text = "Green VPN Chrome Extension", color = Color.White, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                                Text(text = "Installed in Chrome • Version 4.2.0", color = NeonGreen, fontSize = 10.sp)
                            }
                        }
                        Box(modifier = Modifier.clip(RoundedCornerShape(6.dp)).background(NeonGreen.copy(alpha = 0.2f)).padding(horizontal = 6.dp, vertical = 2.dp)) {
                            Text(text = "ACTIVE", color = NeonGreen, fontSize = 9.sp, fontWeight = FontWeight.Bold)
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = "Provides real-time DNS over HTTPS, WebRTC leak shield, military-grade WireGuard encryption, and ad-blocking directly in your Chrome browser tabs.",
                        color = Color.White.copy(alpha = 0.75f),
                        fontSize = 11.sp
                    )

                    Spacer(modifier = Modifier.height(12.dp))
                    Button(
                        onClick = onOpenChrome,
                        colors = ButtonDefaults.buttonColors(containerColor = NeonGreen),
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Icon(imageVector = Icons.Default.OpenInBrowser, contentDescription = null, tint = ObsidianBlack, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(text = "Launch in Chrome Browser", color = ObsidianBlack, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }

        item {
            Text(text = "EXTENSION PERMISSIONS IN CHROME", color = Color.White.copy(alpha = 0.5f), fontSize = 10.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(6.dp))
            PermissionItem("Proxy API", "Routes Chrome network requests through Green VPN gateways")
            PermissionItem("webRequest & webRequestBlocking", "Filters tracking scripts and ads")
            PermissionItem("privacy.network.webRTCIPHandlingPolicy", "Blocks local WebRTC IP leaks")
            PermissionItem("storage & tabs", "Maintains session state and active tab protection")
        }
    }
}

@Composable
private fun PermissionItem(title: String, desc: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Icon(imageVector = Icons.Default.CheckCircle, contentDescription = null, tint = NeonGreen, modifier = Modifier.size(14.dp))
        Column {
            Text(text = title, color = Color.White, fontSize = 11.sp, fontWeight = FontWeight.SemiBold)
            Text(text = desc, color = Color.White.copy(alpha = 0.5f), fontSize = 9.sp)
        }
    }
}
