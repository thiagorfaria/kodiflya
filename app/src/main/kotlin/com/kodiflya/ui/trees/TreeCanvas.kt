package com.kodiflya.ui.trees

import androidx.compose.foundation.Canvas
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
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
import com.kodiflya.core.plugin.VizStep
import com.kodiflya.ui.theme.AccentGreen
import com.kodiflya.ui.theme.AccentPeach
import com.kodiflya.ui.theme.Background
import com.kodiflya.ui.theme.ElementDefault
import com.kodiflya.ui.theme.SurfaceBorder

@Composable
fun TreeCanvas(
    step: VizStep.Tree?,
    modifier: Modifier = Modifier,
) {
    val textMeasurer = rememberTextMeasurer()

    Canvas(modifier = modifier) {
        val nodeRadius = minOf(size.width, size.height) * 0.075f

        // Draw edges first (behind nodes)
        EDGES.forEach { (parent, child) ->
            val (px, py) = NODE_POSITIONS[parent]!!
            val (cx, cy) = NODE_POSITIONS[child]!!
            drawLine(
                color = SurfaceBorder,
                start = Offset(px * size.width, py * size.height),
                end = Offset(cx * size.width, cy * size.height),
                strokeWidth = 2f,
            )
        }

        // Draw nodes
        NODE_POSITIONS.forEach { (value, pos) ->
            val (fx, fy) = pos
            val center = Offset(fx * size.width, fy * size.height)
            val nodeState = step?.nodeStates?.get(value) ?: NodeState.DEFAULT

            val fillColor = when (nodeState) {
                NodeState.ACTIVE -> AccentPeach
                NodeState.VISITED -> AccentGreen
                NodeState.DEFAULT -> ElementDefault
            }

            drawCircle(color = fillColor, radius = nodeRadius, center = center, style = Fill)
            drawCircle(color = SurfaceBorder, radius = nodeRadius, center = center, style = Stroke(width = 2f))

            drawNodeLabel(textMeasurer, value.toString(), center, nodeRadius, nodeState)
        }
    }
}

private fun DrawScope.drawNodeLabel(
    measurer: TextMeasurer,
    text: String,
    center: Offset,
    nodeRadius: Float,
    state: NodeState,
) {
    val textColor = when (state) {
        NodeState.ACTIVE, NodeState.VISITED -> Background
        NodeState.DEFAULT -> AccentGreen
    }
    val style = TextStyle(fontSize = (nodeRadius * 0.7f / density).sp, fontWeight = FontWeight.Bold, color = textColor)
    val measured = measurer.measure(text, style)
    drawText(
        textLayoutResult = measured,
        topLeft = Offset(center.x - measured.size.width / 2f, center.y - measured.size.height / 2f),
    )
}
