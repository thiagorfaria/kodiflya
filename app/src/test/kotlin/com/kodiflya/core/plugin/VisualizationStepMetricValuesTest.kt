package com.kodiflya.core.plugin

import com.kodiflya.algorithms.graph.BFS
import com.kodiflya.algorithms.graph.DFS
import com.kodiflya.algorithms.graph.Dijkstra
import com.kodiflya.algorithms.sorting.BubbleSort
import com.kodiflya.algorithms.sorting.InsertionSort
import com.kodiflya.algorithms.sorting.MergeSort
import com.kodiflya.algorithms.sorting.QuickSort
import com.kodiflya.algorithms.trees.BSTInorder
import com.kodiflya.algorithms.trees.BSTPostorder
import com.kodiflya.algorithms.trees.BSTPreorder
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

// Validates that metricValues is always in sync with metricLabels for every plugin.
//
// The UI zips metricValues with plugin.metricLabels to populate the metrics panel.
// A size mismatch or index swap is a silent rendering bug: the wrong number appears
// under the wrong label with no crash or compile error. These tests are the only
// safety net for that contract.
class VisualizationStepMetricValuesTest {

    // -------------------------------------------------------------------------
    // VisualizationStep.Sort
    // -------------------------------------------------------------------------

    @Nested
    inner class SortStep {

        // Contract: [0] comparisons, [1] swaps, [2] arrayReads
        // Verified against every sorting plugin's metricLabels.

        @Test
        fun `metricValues size equals BubbleSort metricLabels size`() {
            val plugin = BubbleSort()
            val step = sortStep(SortMetrics(1L, 2L, 3L))
            assertEquals(plugin.metricLabels.size, step.metricValues.size)
        }

        @Test
        fun `metricValues index 0 is comparisons for BubbleSort`() {
            // metricLabels[0] = "Comparisons" — must map to metrics.comparisons
            val step = sortStep(SortMetrics(comparisons = 42L, swaps = 7L, arrayReads = 49L))
            assertEquals(42L, step.metricValues[0],
                "metricValues[0] must be metrics.comparisons (Comparisons)")
        }

        @Test
        fun `metricValues index 1 is swaps for BubbleSort`() {
            // metricLabels[1] = "Swaps" — must map to metrics.swaps
            val step = sortStep(SortMetrics(comparisons = 42L, swaps = 7L, arrayReads = 49L))
            assertEquals(7L, step.metricValues[1],
                "metricValues[1] must be metrics.swaps (Swaps)")
        }

        @Test
        fun `metricValues index 2 is arrayReads for BubbleSort`() {
            // metricLabels[2] = "Passes" in BubbleSort — but metricValues[2] is metrics.arrayReads.
            // BubbleSort computes arrayReads as comparisons + swaps, not a pass counter.
            // This test documents the actual behavior. If "Passes" is intended to show a real
            // pass counter, BubbleSort must add a passes field to SortMetrics and update
            // metricValues[2] accordingly — or rename its label to "Array reads".
            val step = sortStep(SortMetrics(comparisons = 42L, swaps = 7L, arrayReads = 49L))
            assertEquals(49L, step.metricValues[2],
                "metricValues[2] must be metrics.arrayReads (currently labelled 'Passes' in BubbleSort but value is comparisons+swaps)")
        }

        @Test
        fun `metricValues reflects change as comparisons increase`() {
            val stepA = sortStep(SortMetrics(comparisons = 1L, swaps = 0L, arrayReads = 1L))
            val stepB = sortStep(SortMetrics(comparisons = 5L, swaps = 0L, arrayReads = 5L))

            assertTrue(stepB.metricValues[0] > stepA.metricValues[0],
                "metricValues[0] (comparisons) must grow as comparisons increase")
        }

        @Test
        fun `metricValues reflects change as swaps increase`() {
            val stepA = sortStep(SortMetrics(comparisons = 3L, swaps = 0L, arrayReads = 3L))
            val stepB = sortStep(SortMetrics(comparisons = 3L, swaps = 2L, arrayReads = 5L))

            assertEquals(0L, stepA.metricValues[1])
            assertEquals(2L, stepB.metricValues[1],
                "metricValues[1] (swaps) must reflect current swap count")
        }

        @Test
        fun `metricValues size equals InsertionSort metricLabels size`() {
            val plugin = InsertionSort()
            val step = sortStep(SortMetrics(1L, 0L, 1L))
            assertEquals(plugin.metricLabels.size, step.metricValues.size)
        }

        @Test
        fun `metricValues size equals MergeSort metricLabels size`() {
            val plugin = MergeSort()
            val step = sortStep(SortMetrics(1L, 0L, 1L))
            assertEquals(plugin.metricLabels.size, step.metricValues.size)
        }

        @Test
        fun `metricValues size equals QuickSort metricLabels size`() {
            val plugin = QuickSort()
            val step = sortStep(SortMetrics(1L, 0L, 1L))
            assertEquals(plugin.metricLabels.size, step.metricValues.size)
        }

        @Test
        fun `metricValues are exactly three elements`() {
            // Explicitly assert the fixed size rather than relying on a single plugin reference.
            // If a future refactor changes the size, this test catches it independently.
            val step = sortStep(SortMetrics(1L, 2L, 3L))
            assertEquals(3, step.metricValues.size)
        }

        @Test
        fun `metricValues ordering matches BubbleSort live steps`() {
            // Run BubbleSort on a known input and verify that each emitted step's
            // metricValues match its own metrics fields — integration-level check.
            val plugin = BubbleSort()
            val input = AlgorithmInput.SortInput(intArrayOf(3, 1, 2))
            plugin.steps(input).forEach { step ->
                check(step is VisualizationStep.Sort)
                assertEquals(step.metrics.comparisons, step.metricValues[0],
                    "Live Sort step: metricValues[0] must equal metrics.comparisons")
                assertEquals(step.metrics.swaps, step.metricValues[1],
                    "Live Sort step: metricValues[1] must equal metrics.swaps")
                assertEquals(step.metrics.arrayReads, step.metricValues[2],
                    "Live Sort step: metricValues[2] must equal metrics.arrayReads")
            }
        }

        @Test
        fun `InsertionSort live steps metricValues match metrics fields`() {
            val plugin = InsertionSort()
            val input = AlgorithmInput.SortInput(intArrayOf(3, 1, 2))
            plugin.steps(input).forEach { step ->
                check(step is VisualizationStep.Sort)
                assertEquals(step.metrics.comparisons, step.metricValues[0])
                assertEquals(step.metrics.swaps, step.metricValues[1])
                assertEquals(step.metrics.arrayReads, step.metricValues[2])
            }
        }

        private fun sortStep(metrics: SortMetrics) = VisualizationStep.Sort(
            values = intArrayOf(3, 1, 2),
            comparing = emptySet(),
            swapping = emptySet(),
            sorted = emptySet(),
            pivot = null,
            metrics = metrics,
        )
    }

