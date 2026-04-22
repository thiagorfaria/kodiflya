package com.kodiflya.core.plugin

interface AlgorithmPlugin {
    val id: String
    val displayName: String
    val category: Category
    val order: Int
    val complexity: Complexity
    val metricLabels: List<MetricLabel>

    fun initialData(): AlgorithmInput
    fun steps(input: AlgorithmInput): Sequence<VisualizationStep>
}

enum class Category { SORTING, GRAPH, TREES }

enum class ColorRole { NEUTRAL, GREEN, PEACH, AMBER, PURPLE }

data class MetricLabel(val label: String, val colorRole: ColorRole)

data class Complexity(
    val bestCase: String,
    val averageCase: String,
    val worstCase: String,
    val spaceComplexity: String,
)

sealed class AlgorithmInput {
    data class SortInput(val values: IntArray) : AlgorithmInput() {
        override fun equals(other: Any?) =
            other is SortInput && values.contentEquals(other.values)
        override fun hashCode() = values.contentHashCode()
    }

    data class GridInput(
        val width: Int,
        val height: Int,
        val walls: Set<Pair<Int, Int>>,
        val start: Pair<Int, Int>,
        val end: Pair<Int, Int>,
    ) : AlgorithmInput()

    data class TreeInput(val values: List<Int>) : AlgorithmInput()
}
