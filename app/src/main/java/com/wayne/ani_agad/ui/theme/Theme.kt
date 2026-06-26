package com.wayne.ani_agad.ui.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val AppColorScheme = lightColorScheme(
    primary = ForestDeep,
    onPrimary = Color.White,
    primaryContainer = MintLight,
    onPrimaryContainer = BlackText,
    secondary = EmeraldMedium,
    onSecondary = Color.White,
    tertiary = PeachAccent,
    onTertiary = BlackText,
    background = DarkGreenBg,
    onBackground = Color.White,
    surface = WhiteSurface,
    onSurface = BlackText,
    outline = GrayOutline
)

// Global Gradient Brush for Backgrounds
val AppGradient = Brush.verticalGradient(
    colors = listOf(DarkGreenBg, DarkNavyBg)
)

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = DarkGreenBg.toArgb()
            window.navigationBarColor = DarkNavyBg.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = false
        }
    }

    MaterialTheme(
        colorScheme = AppColorScheme,
        typography = Typography,
        content = content
    )
}
