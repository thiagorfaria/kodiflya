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

// One step = one element comparison. The eliminated set grows by one each step,
// making the contrast with BinarySearch's logarithmic elimination visible in the metrics.
class LinearSearch : AlgorithmPlugin {

    override val id = "linear_search"
    override val displayName = "Linear Search"
    override val category = Category.SEARCH
    override val order = 1
    override val complexity = Complexity(
        bestCase = BigO.O_1,
        averageCase = BigO.O_N,
        worstCase = BigO.O_N,
        spaceComplexity = BigO.O_1,
    )
    override val metricLabels = listOf(
        MetricLabel("Comparisons", ColorRole.PEACH),
        MetricLabel("Eliminated", ColorRole.GREEN),
        MetricLabel("Remaining", ColorRole.AMBER),
    )

    override fun initialData(): AlgorithmInput {
        val values = (1..100).toMutableList().also { it.shuffle() }.toIntArray()
        val target = values[values.indices.random()]
        return AlgorithmInput.SearchInput(values, target)
    }

    override fun steps(input: AlgorithmInput): Sequence<VisualizationStep> {
        val s = input as AlgorithmInput.SearchInput
        val arr = s.values
        val target = s.target
        val n = arr.size
        val eliminated = mutableSetOf<Int>()

        return sequence {
            var comparisons = 0L

            for (i in 0 until n) {
                comparisons++

                yield(
                    VisualizationStep.Search(
                        values = arr,
                        target = target,
                        current = i,
                        left = null,
                        right = null,
                        eliminated = eliminated.toSet(),
                        found = null,
                        metrics = SearchMetrics(comparisons, eliminated.size.toLong(), (n - i).toLong()),
                    )
                )

                if (arr[i] == target) {
                    yield(
                        VisualizationStep.Search(
                            values = arr,
                            target = target,
                            current = null,
                            left = null,
                            right = null,
                            eliminated = eliminated.toSet(),
                            found = i,
                            metrics = SearchMetrics(comparisons, eliminated.size.toLong(), (n - i).toLong()),
                        )
                    )
                    return@sequence
                }

                eliminated.add(i)
            }

            yield(
                VisualizationStep.Search(
                    values = arr,
                    target = target,
                    current = null,
                    left = null,
                    right = null,
                    eliminated = eliminated.toSet(),
                    found = null,
                    metrics = SearchMetrics(comparisons, n.toLong(), 0L),
                )
            )
        }
    }
}
