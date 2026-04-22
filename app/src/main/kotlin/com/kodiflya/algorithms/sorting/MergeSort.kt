package com.kodiflya.algorithms.sorting

import com.kodiflya.core.plugin.AlgorithmInput
import com.kodiflya.core.plugin.AlgorithmPlugin
import com.kodiflya.core.plugin.Category
import com.kodiflya.core.plugin.ColorRole
import com.kodiflya.core.plugin.Complexity
import com.kodiflya.core.plugin.MetricLabel
import com.kodiflya.core.plugin.SortMetrics
import com.kodiflya.core.plugin.VizStep

class MergeSort : AlgorithmPlugin {

    override val id = "merge_sort"
    override val displayName = "Merge Sort"
    override val category = Category.SORTING
    override val order = 2
    override val complexity = Complexity(
        bestCase = "O(n log n)",
        averageCase = "O(n log n)",
        worstCase = "O(n log n)",
        spaceComplexity = "O(n)",
    )
    override val metricLabels = listOf(
        MetricLabel("Comparisons", ColorRole.PEACH),
        MetricLabel("Array writes", ColorRole.GREEN),
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
            var writes = 0L
            val sorted = mutableSetOf<Int>()

            suspend fun SequenceScope<VizStep>.merge(left: Int, mid: Int, right: Int) {
                val leftArr = arr.copyOfRange(left, mid + 1)
                val rightArr = arr.copyOfRange(mid + 1, right + 1)
                var i = 0; var j = 0; var k = left

                while (i < leftArr.size && j < rightArr.size) {
                    comparisons++
                    yield(
                        VizStep.Sort(
                            values = arr.copyOf(),
                            comparing = setOf(left + i, mid + 1 + j),
                            swapping = emptySet(),
                            sorted = sorted.toSet(),
                            pivot = null,
                            metrics = SortMetrics(comparisons, writes, comparisons + writes),
                        )
                    )
                    if (leftArr[i] <= rightArr[j]) {
                        arr[k++] = leftArr[i++]
                    } else {
                        arr[k++] = rightArr[j++]
                    }
                    writes++
                    yield(
                        VizStep.Sort(
                            values = arr.copyOf(),
                            comparing = emptySet(),
                            swapping = setOf(k - 1),
                            sorted = sorted.toSet(),
                            pivot = null,
                            metrics = SortMetrics(comparisons, writes, comparisons + writes),
                        )
                    )
                }
                while (i < leftArr.size) { arr[k++] = leftArr[i++]; writes++ }
                while (j < rightArr.size) { arr[k++] = rightArr[j++]; writes++ }
            }

            suspend fun SequenceScope<VizStep>.mergeSort(left: Int, right: Int) {
                if (left >= right) {
                    sorted.add(left)
                    return
                }
                val mid = (left + right) / 2
                mergeSort(left, mid)
                mergeSort(mid + 1, right)
                merge(left, mid, right)
                for (idx in left..right) sorted.add(idx)
            }

            mergeSort(0, arr.size - 1)

            sorted.addAll(0 until arr.size)
            yield(
                VizStep.Sort(
                    values = arr.copyOf(),
                    comparing = emptySet(),
                    swapping = emptySet(),
                    sorted = sorted.toSet(),
                    pivot = null,
                    metrics = SortMetrics(comparisons, writes, comparisons + writes),
                )
            )
        }
    }
}
