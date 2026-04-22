package com.kodiflya.core.plugin

// Each VisualizationStep must carry enough data for a Composable to draw a complete frame
// without replaying prior steps (stateless rendering contract).

sealed class VisualizationStep {

    // Sorting: full array snapshot + element state sets per step
    data class Sort(
        val values: IntArray,
        val comparing: Set<Int>,
        val swapping: Set<Int>,
        val sorted: Set<Int>,
        val pivot: Int?,
        val metrics: SortMetrics,
    ) : VisualizationStep() {
        override fun equals(other: Any?) =
            other is Sort && values.contentEquals(other.values) &&
                comparing == other.comparing && swapping == other.swapping &&
                sorted == other.sorted && pivot == other.pivot && metrics == other.metrics
        override fun hashCode(): Int {
            var result = values.contentHashCode()
            result = 31 * result + comparing.hashCode()
            result = 31 * result + swapping.hashCode()
            result = 31 * result + sorted.hashCode()
            result = 31 * result + (pivot ?: 0)
            result = 31 * result + metrics.hashCode()
            return result
        }
    }

    // Grid pathfinding: full 9×9 cell state map per step (not delta)
    data class Grid(
        val cells: Map<Pair<Int, Int>, CellState>,
        val frontier: Set<Pair<Int, Int>>,
        val path: List<Pair<Int, Int>>,
        val metrics: GridMetrics,
    ) : VisualizationStep()

    // Tree traversal: full node state map + complete traversal sequence per step (not delta)
    data class Tree(
        val nodeStates: Map<Int, NodeState>,
        val traversalSequence: List<Int>,
        val metrics: TreeMetrics,
    ) : VisualizationStep()
}

enum class CellState { OPEN, WALL, VISITED, FRONTIER, PATH, START, END }

enum class NodeState { DEFAULT, ACTIVE, VISITED }

data class SortMetrics(val comparisons: Long, val swaps: Long, val arrayReads: Long)

data class GridMetrics(val visited: Long, val pathLength: Long)

data class TreeMetrics(val visited: Long, val totalNodes: Long, val height: Long)
