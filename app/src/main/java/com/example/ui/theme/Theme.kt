package com.example.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorScheme = darkColorScheme(
    primary = NewsRedPrimary,
    onPrimary = Color.White,
    primaryContainer = NewsRedContainer,
    onPrimaryContainer = Color.White,
    secondary = NewsCyanAccent,
    onSecondary = Color.Black,
    tertiary = NewsGoldAlert,
    onTertiary = Color.Black,
    background = NewsDarkBackground,
    onBackground = TextPrimary,
    surface = NewsDarkSurface,
    onSurface = TextPrimary,
    surfaceVariant = NewsDarkSurfaceElevated,
    onSurfaceVariant = TextSecondary,
    outline = NewsDarkSurfaceBorder
)

@Composable
fun MyApplicationTheme(
    content: @Composable () -> Unit
) {
    // Television news apps have a consistent cinematic dark broadcast styling
    MaterialTheme(
        colorScheme = DarkColorScheme,
        typography = Typography,
        content = content
    )
}
