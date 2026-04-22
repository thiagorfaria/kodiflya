package com.kodiflya.algorithms.sorting

import com.kodiflya.core.plugin.AlgorithmInput
import com.kodiflya.core.plugin.AlgorithmPlugin
import com.kodiflya.core.plugin.Category
import com.kodiflya.core.plugin.ColorRole
import com.kodiflya.core.plugin.Complexity
import com.kodiflya.core.plugin.MetricLabel
import com.kodiflya.core.plugin.SortMetrics
import com.kodiflya.core.plugin.VizStep

class QuickSort : AlgorithmPlugin {

    override val id = "quick_sort"
    override val displayName = "Quick Sort"
    override val category = Category.SORTING
    override val order = 3
    override val complexity = Complexity(
        bestCase = "O(n log n)",
        averageCase = "O(n log n)",
        worstCase = "O(n²)",
        spaceComplexity = "O(log n)",
    )
    override val metricLabels = listOf(
        MetricLabel("Comparisons", ColorRole.PEACH),
        MetricLabel("Swaps", ColorRole.GREEN),
        MetricLabel("Array reads", ColorRole.AMBER),
    )

    override fun initialData(): AlgorithmInput {
        val values = (1..10).toMutableList().also { it.shuffle() }.toIntArray()
        return AlgorithmInput.SortInput(values)
    }

    override fun steps(input: AlgorithmInput): Sequence<VizStep> {
        val arr = (input as AlgorithmInput.SortInput).values.copyOf()

        return sequence {
            var comparisons = 0L
            var swaps = 0L
            val sorted = mutableSetOf<Int>()

            suspend fun SequenceScope<VizStep>.partition(low: Int, high: Int): Int {
                val pivotVal = arr[high]
                var i = low - 1
                for (j in low until high) {
                    comparisons++
                    yield(
                        VizStep.Sort(
                            values = arr.copyOf(),
                            comparing = setOf(j, high),
                            swapping = emptySet(),
                            sorted = sorted.toSet(),
                            pivot = high,
                            metrics = SortMetrics(comparisons, swaps, comparisons + swaps),
                        )
                    )
                    if (arr[j] <= pivotVal) {
                        i++
                        val tmp = arr[i]; arr[i] = arr[j]; arr[j] = tmp
                        swaps++
                        yield(
                            VizStep.Sort(
                                values = arr.copyOf(),
                                comparing = emptySet(),
                                swapping = setOf(i, j),
                                sorted = sorted.toSet(),
                                pivot = high,
                                metrics = SortMetrics(comparisons, swaps, comparisons + swaps),
                            )
                        )
                    }
                }
                val tmp = arr[i + 1]; arr[i + 1] = arr[high]; arr[high] = tmp
                swaps++
                return i + 1
            }

            suspend fun SequenceScope<VizStep>.quickSort(low: Int, high: Int) {
                if (low >= high) {
                    if (low == high) sorted.add(low)
                    return
                }
                val pi = partition(low, high)
                sorted.add(pi)
                quickSort(low, pi - 1)
                quickSort(pi + 1, high)
            }

            quickSort(0, arr.size - 1)

            sorted.addAll(0 until arr.size)
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