    // -------------------------------------------------------------------------
    // VisualizationStep.Grid
    // -------------------------------------------------------------------------

    @Nested
    inner class GridStep {

        // Contract: [0] visited (metrics.visited), [1] frontier size (frontier.size),
        //           [2] pathLength (metrics.pathLength)
        //
        // Note: [1] is NOT stored in GridMetrics — it is derived from frontier.size.
        // This is the most fragile index because the source (frontier set) is separate
        // from the metrics struct that holds the other two values.

        @Test
        fun `metricValues size equals BFS metricLabels size`() {
            val plugin = BFS()
            val step = gridStep(visited = 5L, frontierSize = 3, pathLength = 0L)
            assertEquals(plugin.metricLabels.size, step.metricValues.size)
        }

        @Test
        fun `metricValues size equals DFS metricLabels size`() {
            val plugin = DFS()
            val step = gridStep(visited = 5L, frontierSize = 3, pathLength = 0L)
            assertEquals(plugin.metricLabels.size, step.metricValues.size)
        }

        @Test
        fun `metricValues size equals Dijkstra metricLabels size`() {
            val plugin = Dijkstra()
            val step = gridStep(visited = 5L, frontierSize = 3, pathLength = 0L)
            assertEquals(plugin.metricLabels.size, step.metricValues.size)
        }

        @Test
        fun `metricValues are exactly three elements`() {
            val step = gridStep(visited = 1L, frontierSize = 2, pathLength = 3L)
            assertEquals(3, step.metricValues.size)
        }

        @Test
        fun `metricValues index 0 is visited count`() {
            // metricLabels[0] = "Visited" for BFS, DFS, Dijkstra
            val step = gridStep(visited = 17L, frontierSize = 4, pathLength = 0L)
            assertEquals(17L, step.metricValues[0],
                "metricValues[0] must be metrics.visited (Visited)")
        }

        @Test
        fun `metricValues index 1 is frontier set size not a metrics field`() {
            // metricLabels[1] = "Frontier" / "Stack depth" — sourced from frontier.size, NOT GridMetrics.
            // GridMetrics has no frontier field; the value is computed from the frontier set directly.
            val step = gridStep(visited = 10L, frontierSize = 6, pathLength = 0L)
            assertEquals(6L, step.metricValues[1],
                "metricValues[1] must be frontier.size (not a GridMetrics field)")
        }

        @Test
        fun `metricValues index 2 is path length`() {
            // metricLabels[2] = "Path length" for BFS, DFS, Dijkstra
            val step = gridStep(visited = 20L, frontierSize = 0, pathLength = 12L)
            assertEquals(12L, step.metricValues[2],
                "metricValues[2] must be metrics.pathLength (Path length)")
        }

        @Test
        fun `metricValues index 1 tracks frontier set size not visited count`() {
            // Explicitly assert they are different: if someone accidentally copies visited
            // into index 1, this test will catch it.
            val step = gridStep(visited = 10L, frontierSize = 3, pathLength = 0L)
            assertEquals(10L, step.metricValues[0])
            assertEquals(3L, step.metricValues[1])
            assertTrue(step.metricValues[0] != step.metricValues[1],
                "metricValues[0] (visited) and metricValues[1] (frontier size) must be independent")
        }

        @Test
        fun `metricValues reflect frontier size change between two steps`() {
            val stepGrowing = gridStep(visited = 1L, frontierSize = 4, pathLength = 0L)
            val stepShrinking = gridStep(visited = 5L, frontierSize = 1, pathLength = 0L)

            assertEquals(4L, stepGrowing.metricValues[1])
            assertEquals(1L, stepShrinking.metricValues[1])
        }

        @Test
        fun `metricValues on final BFS step reflect zero frontier and non-zero path`() {
            val plugin = BFS()
            val lastStep = plugin.steps(plugin.initialData()).toList().last() as VisualizationStep.Grid

            // Final step: search complete, frontier cleared, path traced
            assertEquals(0L, lastStep.metricValues[1],
                "Final step frontier must be empty (metricValues[1] == 0)")
            assertTrue(lastStep.metricValues[2] > 0L,
                "Final step path length must be > 0 (metricValues[2])")
        }

        @Test
        fun `BFS live steps metricValues match their source fields`() {
            val plugin = BFS()
            plugin.steps(plugin.initialData()).forEach { step ->
                check(step is VisualizationStep.Grid)
                assertEquals(step.metrics.visited, step.metricValues[0],
                    "Live Grid step: metricValues[0] must equal metrics.visited")
                assertEquals(step.frontier.size.toLong(), step.metricValues[1],
                    "Live Grid step: metricValues[1] must equal frontier.size")
                assertEquals(step.metrics.pathLength, step.metricValues[2],
                    "Live Grid step: metricValues[2] must equal metrics.pathLength")
            }
        }

        @Test
        fun `DFS live steps metricValues match their source fields`() {
            val plugin = DFS()
            plugin.steps(plugin.initialData()).forEach { step ->
                check(step is VisualizationStep.Grid)
                assertEquals(step.metrics.visited, step.metricValues[0])
                assertEquals(step.frontier.size.toLong(), step.metricValues[1])
                assertEquals(step.metrics.pathLength, step.metricValues[2])
            }
        }

        private fun gridStep(visited: Long, frontierSize: Int, pathLength: Long): VisualizationStep.Grid {
            val frontier = (0 until frontierSize).map { Pair(it, 0) }.toSet()
            return VisualizationStep.Grid(
                cells = emptyMap(),
                frontier = frontier,
                path = emptyList(),
                metrics = GridMetrics(visited = visited, pathLength = pathLength),
            )
        }
    }

