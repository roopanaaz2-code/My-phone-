package com.example.ui.components

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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ClearAll
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Extension
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.PhoneApp
import com.example.ui.theme.DarkBorder
import com.example.ui.theme.DarkSurfaceElevated
import com.example.ui.theme.NeonGreen

@Composable
fun RecentAppsView(
    recentApps: List<PhoneApp>,
    onSelectApp: (PhoneApp) -> Unit,
    onCloseRecents: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.88f))
            .padding(vertical = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        // Header
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = "Recent Applications", color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.Bold)
            IconButton(onClick = onCloseRecents) {
                Icon(imageVector = Icons.Default.Close, contentDescription = "Close Recents", tint = Color.White)
            }
        }

        // Horizontal Carousel of App Cards
        LazyRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 32.dp)
        ) {
            items(recentApps) { app ->
                Card(
                    modifier = Modifier
                        .width(220.dp)
                        .height(340.dp)
                        .clickable { onSelectApp(app) }
                        .testTag("recent_app_${app.name}"),
                    colors = CardDefaults.cardColors(containerColor = DarkSurfaceElevated),
                    shape = RoundedCornerShape(20.dp),
                    border = androidx.compose.foundation.BorderStroke(1.5.dp, if (app == PhoneApp.CHROME || app == PhoneApp.GREEN_VPN) NeonGreen.copy(alpha = 0.6f) else DarkBorder)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(14.dp),
                        verticalArrangement = Arrangement.SpaceBetween
                    ) {
                        // App Card Header
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Icon(
                                imageVector = when (app) {
                                    PhoneApp.CHROME -> Icons.Default.Language
                                    PhoneApp.GREEN_VPN -> Icons.Default.Security
                                    PhoneApp.APP_STORE -> Icons.Default.Extension
                                    else -> Icons.Default.Settings
                                },
                                contentDescription = null,
                                tint = if (app == PhoneApp.GREEN_VPN || app == PhoneApp.CHROME) NeonGreen else Color.White,
                                modifier = Modifier.size(18.dp)
                            )
                            Text(text = app.appName, color = Color.White, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                        }

                        // App Preview Body
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .weight(1f)
                                .padding(vertical = 12.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .background(Color(0xFF0F1512))
                                .border(1.dp, DarkBorder, RoundedCornerShape(12.dp)),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text(
                                    text = when (app) {
                                        PhoneApp.CHROME -> "🌐 Chrome + Green VPN Active"
                                        PhoneApp.GREEN_VPN -> "🛡️ Green VPN Shield"
                                        PhoneApp.SETTINGS -> "⚙️ System Configuration"
                                        else -> "📱 ${app.appName}"
                                    },
                                    color = Color.White.copy(alpha = 0.7f),
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Medium
                                )
                            }
                        }

                        // Tap to switch
                        Text(
                            text = "Tap to resume app",
                            color = NeonGreen,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.SemiBold,
                            modifier = Modifier.align(Alignment.CenterHorizontally)
                        )
                    }
                }
            }
        }

        // Clear All Action
        Button(
            onClick = onCloseRecents,
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF26332C)),
            shape = RoundedCornerShape(20.dp)
        ) {
            Icon(imageVector = Icons.Default.ClearAll, contentDescription = null, tint = NeonGreen, modifier = Modifier.size(16.dp))
            Spacer(modifier = Modifier.width(6.dp))
            Text(text = "Close Recents", color = NeonGreen, fontSize = 12.sp)
        }
    }
}
