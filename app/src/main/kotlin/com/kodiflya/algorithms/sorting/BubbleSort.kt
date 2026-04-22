package com.kodiflya.algorithms.sorting

import com.kodiflya.core.plugin.AlgorithmInput
import com.kodiflya.core.plugin.AlgorithmPlugin
import com.kodiflya.core.plugin.Category
import com.kodiflya.core.plugin.ColorRole
import com.kodiflya.core.plugin.Complexity
import com.kodiflya.core.plugin.MetricLabel
import com.kodiflya.core.plugin.SortMetrics
import com.kodiflya.core.plugin.VizStep

// One step = one comparison (the atomic decision a human would narrate aloud).
// The full array snapshot is carried in every step for stateless rendering.
class BubbleSort : AlgorithmPlugin {

    override val id = "bubble_sort"
    override val displayName = "Bubble Sort"
    override val category = Category.SORTING
    override val order = 0
    override val complexity = Complexity(
        bestCase = "O(n)",
        averageCase = "O(n²)",
        worstCase = "O(n²)",
        spaceComplexity = "O(1)",
    )
    override val metricLabels = listOf(
        MetricLabel("Comparisons", ColorRole.PEACH),
        MetricLabel("Swaps", ColorRole.GREEN),
        MetricLabel("Passes", ColorRole.AMBER),
    )

    override fun initialData(): AlgorithmInput {
        val values = (1..10).toMutableList().also { it.shuffle() }.toIntArray()
        return AlgorithmInput.SortInput(values)
    }

    override fun steps(input: AlgorithmInput): Sequence<VizStep> {
        val arr = (input as AlgorithmInput.SortInput).values.copyOf()
        val n = arr.size

        return sequence {
            var comparisons = 0L
            var swaps = 0L
            val sorted = mutableSetOf<Int>()

            for (pass in 0 until n - 1) {
                var swappedThisPass = false

                for (j in 0 until n - 1 - pass) {
                    comparisons++

                    // Comparison step — peach on both elements being compared
                    yield(
                        VizStep.Sort(
                            values = arr.copyOf(),
                            comparing = setOf(j, j + 1),
                            swapping = emptySet(),
                            sorted = sorted.toSet(),
                            pivot = null,
                            metrics = SortMetrics(comparisons, swaps, comparisons + swaps),
                        )
                    )

                    if (arr[j] > arr[j + 1]) {
                        val tmp = arr[j]
                        arr[j] = arr[j + 1]
                        arr[j + 1] = tmp
                        swaps++
                        swappedThisPass = true

                        // Swap step — emit after swap so values reflect the swapped state
                        yield(
                            VizStep.Sort(
                                values = arr.copyOf(),
                                comparing = emptySet(),
                                swapping = setOf(j, j + 1),
                                sorted = sorted.toSet(),
                                pivot = null,
                                metrics = SortMetrics(comparisons, swaps, comparisons + swaps),
                            )
                        )
                    }
                }

                // The element that bubbled to the end of this pass is now sorted
                sorted.add(n - 1 - pass)

                // If no swaps happened, the array is already sorted
                if (!swappedThisPass) {
                    for (k in 0 until n - 1 - pass) sorted.add(k)
                    break
                }
            }

            // Final frame: everything sorted, no active elements
            sorted.addAll(0 until n)
            yield(
                VizStep.Sort(
                    values = arr.copyOf(),
                    comparing = emptySet(),
                    swapping = emptySet(),
                    sorted = sorted.toSet(),
                    pivot = null,
                    metrics = SortMetrics(comparisons, swaps, comparisons + swaps),
                )
            )
        }
    }
}
