package com.kodiflya.algorithms.search

import com.kodiflya.core.plugin.AlgorithmInput
import com.kodiflya.core.plugin.AlgorithmPlugin
import com.kodiflya.core.plugin.BigO
import com.kodiflya.core.plugin.Category
import com.kodiflya.core.plugin.ColorRole
import com.kodiflya.core.plugin.Complexity
import com.kodiflya.core.plugin.MetricLabel
import com.kodiflya.core.plugin.SearchMetrics
import com.kodiflya.core.plugin.VisualizationStep

// One step = one mid-point comparison. Left/right carry the current search range so
// the canvas can shade eliminated halves without replaying prior steps.
class BinarySearch : AlgorithmPlugin {

    override val id = "binary_search"
    override val displayName = "Binary Search"
    override val category = Category.SEARCH
    override val order = 0
    override val complexity = Complexity(
        bestCase = BigO.O_1,
        averageCase = BigO.O_LOG_N,
        worstCase = BigO.O_LOG_N,
        spaceComplexity = BigO.O_1,
    )
    override val metricLabels = listOf(
        MetricLabel("Comparisons", ColorRole.PEACH),
        MetricLabel("Eliminated", ColorRole.GREEN),
        MetricLabel("Remaining", ColorRole.AMBER),
    )

    override fun initialData(): AlgorithmInput {
        val values = (1..100).toList().toIntArray()
        val target = values[values.indices.random()]
        return AlgorithmInput.SearchInput(values, target)
    }

    override fun steps(input: AlgorithmInput): Sequence<VisualizationStep> {
        val s = input as AlgorithmInput.SearchInput
        val arr = s.values
        val target = s.target
        val n = arr.size

        return sequence {
            var left = 0
            var right = n - 1
            var comparisons = 0L

            while (left <= right) {
                val mid = (left + right) / 2
                comparisons++
                val eliminated = (n - (right - left + 1)).toLong()
                val remaining = (right - left + 1).toLong()

                yield(
                    VisualizationStep.Search(
                        values = arr,
                        target = target,
                        current = mid,
                        left = left,
                        right = right,
                        eliminated = emptySet(),
                        found = null,
                        metrics = SearchMetrics(comparisons, eliminated, remaining),
                    )
                )

                when {
                    arr[mid] == target -> {
                        yield(
                            VisualizationStep.Search(
                                values = arr,
                                target = target,
                                current = null,
                                left = null,
                                right = null,
                                eliminated = emptySet(),
                                found = mid,
                                metrics = SearchMetrics(comparisons, (n - 1).toLong(), 1L),
                            )
                        )
                        return@sequence
                    }
                    arr[mid] < target -> left = mid + 1
                    else -> right = mid - 1
                }
            }

            yield(
                VisualizationStep.Search(
                    values = arr,
                    target = target,
                    current = null,
                    left = null,
                    right = null,
                    eliminated = emptySet(),
                    found = null,
                    metrics = SearchMetrics(comparisons, n.toLong(), 0L),
                )
            )
        }
    }
}
