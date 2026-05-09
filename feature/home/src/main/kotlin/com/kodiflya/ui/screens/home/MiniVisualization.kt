package com.kodiflya.ui.screens.home

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope

private val highlighted = setOf(2, 5)
private val baseHeights = listOf(0.55f, 0.30f, 0.80f, 0.45f, 0.65f, 0.90f, 0.35f, 0.70f)

@Composable
fun SortingMiniVisualization(modifier: Modifier = Modifier) {
    val primary = MaterialTheme.colorScheme.primary
    val surfaceVariant = MaterialTheme.colorScheme.surfaceVariant

    val transition = rememberInfiniteTransition(label = "sort")
    val animatedHeights = baseHeights.mapIndexed { i, base ->
        val target = if (base > 0.5f) base - 0.25f else base + 0.25f
        transition.animateFloat(
            initialValue = base,
            targetValue = target,
            animationSpec = infiniteRepeatable(
                animation = tween(durationMillis = 1200 + i * 80),
                repeatMode = RepeatMode.Reverse,
            ),
            label = "bar$i",
        ).value
    }

    Canvas(modifier = modifier.fillMaxSize()) {
        val barCount = baseHeights.size
        val gap = size.width * 0.05f / (barCount - 1)
        val barWidth = (size.width - gap * (barCount - 1)) / barCount
        animatedHeights.forEachIndexed { i, h ->
            val barH = size.height * h.coerceIn(0.05f, 1f)
            val color = if (i in highlighted) primary else surfaceVariant
            drawBar(
                x = i * (barWidth + gap),
                barWidth = barWidth,
                barHeight = barH,
                totalHeight = size.height,
                color = color,
            )
        }
    }
}

private fun DrawScope.drawBar(x: Float, barWidth: Float, barHeight: Float, totalHeight: Float, color: Color) {
    drawRoundRect(
        color = color,
        topLeft = Offset(x, totalHeight - barHeight),
        size = Size(barWidth, barHeight),
        cornerRadius = CornerRadius(2f, 2f),
    )
}

private val nodePositions = listOf(
    Offset(0.08f, 0.5f),
    Offset(0.35f, 0.15f),
    Offset(0.35f, 0.85f),
    Offset(0.65f, 0.5f),
    Offset(0.92f, 0.5f),
)
private val edges = listOf(0 to 1, 0 to 2, 1 to 3, 2 to 3, 3 to 4)

@Composable
fun GraphMiniVisualization(modifier: Modifier = Modifier) {
    val tertiary = MaterialTheme.colorScheme.tertiary
    val surfaceVariant = MaterialTheme.colorScheme.surfaceVariant

    val transition = rememberInfiniteTransition(label = "graph")
    val pulseAlpha by transition.animateFloat(
        initialValue = 0.4f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 900),
            repeatMode = RepeatMode.Reverse,
        ),
        label = "pulse",
    )

    Canvas(modifier = modifier.fillMaxSize()) {
        val nodeRadius = size.minDimension * 0.09f
        val positions = nodePositions.map { Offset(it.x * size.width, it.y * size.height) }

        edges.forEach { (a, b) ->
            drawLine(
                color = surfaceVariant,
                start = positions[a],
                end = positions[b],
                strokeWidth = 1.5f,
            )
        }

        positions.forEachIndexed { i, pos ->
            val color = if (i == 0) tertiary.copy(alpha = pulseAlpha) else surfaceVariant
            drawCircle(color = color, radius = nodeRadius, center = pos)
        }
    }
}

private val treeNodes = listOf(
    Offset(0.5f, 0.1f),
    Offset(0.25f, 0.45f),
    Offset(0.75f, 0.45f),
    Offset(0.1f, 0.85f),
    Offset(0.4f, 0.85f),
)
private val treeEdges = listOf(0 to 1, 0 to 2, 1 to 3, 1 to 4)

@Composable
fun TreeMiniVisualization(modifier: Modifier = Modifier) {
    val error = MaterialTheme.colorScheme.error
    val surfaceVariant = MaterialTheme.colorScheme.surfaceVariant

    val transition = rememberInfiniteTransition(label = "tree")
    val phase by transition.animateFloat(
        initialValue = 0f,
        targetValue = treeNodes.size.toFloat(),
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 2500),
            repeatMode = RepeatMode.Restart,
        ),
        label = "traverse",
    )
    val activeIndex = phase.toInt().coerceIn(0, treeNodes.lastIndex)

    Canvas(modifier = modifier.fillMaxSize()) {
        val nodeRadius = size.minDimension * 0.09f
        val positions = treeNodes.map { Offset(it.x * size.width, it.y * size.height) }

        treeEdges.forEach { (a, b) ->
            drawLine(
                color = surfaceVariant,
                start = positions[a],
                end = positions[b],
                strokeWidth = 1.5f,
            )
        }

        positions.forEachIndexed { i, pos ->
            val color = if (i == activeIndex) error else surfaceVariant
            drawCircle(color = color, radius = nodeRadius, center = pos)
        }
    }
}

private const val MINI_SEARCH_COLS = 4
private const val MINI_SEARCH_ROWS = 2
private const val MINI_SEARCH_FOUND = 5

@Composable
fun SearchMiniVisualization(modifier: Modifier = Modifier) {
    val secondary = MaterialTheme.colorScheme.secondary
    val primary = MaterialTheme.colorScheme.primary
    val surfaceVariant = MaterialTheme.colorScheme.surfaceVariant
    val eliminatedColor = Color(0xFF252525)

    val total = MINI_SEARCH_COLS * MINI_SEARCH_ROWS
    val transition = rememberInfiniteTransition(label = "search")
    val phase by transition.animateFloat(
        initialValue = 0f,
        targetValue = total.toFloat(),
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 2400, easing = LinearEasing),
            repeatMode = RepeatMode.Restart,
        ),
        label = "scan",
    )
    val activeIndex = phase.toInt().coerceIn(0, total - 1)

    Canvas(modifier = modifier.fillMaxSize()) {
        val gap = 2f
        val cellWidth = (size.width - gap * (MINI_SEARCH_COLS - 1)) / MINI_SEARCH_COLS
        val cellHeight = (size.height - gap * (MINI_SEARCH_ROWS - 1)) / MINI_SEARCH_ROWS

        repeat(total) { i ->
            val row = i / MINI_SEARCH_COLS
            val col = i % MINI_SEARCH_COLS
            val cellColor = when {
                activeIndex > MINI_SEARCH_FOUND && i == MINI_SEARCH_FOUND -> primary
                i == activeIndex -> secondary
                i < activeIndex -> eliminatedColor
                else -> surfaceVariant
            }
            drawRoundRect(
                color = cellColor,
                topLeft = Offset(col * (cellWidth + gap), row * (cellHeight + gap)),
                size = Size(cellWidth, cellHeight),
                cornerRadius = CornerRadius(2f, 2f),
            )
        }
    }
}
