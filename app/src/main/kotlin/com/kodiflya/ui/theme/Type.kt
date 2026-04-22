package com.kodiflya.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

// Visual language: Space Mono for metrics (monospace), DM Sans for labels (sans-serif).
// Using system font families for v1; replace with GoogleFont provider in production
// by adding ui-text-google-fonts dependency and the GMS cert array resource.
val SpaceMonoFamily: FontFamily = FontFamily.Monospace
val DmSansFamily: FontFamily = FontFamily.SansSerif

// All sizes are +30% from the previous values (×1.3, rounded to nearest whole sp).
val KodiflyaTypography = Typography(
    headlineMedium = TextStyle(   // 20 × 1.3 = 26
        fontFamily = DmSansFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 26.sp,
        lineHeight = 34.sp,
    ),
    titleMedium = TextStyle(      // 14 × 1.3 = 18
        fontFamily = DmSansFamily,
        fontWeight = FontWeight.Medium,
        fontSize = 18.sp,
        lineHeight = 24.sp,
    ),
    bodyMedium = TextStyle(       // 14 × 1.3 = 18
        fontFamily = DmSansFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 18.sp,
        lineHeight = 24.sp,
    ),
    labelSmall = TextStyle(       // 11 × 1.3 = 14
        fontFamily = DmSansFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp,
        lineHeight = 20.sp,
    ),
    labelMedium = TextStyle(      // 12 × 1.3 = 16
        fontFamily = DmSansFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 22.sp,
    ),
    // Used for metric card values — Space Mono for the "live metrics" feel
    displaySmall = TextStyle(     // 20 × 1.3 = 26
        fontFamily = SpaceMonoFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 26.sp,
        lineHeight = 34.sp,
    ),
)
