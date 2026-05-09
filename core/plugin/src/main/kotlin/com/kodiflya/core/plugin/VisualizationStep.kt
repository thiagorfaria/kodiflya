package com.kodiflya.core.plugin

// Each VisualizationStep must carry enough data for a Composable to draw a complete frame
// without replaying prior steps (stateless rendering contract).

sealed class VisualizationStep {

    // Ordered metric values matching the plugin's metricLabels list.
    // Screens zip this with plugin.metricLabels — no hardcoded labels or color slots in UI.
    abstract val metricValues: List<Long>

    // Sorting: full array snapshot + element state sets per step
    data class Sort(
        val values: IntArray,
        val comparing: Set<Int>,
        val swapping: Set<Int>,
        val sorted: Set<Int>,
        val pivot: Int?,
        val metrics: SortMetrics,
    ) : VisualizationStep() {
        // metricValues order matches every sorting plugin's metricLabels:
        // [0] comparisons, [1] swaps, [2] arrayReads
        override val metricValues: List<Long>
            get() = listOf(metrics.comparisons, metrics.swaps, metrics.arrayReads)

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
    ) : VisualizationStep() {
        // metricValues order matches every graph plugin's metricLabels:
        // [0] visited, [1] frontier size (from frontier set — not duplicated in GridMetrics),
        // [2] pathLength
        override val metricValues: List<Long>
            get() = listOf(metrics.visited, frontier.size.toLong(), metrics.pathLength)
    }

    // Tree traversal: full node state map + complete traversal sequence per step (not delta)
    data class Tree(
        val nodeStates: Map<Int, NodeState>,
        val traversalSequence: List<Int>,
        val metrics: TreeMetrics,
    ) : VisualizationStep() {
        // metricValues order matches every tree plugin's metricLabels:
        // [0] visited, [1] totalNodes, [2] height
        override val metricValues: List<Long>
            get() = listOf(metrics.visited, metrics.totalNodes, metrics.height)
    }

    // Search: full array snapshot + pointer positions per step (stateless rendering contract)
    data class Search(
        val values: IntArray,
        val target: Int,
        val current: Int?,
        val left: Int?,
        val right: Int?,
        val eliminated: Set<Int>,
        val found: Int?,
        val metrics: SearchMetrics,
    ) : VisualizationStep() {
        // metricValues order matches every search plugin's metricLabels:
        // [0] comparisons, [1] eliminated, [2] remaining
        override val metricValues: List<Long>
            get() = listOf(metrics.comparisons, metrics.eliminated, metrics.remaining)

        override fun equals(other: Any?) =
            other is Search && values.contentEquals(other.values) && target == other.target &&
                current == other.current && left == other.left && right == other.right &&
                eliminated == other.eliminated && found == other.found && metrics == other.metrics
        override fun hashCode(): Int {
            var result = values.contentHashCode()
            result = 31 * result + target
            result = 31 * result + (current ?: -1)
            result = 31 * result + (left ?: -1)
            result = 31 * result + (right ?: -1)
            result = 31 * result + eliminated.hashCode()
            result = 31 * result + (found ?: -1)
            result = 31 * result + metrics.hashCode()
            return result
        }
    }
}

enum class CellState { OPEN, WALL, VISITED, FRONTIER, PATH, START, END }

enum class NodeState { DEFAULT, ACTIVE, VISITED }

data class SortMetrics(val comparisons: Long, val swaps: Long, val arrayReads: Long)

data class GridMetrics(val visited: Long, val pathLength: Long)

data class TreeMetrics(val visited: Long, val totalNodes: Long, val height: Long)
