package com.example.playlistmaker.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

private val LightColorScheme = lightColorScheme(
    primary = BackgroundColor, //  background_color
    secondary = BlackText, //  blue_text
    onTertiary = BlueText, // blue_text
    tertiary = TextGray, // text_gray
    background = White, // white
    surface = LightGray, // switcher_on_color
    onPrimary = BlackText, // black_text
    onSecondary = ElementsColor, // elements_color
    onBackground = BlackText, // black_text
    onSurface = TextGray,
    surfaceVariant = LightGray,
    onSurfaceVariant = TextGray
)

private val DarkColorScheme = darkColorScheme(
    primary = BackgroundNavbar, // background_navbar
    secondary = White, //  white
    onTertiary = BlueText, // blue_text
    tertiary = Color(0xfff6d54f), //  white
    background = BlackText, // background_color_dark
    surface = White,
    onPrimary = White, // white
    onSecondary = LightGray, // light_gray
    onBackground = White, // text_color
    onSurface = White,
    surfaceVariant = White,
    onSurfaceVariant = BlackText
)

@Composable
fun AppTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}