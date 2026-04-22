package com.kodiflya.ui.sorting

import androidx.compose.foundation.Canvas
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import com.kodiflya.core.plugin.VisualizationStep
import com.kodiflya.ui.theme.AccentGreen
import com.kodiflya.ui.theme.AccentPeach
import com.kodiflya.ui.theme.AccentPurple
import com.kodiflya.ui.theme.ElementDefault

@Composable
fun SortingCanvas(
    step: VisualizationStep.Sort?,
    modifier: Modifier = Modifier,
) {
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
                step == null -> ElementDefault
                i in (step.sorted) -> AccentGreen
                i in (step.swapping) -> AccentPeach
                i in (step.comparing) -> AccentPeach
                step.pivot == i -> AccentPurple
                else -> ElementDefault
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
