package com.kodiflya.ui.screens.trees

import androidx.compose.foundation.Canvas
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.TextMeasurer
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.unit.sp
import com.kodiflya.algorithms.trees.EDGES
import com.kodiflya.algorithms.trees.NODE_POSITIONS
import com.kodiflya.core.plugin.NodeState
import com.kodiflya.core.plugin.VisualizationStep

@Composable
fun TreeCanvas(
    step: VisualizationStep.Tree?,
    modifier: Modifier = Modifier,
) {
    val textMeasurer = rememberTextMeasurer()
    val primary = MaterialTheme.colorScheme.primary
    val secondary = MaterialTheme.colorScheme.secondary
    val background = MaterialTheme.colorScheme.background
    val surfaceVariant = MaterialTheme.colorScheme.surfaceVariant
    val outline = MaterialTheme.colorScheme.outline

    Canvas(modifier = modifier) {
        val nodeRadius = minOf(size.width, size.height) * 0.075f

        EDGES.forEach { (parent, child) ->
            val (px, py) = NODE_POSITIONS[parent]!!
            val (cx, cy) = NODE_POSITIONS[child]!!
            drawLine(
                color = outline,
                start = Offset(px * size.width, py * size.height),
                end = Offset(cx * size.width, cy * size.height),
                strokeWidth = 2f,
            )
        }

        NODE_POSITIONS.forEach { (value, pos) ->
            val (fx, fy) = pos
            val center = Offset(fx * size.width, fy * size.height)
            val nodeState = step?.nodeStates?.get(value) ?: NodeState.DEFAULT

            val fillColor = when (nodeState) {
                NodeState.ACTIVE -> secondary
                NodeState.VISITED -> primary
                NodeState.DEFAULT -> surfaceVariant
            }

            drawCircle(color = fillColor, radius = nodeRadius, center = center, style = Fill)
            drawCircle(color = outline, radius = nodeRadius, center = center, style = Stroke(width = 2f))

            drawNodeLabel(textMeasurer, value.toString(), center, nodeRadius, nodeState, primary, background)
        }
    }
}

private fun DrawScope.drawNodeLabel(
    measurer: TextMeasurer,
    text: String,
    center: Offset,
    nodeRadius: Float,
    state: NodeState,
    primary: Color,
    background: Color,
) {
    val textColor = when (state) {
        NodeState.ACTIVE, NodeState.VISITED -> background
        NodeState.DEFAULT -> primary
    }
    val style = TextStyle(fontSize = (nodeRadius * 0.7f / density).sp, fontWeight = FontWeight.Bold, color = textColor)
    val measured = measurer.measure(text, style)
    drawText(
        textLayoutResult = measured,
        topLeft = Offset(center.x - measured.size.width / 2f, center.y - measured.size.height / 2f),
    )
}
