package com.example.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val GreenVpnColorScheme = darkColorScheme(
    primary = NeonGreen,
    onPrimary = ObsidianBlack,
    primaryContainer = EmeraldDeep,
    onPrimaryContainer = NeonGreen,
    secondary = Color(0xFF69F0AE),
    onSecondary = ObsidianBlack,
    tertiary = Color(0xFF64FFDA),
    background = ObsidianBlack,
    onBackground = Color(0xFFE0E6E3),
    surface = DarkSurface,
    onSurface = Color(0xFFE0E6E3),
    surfaceVariant = DarkSurfaceElevated,
    onSurfaceVariant = Color(0xFFA5B4AC),
    outline = DarkBorder,
    error = Color(0xFFFF5252),
    onError = Color.White
)

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = GreenVpnColorScheme,
        typography = Typography,
        content = content
    )
}