    // -------------------------------------------------------------------------
    // VisualizationStep.Tree
    // -------------------------------------------------------------------------

    @Nested
    inner class TreeStep {

        // Contract: [0] visited (metrics.visited), [1] totalNodes (metrics.totalNodes),
        //           [2] height (metrics.height)
        // All three values come from TreeMetrics — no external set like Grid's frontier.

        @Test
        fun `metricValues size equals BSTInorder metricLabels size`() {
            val plugin = BSTInorder()
            val step = treeStep(TreeMetrics(visited = 1L, totalNodes = 7L, height = 3L))
            assertEquals(plugin.metricLabels.size, step.metricValues.size)
        }

        @Test
        fun `metricValues size equals BSTPreorder metricLabels size`() {
            val plugin = BSTPreorder()
            val step = treeStep(TreeMetrics(visited = 1L, totalNodes = 7L, height = 3L))
            assertEquals(plugin.metricLabels.size, step.metricValues.size)
        }

        @Test
        fun `metricValues size equals BSTPostorder metricLabels size`() {
            val plugin = BSTPostorder()
            val step = treeStep(TreeMetrics(visited = 1L, totalNodes = 7L, height = 3L))
            assertEquals(plugin.metricLabels.size, step.metricValues.size)
        }

        @Test
        fun `metricValues are exactly three elements`() {
            val step = treeStep(TreeMetrics(1L, 7L, 3L))
            assertEquals(3, step.metricValues.size)
        }

        @Test
        fun `metricValues index 0 is visited count`() {
            // metricLabels[0] = "Visited" for all tree plugins
            val step = treeStep(TreeMetrics(visited = 4L, totalNodes = 7L, height = 3L))
            assertEquals(4L, step.metricValues[0],
                "metricValues[0] must be metrics.visited (Visited)")
        }

        @Test
        fun `metricValues index 1 is totalNodes`() {
            // metricLabels[1] = "Total" for all tree plugins
            val step = treeStep(TreeMetrics(visited = 4L, totalNodes = 7L, height = 3L))
            assertEquals(7L, step.metricValues[1],
                "metricValues[1] must be metrics.totalNodes (Total)")
        }

        @Test
        fun `metricValues index 2 is height`() {
            // metricLabels[2] = "Height" for all tree plugins
            val step = treeStep(TreeMetrics(visited = 4L, totalNodes = 7L, height = 3L))
            assertEquals(3L, step.metricValues[2],
                "metricValues[2] must be metrics.height (Height)")
        }

        @Test
        fun `metricValues totalNodes and height are constant across all BSTInorder steps`() {
            // totalNodes and height are structural facts about the BST — they must not
            // change across steps. Only visited grows.
            val plugin = BSTInorder()
            val steps = plugin.steps(plugin.initialData()).toList()
                .filterIsInstance<VisualizationStep.Tree>()
            assertTrue(steps.isNotEmpty())

            val expectedTotal = steps.first().metricValues[1]
            val expectedHeight = steps.first().metricValues[2]

            steps.forEach { step ->
                assertEquals(expectedTotal, step.metricValues[1],
                    "totalNodes must be constant across all steps")
                assertEquals(expectedHeight, step.metricValues[2],
                    "height must be constant across all steps")
            }
        }

        @Test
        fun `metricValues visited grows monotonically across BSTInorder steps`() {
            val plugin = BSTInorder()
            // Exclude the final summary step (same visited count as step before it)
            val steps = plugin.steps(plugin.initialData()).toList()
                .filterIsInstance<VisualizationStep.Tree>()
                .dropLast(1)

            var lastVisited = -1L
            steps.forEach { step ->
                assertTrue(step.metricValues[0] > lastVisited,
                    "metricValues[0] (visited) must increase strictly for each inorder step")
                lastVisited = step.metricValues[0]
            }
        }

        @Test
        fun `metricValues reflect different visited counts in two steps`() {
            val stepEarly = treeStep(TreeMetrics(visited = 1L, totalNodes = 7L, height = 3L))
            val stepLater = treeStep(TreeMetrics(visited = 5L, totalNodes = 7L, height = 3L))

            assertTrue(stepLater.metricValues[0] > stepEarly.metricValues[0],
                "metricValues[0] must reflect the visited count of each individual step")
        }

        @Test
        fun `BSTInorder live steps metricValues match metrics fields`() {
            val plugin = BSTInorder()
            plugin.steps(plugin.initialData()).forEach { step ->
                check(step is VisualizationStep.Tree)
                assertEquals(step.metrics.visited, step.metricValues[0],
                    "Live Tree step: metricValues[0] must equal metrics.visited")
                assertEquals(step.metrics.totalNodes, step.metricValues[1],
                    "Live Tree step: metricValues[1] must equal metrics.totalNodes")
                assertEquals(step.metrics.height, step.metricValues[2],
                    "Live Tree step: metricValues[2] must equal metrics.height")
            }
        }

        @Test
        fun `BSTPreorder live steps metricValues match metrics fields`() {
            val plugin = BSTPreorder()
            plugin.steps(plugin.initialData()).forEach { step ->
                check(step is VisualizationStep.Tree)
                assertEquals(step.metrics.visited, step.metricValues[0])
                assertEquals(step.metrics.totalNodes, step.metricValues[1])
                assertEquals(step.metrics.height, step.metricValues[2])
            }
        }

        private fun treeStep(metrics: TreeMetrics) = VisualizationStep.Tree(
            nodeStates = emptyMap(),
            traversalSequence = emptyList(),
            metrics = metrics,
        )
    }
}
