package com.kodiflya.ui.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.unit.dp

@Composable
fun ComplexityRainbowBar(
    modifier: Modifier = Modifier,
    fill: Float = 1f,
) {
    val green     = MaterialTheme.colorScheme.primary
    val blue      = MaterialTheme.colorScheme.primaryContainer
    val moss      = MaterialTheme.colorScheme.secondaryContainer
    val amber     = MaterialTheme.colorScheme.error
    val peach     = MaterialTheme.colorScheme.secondary
    val dustyRose = MaterialTheme.colorScheme.tertiaryContainer
    val purple    = MaterialTheme.colorScheme.tertiary

    val colors = listOf(green, blue, moss, amber, peach, dustyRose, purple)
    val clampedFill = fill.coerceIn(0f, 1f)

    Box(
        modifier = modifier
            .height(3.dp)
            .clip(RoundedCornerShape(2.dp))
            .drawBehind {
                if (clampedFill > 0f) {
                    drawRect(
                        brush = Brush.linearGradient(
                            colors = colors,
                            start = Offset.Zero,
                            end = Offset(size.width, 0f),
                        ),
                        size = Size(size.width * clampedFill, size.height),
                    )
                }
            },
    )
}
