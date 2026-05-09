package com.kodiflya.ui.screens.graph

import androidx.compose.foundation.Canvas
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import com.kodiflya.core.plugin.AlgorithmInput
import com.kodiflya.core.plugin.CellState
import com.kodiflya.core.plugin.VisualizationStep

private const val GRID_SIZE = 9
private val CellOpen = Color(0xFF252525) // slightly lighter than Surface to show grid structure

@Composable
fun GraphCanvas(
    step: VisualizationStep.Grid?,
    initialGrid: AlgorithmInput.GridInput,
    modifier: Modifier = Modifier,
) {
    val primary = MaterialTheme.colorScheme.primary
    val secondary = MaterialTheme.colorScheme.secondary
    val tertiary = MaterialTheme.colorScheme.tertiary
    val surfaceVariant = MaterialTheme.colorScheme.surfaceVariant

    Canvas(modifier = modifier) {
        val gap = 3f
        val cellSize = (minOf(size.width, size.height) - gap * (GRID_SIZE - 1)) / GRID_SIZE
        val offsetX = (size.width - (cellSize * GRID_SIZE + gap * (GRID_SIZE - 1))) / 2f
        val offsetY = (size.height - (cellSize * GRID_SIZE + gap * (GRID_SIZE - 1))) / 2f
        val cornerRadius = CornerRadius(4f, 4f)

        for (row in 0 until GRID_SIZE) {
            for (col in 0 until GRID_SIZE) {
                val pos = Pair(col, row)
                val cellState = step?.cells?.get(pos) ?: idleCellState(pos, initialGrid)
                val color = when (cellState) {
                    CellState.START -> primary
                    CellState.END -> primary
                    CellState.WALL -> surfaceVariant
                    CellState.VISITED -> tertiary
                    CellState.FRONTIER -> secondary
                    CellState.PATH -> primary
                    CellState.OPEN -> CellOpen
                }

                drawRoundRect(
                    color = color,
                    topLeft = Offset(
                        offsetX + col * (cellSize + gap),
                        offsetY + row * (cellSize + gap),
                    ),
                    size = Size(cellSize, cellSize),
                    cornerRadius = cornerRadius,
                )
            }
        }
    }
}

private fun idleCellState(pos: Pair<Int, Int>, grid: AlgorithmInput.GridInput): CellState = when {
    pos == grid.start -> CellState.START
    pos == grid.end -> CellState.END
    pos in grid.walls -> CellState.WALL
    else -> CellState.OPEN
}
