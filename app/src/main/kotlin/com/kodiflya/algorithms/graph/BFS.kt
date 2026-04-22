package com.kodiflya.algorithms.graph

import com.kodiflya.core.plugin.AlgorithmInput
import com.kodiflya.core.plugin.AlgorithmPlugin
import com.kodiflya.core.plugin.Category
import com.kodiflya.core.plugin.ColorRole
import com.kodiflya.core.plugin.Complexity
import com.kodiflya.core.plugin.GridMetrics
import com.kodiflya.core.plugin.MetricLabel
import com.kodiflya.core.plugin.VizStep

class BFS : AlgorithmPlugin {

    override val id = "bfs"
    override val displayName = "BFS"
    override val category = Category.GRAPH
    override val order = 0
    override val complexity = Complexity(
        bestCase = "O(V+E)",
        averageCase = "O(V+E)",
        worstCase = "O(V+E)",
        spaceComplexity = "O(V)",
    )
    override val metricLabels = listOf(
        MetricLabel("Visited", ColorRole.PURPLE),
        MetricLabel("Frontier", ColorRole.PEACH),
        MetricLabel("Path length", ColorRole.GREEN),
    )

    override fun initialData() = defaultGridInput()

    override fun steps(input: AlgorithmInput): Sequence<VizStep> {
        val grid = input as AlgorithmInput.GridInput

        return sequence {
            val queue = ArrayDeque<Pair<Int, Int>>()
            val parent = mutableMapOf<Pair<Int, Int>, Pair<Int, Int>?>()
            val processed = mutableSetOf<Pair<Int, Int>>()
            val inQueue = mutableSetOf(grid.start)
            var found = false

            queue.add(grid.start)
            parent[grid.start] = null

            while (queue.isNotEmpty() && !found) {
                val current = queue.removeFirst()
                processed.add(current)
                inQueue.remove(current)

                if (current == grid.end) { found = true; break }

                for (neighbor in neighbors(current, grid.width, grid.height, grid.walls)) {
                    if (neighbor !in processed && neighbor !in inQueue) {
                        inQueue.add(neighbor)
                        parent[neighbor] = current
                        queue.add(neighbor)
                        if (neighbor == grid.end) { found = true }
                    }
                }

                yield(
                    VizStep.Grid(
                        cells = buildCells(grid.width, grid.height, grid.walls, grid.start, grid.end, processed, inQueue),
                        frontier = inQueue.toSet(),
                        path = emptyList(),
                        metrics = GridMetrics(processed.size.toLong(), 0L),
                    )
                )
            }

            val path = if (found) tracePath(grid.end, parent) else emptyList()
            yield(
                VizStep.Grid(
                    cells = buildCells(grid.width, grid.height, grid.walls, grid.start, grid.end, processed, emptySet(), path.toSet()),
                    frontier = emptySet(),
                    path = path,
                    metrics = GridMetrics(processed.size.toLong(), (path.size - 1).coerceAtLeast(0).toLong()),
                )
            )
        }
    }
}
