package com.kodiflya.ui.theme

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

private val KodiflyaDarkColorScheme = darkColorScheme(
    primary = AccentGreen,
    onPrimary = Background,
    primaryContainer = AccentBlue,
    secondaryContainer = AccentMoss,
    tertiaryContainer = AccentDustyRose,
    secondary = AccentPeach,
    onSecondary = Background,
    tertiary = AccentPurple,
    error = AccentAmber,
    background = Background,
    onBackground = TextPrimary,
    surface = Surface,
    onSurface = TextPrimary,
    surfaceVariant = ElementDefault,
    onSurfaceVariant = TextSecondary,
    outline = SurfaceBorder,
    outlineVariant = MetricLabel,
)

private val KodiflyaLightColorScheme = lightColorScheme(
    primary           = LightAccentGreen,
    onPrimary         = Color.White,
    primaryContainer  = LightAccentBlue,
    secondaryContainer = LightAccentMoss,
    tertiaryContainer = LightAccentDustyRose,
    secondary         = LightAccentPeach,
    onSecondary       = Color.White,
    tertiary          = LightAccentPurple,
    error             = LightAccentAmber,
    background        = LightBackground,
    onBackground      = LightOnSurface,
    surface           = LightSurface,
    onSurface         = LightOnSurface,
    surfaceVariant    = LightSurfaceVariant,
    onSurfaceVariant  = LightOnSurfaceVariant,
    outline           = LightOutline,
    outlineVariant    = LightOutlineVariant,
)

@Composable
fun KodiflyaTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> KodiflyaDarkColorScheme
        else -> KodiflyaLightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = KodiflyaTypography, // Completed the name
        content = content,
    )
}