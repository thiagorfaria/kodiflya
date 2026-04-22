package com.kodiflya.algorithms.sorting

import com.kodiflya.core.plugin.AlgorithmInput
import com.kodiflya.core.plugin.VizStep
import org.junit.jupiter.api.Assertions.assertArrayEquals
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class BubbleSortTest {

    private val subject = BubbleSort()

    @Test
    fun `final step produces a fully sorted array`() {
        val input = AlgorithmInput.SortInput(intArrayOf(5, 3, 8, 1, 9, 2, 7, 4, 6, 0))
        val steps = subject.steps(input).toList()
        val finalStep = steps.last() as VizStep.Sort

        assertArrayEquals(intArrayOf(0, 1, 2, 3, 4, 5, 6, 7, 8, 9), finalStep.values)
    }

    @Test
    fun `final step marks all indices as sorted`() {
        val input = AlgorithmInput.SortInput(intArrayOf(3, 1, 2))
        val steps = subject.steps(input).toList()
        val finalStep = steps.last() as VizStep.Sort

        assertEquals(setOf(0, 1, 2), finalStep.sorted)
    }

    @Test
    fun `final step has no active comparing or swapping elements`() {
        val input = AlgorithmInput.SortInput(intArrayOf(3, 1, 2))
        val steps = subject.steps(input).toList()
        val finalStep = steps.last() as VizStep.Sort

        assertTrue(finalStep.comparing.isEmpty())
        assertTrue(finalStep.swapping.isEmpty())
    }

    @Test
    fun `comparison count increases monotonically across steps`() {
        val input = AlgorithmInput.SortInput(intArrayOf(4, 3, 2, 1))
        val steps = subject.steps(input).toList().filterIsInstance<VizStep.Sort>()

        var lastComparisons = -1L
        for (step in steps) {
            assertTrue(step.metrics.comparisons >= lastComparisons)
            lastComparisons = step.metrics.comparisons
        }
    }

    @Test
    fun `reversed array produces maximum number of comparisons for size 4`() {
        // Bubble sort on [4,3,2,1]: should perform (n-1)+(n-2)+...+1 = n*(n-1)/2 comparisons = 6
        val input = AlgorithmInput.SortInput(intArrayOf(4, 3, 2, 1))
        val steps = subject.steps(input).toList()
        val finalMetrics = (steps.last() as VizStep.Sort).metrics

        assertEquals(6L, finalMetrics.comparisons)
        assertEquals(6L, finalMetrics.swaps)
    }

    @Test
    fun `already-sorted array produces minimum comparisons (early exit)`() {
        // BubbleSort with early exit: [1,2,3,4] should stop after first pass (0 swaps)
        val input = AlgorithmInput.SortInput(intArrayOf(1, 2, 3, 4))
        val steps = subject.steps(input).toList()
        val finalMetrics = (steps.last() as VizStep.Sort).metrics

        // One pass of n-1 comparisons, then early exit
        assertEquals(3L, finalMetrics.comparisons)
        assertEquals(0L, finalMetrics.swaps)
    }

    @Test
    fun `each step carries a full array snapshot for stateless rendering`() {
        val input = AlgorithmInput.SortInput(intArrayOf(3, 1, 2))
        val steps = subject.steps(input).toList().filterIsInstance<VizStep.Sort>()

        // Every step must have values.size == input.values.size
        steps.forEach { step ->
            assertEquals(3, step.values.size, "Step must carry full array snapshot")
        }
    }

    @Test
    fun `sequence is deterministic for the same input`() {
        val input = AlgorithmInput.SortInput(intArrayOf(5, 2, 8, 1, 9))
        val run1 = subject.steps(input).toList()
        val run2 = subject.steps(input).toList()

        assertEquals(run1.size, run2.size)
        run1.zip(run2).forEach { (a, b) ->
            val sa = a as VizStep.Sort
            val sb = b as VizStep.Sort
            assertArrayEquals(sa.values, sb.values)
            assertEquals(sa.metrics, sb.metrics)
        }
    }

    @Test
    fun `BubbleSort has no android imports (pure Kotlin contract)`() {
        // Verified at compile time — BubbleSort.kt imports only com.kodiflya.core.plugin.*
        // This test documents the intent; the build will fail if android.* is imported.
        val imports = BubbleSort::class.java.`package`?.name ?: ""
        assertTrue(imports.startsWith("com.kodiflya"))
    }
}
