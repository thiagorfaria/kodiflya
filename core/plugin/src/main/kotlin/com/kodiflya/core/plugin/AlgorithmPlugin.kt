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

enum class ColorRole { NEUTRAL, GREEN, BLUE, MOSS, AMBER, PEACH, DUSTY_ROSE, PURPLE }

data class MetricLabel(val label: String, val colorRole: ColorRole)

enum class BigO(val label: String, val colorRole: ColorRole) {
    O_1(          "O(1)",           ColorRole.GREEN),
    O_LOG_N(      "O(log n)",       ColorRole.BLUE),
    O_H(          "O(h)",           ColorRole.PURPLE),
    O_N(          "O(n)",           ColorRole.MOSS),
    O_V(          "O(V)",           ColorRole.MOSS),
    O_N_LOG_N(    "O(n log n)",     ColorRole.AMBER),
    O_V_PLUS_E(   "O(V+E)",         ColorRole.MOSS),
    O_N_SQUARED(  "O(n²)",          ColorRole.PEACH),
    O_V_PLUS_E_LOG_V("O((V+E)log V)", ColorRole.AMBER),
    O_2_N(        "O(2ⁿ)",          ColorRole.DUSTY_ROSE),
    O_N_FACTORIAL("O(n!)",          ColorRole.PURPLE),
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
