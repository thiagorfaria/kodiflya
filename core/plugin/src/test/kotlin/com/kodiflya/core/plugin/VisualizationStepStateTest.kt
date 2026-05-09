package com.kodiflya.core.plugin

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

// Validates the stateless rendering contract: each VisualizationStep must carry enough data
// for a Composable to draw a complete frame without replaying prior steps.
// These tests run before any canvas Composable is written.
class VisualizationStepStateTest {

    @Test
    fun `VisualizationStep Sort carries full array snapshot in every step`() {
        val values = intArrayOf(3, 1, 2)
        val step = VisualizationStep.Sort(
            values = values,
            comparing = setOf(0, 1),
            swapping = emptySet(),
            sorted = emptySet(),
            pivot = null,
            metrics = SortMetrics(1L, 0L, 1L),
        )

        // Renderer can draw all bars from step.values alone — no history needed
        assertEquals(3, step.values.size)
        assertTrue(step.values.contentEquals(intArrayOf(3, 1, 2)))
    }

    @Test
    fun `VisualizationStep Grid carries full 9x9 cell map for stateless rendering`() {
        val gridSize = 9
        val fullCellMap = buildMap {
            for (row in 0 until gridSize) {
                for (col in 0 until gridSize) {
                    put(Pair(row, col), CellState.OPEN)
                }
            }
        }
        fullCellMap.toMutableMap()[Pair(0, 0)] = CellState.START
        fullCellMap.toMutableMap()[Pair(8, 8)] = CellState.END

        val step = VisualizationStep.Grid(
            cells = fullCellMap,
            frontier = setOf(Pair(0, 1), Pair(1, 0)),
            path = emptyList(),
            metrics = GridMetrics(2L, 0L),
        )

        // Renderer can draw all 81 cells from step.cells alone
        assertEquals(81, step.cells.size)
        // Every cell coordinate is present — no gaps that would require prior state
        for (row in 0 until gridSize) {
            for (col in 0 until gridSize) {
                assertNotNull(step.cells[Pair(row, col)], "Cell ($row,$col) missing from VisualizationStep.Grid")
            }
        }
    }

    @Test
    fun `VisualizationStep Tree carries full node state map for stateless rendering`() {
        // 7-node BST: nodes 1..7
        val nodeStates = mapOf(
            1 to NodeState.VISITED,
            2 to NodeState.ACTIVE,
            3 to NodeState.DEFAULT,
            4 to NodeState.VISITED,
            5 to NodeState.DEFAULT,
            6 to NodeState.DEFAULT,
            7 to NodeState.DEFAULT,
        )
        val step = VisualizationStep.Tree(
            nodeStates = nodeStates,
            traversalSequence = listOf(4, 2, 1),
            metrics = TreeMetrics(3L, 7L, 3L),
        )

        // Renderer can draw all nodes from step.nodeStates alone — no history needed
        assertEquals(7, step.nodeStates.size)
        for (nodeId in 1..7) {
            assertNotNull(step.nodeStates[nodeId], "Node $nodeId missing from VisualizationStep.Tree")
        }
        // Traversal sequence is the full accumulated list — renderer can display it directly
        assertEquals(listOf(4, 2, 1), step.traversalSequence)
    }

    @Test
    fun `simulated BFS produces steps with full cell maps`() {
        // Simulate 5 BFS steps and verify each carries full 9x9 state
        val gridSize = 9
        val visited = mutableMapOf<Pair<Int, Int>, CellState>()

        // Initialize full grid
        for (row in 0 until gridSize) {
            for (col in 0 until gridSize) {
                visited[Pair(row, col)] = CellState.OPEN
            }
        }
        visited[Pair(0, 0)] = CellState.START
        visited[Pair(8, 8)] = CellState.END

        val steps = mutableListOf<VisualizationStep.Grid>()
        val toVisit = listOf(Pair(0, 1), Pair(1, 0), Pair(0, 2), Pair(1, 1), Pair(2, 0))

        toVisit.forEach { cell ->
            visited[cell] = CellState.VISITED
            // Each step is a SNAPSHOT of the full grid — not a delta
            steps.add(
                VisualizationStep.Grid(
                    cells = visited.toMap(), // full copy
                    frontier = emptySet(),
                    path = emptyList(),
                    metrics = GridMetrics(steps.size + 1L, 0L),
                )
            )
        }

        // Every step must allow stateless rendering
        steps.forEach { step ->
            assertEquals(81, step.cells.size,
                "Step must carry full 81-cell map for stateless rendering")
        }
    }

    @Test
    fun `simulated Inorder BST produces steps with full traversal sequence`() {
        // Inorder on [4, 2, 1, 3, 6, 5, 7] produces sequence [1, 2, 3, 4, 5, 6, 7]
        val accumulatedSequence = mutableListOf<Int>()
        val inorderVisit = listOf(1, 2, 3, 4, 5, 6, 7)
        val steps = mutableListOf<VisualizationStep.Tree>()

        inorderVisit.forEach { nodeId ->
            accumulatedSequence.add(nodeId)
            steps.add(
                VisualizationStep.Tree(
                    nodeStates = mapOf(
                        1 to if (nodeId > 1) NodeState.VISITED else NodeState.ACTIVE,
                        2 to if (nodeId > 2) NodeState.VISITED else if (nodeId == 2) NodeState.ACTIVE else NodeState.DEFAULT,
                        3 to if (nodeId > 3) NodeState.VISITED else if (nodeId == 3) NodeState.ACTIVE else NodeState.DEFAULT,
                        4 to NodeState.DEFAULT,
                        5 to NodeState.DEFAULT,
                        6 to NodeState.DEFAULT,
                        7 to NodeState.DEFAULT,
                    ),
                    traversalSequence = accumulatedSequence.toList(), // full accumulated sequence
                    metrics = TreeMetrics(accumulatedSequence.size.toLong(), 7L, 3L),
                )
            )
        }

        // Each step must carry the full accumulated traversal sequence (not just the latest node)
        steps.forEachIndexed { index, step ->
            assertEquals(index + 1, step.traversalSequence.size,
                "Step $index must carry full accumulated traversal sequence")
        }

        // Final step has the complete inorder sequence
        assertEquals(listOf(1, 2, 3, 4, 5, 6, 7), steps.last().traversalSequence)
    }
}
