package com.kodiflya.ui.component

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.kodiflya.core.plugin.ColorRole

// Single place that maps a plugin ColorRole to the correct MaterialTheme.colorScheme slot.
// Screens and components call this — they never hardcode scheme slots per label position.
// Mapping is anchored to the visual language defined in CLAUDE.md:
//   GREEN  → primary   (sorted / visited / positive)
//   PEACH  → secondary (comparing / active / visiting)
//   PURPLE → tertiary  (pivot / height metric)
//   AMBER  → error     (O(n²) / reads / complexity)
//   NEUTRAL → onSurface
@Composable
fun ColorRole.toColor(): Color = when (this) {
    ColorRole.GREEN   -> MaterialTheme.colorScheme.primary
    ColorRole.PEACH   -> MaterialTheme.colorScheme.secondary
    ColorRole.PURPLE  -> MaterialTheme.colorScheme.tertiary
    ColorRole.AMBER   -> MaterialTheme.colorScheme.error
    ColorRole.NEUTRAL -> MaterialTheme.colorScheme.onSurface
}
