package com.kodiflya.ui.screens.sorting

import androidx.compose.foundation.Canvas
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import com.kodiflya.core.plugin.VisualizationStep

@Composable
fun SortingCanvas(
    step: VisualizationStep.Sort?,
    modifier: Modifier = Modifier,
) {
    val primary = MaterialTheme.colorScheme.primary
    val secondary = MaterialTheme.colorScheme.secondary
    val tertiary = MaterialTheme.colorScheme.tertiary
    val surfaceVariant = MaterialTheme.colorScheme.surfaceVariant

    Canvas(modifier = modifier) {
        val values = step?.values ?: IntArray(10) { it + 1 }
        val n = values.size
        val maxValue = values.max().coerceAtLeast(1)

        val gapDp = 2f
        val totalGap = gapDp * (n - 1)
        val barWidth = (size.width - totalGap) / n
        val cornerRadius = CornerRadius(2f, 2f)

        values.forEachIndexed { i, value ->
            val barHeight = (value.toFloat() / maxValue) * size.height
            val x = i * (barWidth + gapDp)
            val y = size.height - barHeight

            val color = when {
                step == null -> surfaceVariant
                i in (step.sorted) -> primary
                i in (step.swapping) -> secondary
                i in (step.comparing) -> secondary
                step.pivot == i -> tertiary
                else -> surfaceVariant
            }

            drawRoundRect(
                color = color,
                topLeft = Offset(x, y),
                size = Size(barWidth, barHeight),
                cornerRadius = cornerRadius,
            )
        }
    }
}
