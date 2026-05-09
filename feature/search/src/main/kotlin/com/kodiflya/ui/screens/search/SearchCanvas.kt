package com.kodiflya.ui.screens.search

import androidx.compose.foundation.Canvas
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.TextMeasurer
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.unit.sp
import com.kodiflya.core.plugin.VisualizationStep
import kotlin.math.ceil

private const val SEARCH_COLUMNS = 10
private val eliminatedColor = Color(0xFF252525)

@Composable
fun SearchCanvas(
    step: VisualizationStep.Search?,
    modifier: Modifier = Modifier,
) {
    val textMeasurer = rememberTextMeasurer()
    val primary = MaterialTheme.colorScheme.primary
    val secondary = MaterialTheme.colorScheme.secondary
    val surfaceVariant = MaterialTheme.colorScheme.surfaceVariant
    val onSurfaceVariant = MaterialTheme.colorScheme.onSurfaceVariant
    val background = MaterialTheme.colorScheme.background
    val outline = MaterialTheme.colorScheme.outline

    Canvas(modifier = modifier) {
        val values = step?.values ?: IntArray(100) { it + 1 }
        val n = values.size
        val cols = SEARCH_COLUMNS
        val rows = ceil(n.toFloat() / cols).toInt()

        val gap = 2f
        val maxCellWidth = (size.width - gap * (cols - 1)) / cols
        val maxCellHeight = (size.height - gap * (rows - 1)) / rows
        val cellSize = minOf(maxCellWidth, maxCellHeight)

        val totalWidth = cellSize * cols + gap * (cols - 1)
        val totalHeight = cellSize * rows + gap * (rows - 1)
        val startX = (size.width - totalWidth) / 2f
        val startY = (size.height - totalHeight) / 2f

        val rangeLeft = step?.left
        val rangeRight = step?.right

        values.forEachIndexed { i, value ->
            val row = i / cols
            val col = i % cols
            val cellX = startX + col * (cellSize + gap)
            val cellY = startY + row * (cellSize + gap)

            val isOutOfRange = rangeLeft != null && rangeRight != null &&
                (i < rangeLeft || i > rangeRight)
            val isEliminated = step?.eliminated?.contains(i) == true

            val cellColor = when {
                step == null -> surfaceVariant
                i == step.found -> primary
                i == step.current -> secondary
                isEliminated || isOutOfRange -> eliminatedColor
                else -> surfaceVariant
            }
            val textColor = when (cellColor) {
                primary, secondary -> background
                else -> onSurfaceVariant
            }

            drawRoundRect(
                color = cellColor,
                topLeft = Offset(cellX, cellY),
                size = Size(cellSize, cellSize),
                cornerRadius = CornerRadius(4f, 4f),
            )
            drawRoundRect(
                color = outline.copy(alpha = if (cellColor == eliminatedColor) 0.3f else 1f),
                topLeft = Offset(cellX, cellY),
                size = Size(cellSize, cellSize),
                cornerRadius = CornerRadius(4f, 4f),
                style = Stroke(width = 1.5f),
            )

            val center = Offset(cellX + cellSize / 2f, cellY + cellSize / 2f)
            drawCellValue(textMeasurer, value.toString(), center, cellSize, textColor)
        }
    }
}

private fun DrawScope.drawCellValue(
    measurer: TextMeasurer,
    text: String,
    center: Offset,
    cellSize: Float,
    color: Color,
) {
    val style = TextStyle(
        fontSize = (cellSize * 0.32f / density).sp,
        fontWeight = FontWeight.Bold,
        color = color,
    )
    val measured = measurer.measure(text, style)
    drawText(
        textLayoutResult = measured,
        topLeft = Offset(center.x - measured.size.width / 2f, center.y - measured.size.height / 2f),
    )
}
