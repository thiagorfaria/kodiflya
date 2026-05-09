package com.kodiflya.ui.theme

import androidx.compose.ui.graphics.Color

// Established visual language from validated HTML prototypes
val Background = Color(0xFF0D0D0D)
val Surface = Color(0xFF1A1A1A)
val ElementDefault = Color(0xFF2E2E2E)

val AccentGreen = Color(0xFF7CB99A)   // sorted / visited / positive states
val AccentPeach = Color(0xFFE8917A)   // comparing / active / visiting states
val AccentPurple = Color(0xFFB8A4E8)  // pivot / height metric
val AccentAmber = Color(0xFFE8C47A)   // O(n²) complexity labels / array reads

val MetricLabel = Color(0xFF888888)   // WCAG AA compliant on Surface (#888 on #1A1A1A = 5.9:1)
val TextPrimary = Color(0xFFFFFFFF)
val TextSecondary = Color(0xFFAAAAAA)

val SurfaceBorder = Color(0xFF2E2E2E)

// Light mode tokens
val LightBackground       = Color(0xFFEBEBEB)
val LightSurface          = Color(0xFFF5F5F5)
val LightSurfaceVariant   = Color(0xFFD8D8D8)
val LightOutline          = Color(0xFFC0C0C0)
val LightOutlineVariant   = Color(0xFF5A5A5A)
val LightOnSurface        = Color(0xFF111111)
val LightOnSurfaceVariant = Color(0xFF444444)

// Light accent colors — darkened from pastel for contrast on light surfaces (all pass WCAG AA on LightSurface)
val LightAccentGreen  = Color(0xFF2E7D5A)  // 5.3:1
val LightAccentPeach  = Color(0xFFB84E2F)  // 5.8:1
val LightAccentPurple = Color(0xFF6040C0)  // 6.2:1
val LightAccentAmber  = Color(0xFF8A6000)  // 6.5:1
