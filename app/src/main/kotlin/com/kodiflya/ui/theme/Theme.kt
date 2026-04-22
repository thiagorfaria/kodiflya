package com.kodiflya.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

private val KodiflyaDarkColorScheme = darkColorScheme(
    primary = AccentGreen,
    onPrimary = Background,
    secondary = AccentPeach,
    onSecondary = Background,
    tertiary = AccentPurple,
    background = Background,
    onBackground = TextPrimary,
    surface = Surface,
    onSurface = TextPrimary,
    surfaceVariant = ElementDefault,
    onSurfaceVariant = TextSecondary,
    outline = SurfaceBorder,
)

// Tip: When you're ready for Light Mode, define KodiflyaLightColorScheme here.

@Composable
fun KodiflyaTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> KodiflyaDarkColorScheme
        else -> KodiflyaDarkColorScheme // Falling back to Dark for now to avoid breaking the UI
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = KodiflyaTypography, // Completed the name
        content = content,
    )
}