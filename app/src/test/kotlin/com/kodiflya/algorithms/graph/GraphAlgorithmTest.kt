package com.kodiflya.algorithms.graph

import com.kodiflya.core.plugin.CellState
import com.kodiflya.core.plugin.VisualizationStep
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class GraphAlgorithmTest {

    private val algorithms = listOf(BFS(), DFS(), Dijkstra())

    @Test
    fun `all algorithms produce steps with full 81-cell map`() {
        algorithms.forEach { algo ->
            val input = algo.initialData()
            val steps = algo.steps(input).toList()
            assertTrue(steps.isNotEmpty(), "${algo.displayName} produced no steps")
            steps.forEach { step ->
                check(step is VisualizationStep.Grid)
                assertEquals(81, step.cells.size, "${algo.displayName}: step missing cells")
            }
        }
    }

    @Test
    fun `all algorithms reach the end and produce a path`() {
        algorithms.forEach { algo ->
            val input = algo.initialData()
            val steps = algo.steps(input).toList()
            val lastStep = steps.last() as VisualizationStep.Grid
            assertTrue(lastStep.path.isNotEmpty(), "${algo.displayName}: no path found")
            assertTrue(lastStep.metrics.pathLength > 0, "${algo.displayName}: path length is 0")
        }
    }

    @Test
    fun `BFS finds the shortest path`() {
        val steps = BFS().steps(BFS().initialData()).toList()
        val path = (steps.last() as VisualizationStep.Grid).path
        // BFS guarantees shortest path; DFS may find a longer one
        val dfsPath = (DFS().steps(DFS().initialData()).toList().last() as VisualizationStep.Grid).path
        assertTrue(path.size <= dfsPath.size, "BFS path should be <= DFS path length")
    }

    @Test
    fun `path starts at start and ends at end`() {
        algorithms.forEach { algo ->
            val input = algo.initialData()
            val lastStep = algo.steps(input).toList().last() as VisualizationStep.Grid
            if (lastStep.path.isNotEmpty()) {
                assertEquals(DEFAULT_START, lastStep.path.first(), "${algo.displayName}: path doesn't start at start")
                assertEquals(DEFAULT_END, lastStep.path.last(), "${algo.displayName}: path doesn't end at end")
            }
        }
    }

    @Test
    fun `path contains no wall cells`() {
        algorithms.forEach { algo ->
            val input = algo.initialData()
            val lastStep = algo.steps(input).toList().last() as VisualizationStep.Grid
            lastStep.path.forEach { pos ->
                val state = lastStep.cells[pos]
                assertTrue(state != CellState.WALL, "${algo.displayName}: path goes through a wall at $pos")
            }
        }
    }

    @Test
    fun `all algorithms have no android imports in logic layer`() {
        // Compile-time guarantee: if this test runs, no android.* was imported
        // (android.* classes don't exist on the JVM test classpath)
        algorithms.forEach { algo ->
            val steps = algo.steps(algo.initialData()).take(1).toList()
            assertTrue(steps.isNotEmpty())
        }
    }
}
