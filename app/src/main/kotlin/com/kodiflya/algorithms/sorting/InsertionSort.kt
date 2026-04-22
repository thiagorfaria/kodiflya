package com.kodiflya.algorithms.sorting

import com.kodiflya.core.plugin.AlgorithmInput
import com.kodiflya.core.plugin.AlgorithmPlugin
import com.kodiflya.core.plugin.Category
import com.kodiflya.core.plugin.ColorRole
import com.kodiflya.core.plugin.Complexity
import com.kodiflya.core.plugin.MetricLabel
import com.kodiflya.core.plugin.SortMetrics
import com.kodiflya.core.plugin.VisualizationStep

class InsertionSort : AlgorithmPlugin {

    override val id = "insertion_sort"
    override val displayName = "Insertion Sort"
    override val category = Category.SORTING
    override val order = 1
    override val complexity = Complexity(
        bestCase = "O(n)",
        averageCase = "O(n²)",
        worstCase = "O(n²)",
        spaceComplexity = "O(1)",
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

    override fun steps(input: AlgorithmInput): Sequence<VisualizationStep> {
        val arr = (input as AlgorithmInput.SortInput).values.copyOf()
        val n = arr.size

        return sequence {
            var comparisons = 0L
            var swaps = 0L
            val sorted = mutableSetOf(0)

            for (i in 1 until n) {
                var j = i
                while (j > 0) {
                    comparisons++
                    yield(
                        VisualizationStep.Sort(
                            values = arr.copyOf(),
                            comparing = setOf(j - 1, j),
                            swapping = emptySet(),
                            sorted = sorted.toSet(),
                            pivot = i,
                            metrics = SortMetrics(comparisons, swaps, comparisons + swaps),
                        )
                    )
                    if (arr[j - 1] > arr[j]) {
                        val tmp = arr[j - 1]
                        arr[j - 1] = arr[j]
                        arr[j] = tmp
                        swaps++
                        yield(
                            VisualizationStep.Sort(
                                values = arr.copyOf(),
                                comparing = emptySet(),
                                swapping = setOf(j - 1, j),
                                sorted = sorted.toSet(),
                                pivot = i,
                                metrics = SortMetrics(comparisons, swaps, comparisons + swaps),
                            )
                        )
                        j--
                    } else {
                        break
                    }
                }
                sorted.add(i)
            }

            sorted.addAll(0 until n)
            yield(
                VisualizationStep.Sort(
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
