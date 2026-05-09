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

enum class Category(val route: String) {
    SORTING("sort"),
    GRAPH("graph"),
    TREES("tree"),
    SEARCH("search"),
}

enum class ColorRole { NEUTRAL, GREEN, PEACH, AMBER, PURPLE }

data class MetricLabel(val label: String, val colorRole: ColorRole)

enum class BigO(val label: String) {
    O_1("O(1)"),
    O_LOG_N("O(log n)"),
    O_H("O(h)"),
    O_N("O(n)"),
    O_V("O(V)"),
    O_N_LOG_N("O(n log n)"),
    O_V_PLUS_E("O(V+E)"),
    O_N_SQUARED("O(n²)"),
    O_V_PLUS_E_LOG_V("O((V+E)log V)"),
}

data class Complexity(
    val bestCase: BigO,
    val averageCase: BigO,
    val worstCase: BigO,
    val spaceComplexity: BigO,
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

    data class SearchInput(val values: IntArray, val target: Int) : AlgorithmInput() {
        override fun equals(other: Any?) =
            other is SearchInput && values.contentEquals(other.values) && target == other.target
        override fun hashCode(): Int = 31 * values.contentHashCode() + target
    }
}

data class SearchMetrics(val comparisons: Long, val eliminated: Long, val remaining: Long)
