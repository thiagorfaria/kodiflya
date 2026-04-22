package com.kodiflya.algorithms.graph

import com.kodiflya.core.plugin.AlgorithmInput
import com.kodiflya.core.plugin.AlgorithmPlugin
import com.kodiflya.core.plugin.Category
import com.kodiflya.core.plugin.ColorRole
import com.kodiflya.core.plugin.Complexity
import com.kodiflya.core.plugin.GridMetrics
import com.kodiflya.core.plugin.MetricLabel
import com.kodiflya.core.plugin.VisualizationStep

class DFS : AlgorithmPlugin {

    override val id = "dfs"
    override val displayName = "DFS"
    override val category = Category.GRAPH
    override val order = 1
    override val complexity = Complexity(
        bestCase = "O(V+E)",
        averageCase = "O(V+E)",
        worstCase = "O(V+E)",
        spaceComplexity = "O(V)",
    )
    override val metricLabels = listOf(
        MetricLabel("Visited", ColorRole.PURPLE),
        MetricLabel("Stack depth", ColorRole.PEACH),
        MetricLabel("Path length", ColorRole.GREEN),
    )

    override fun initialData() = defaultGridInput()

    override fun steps(input: AlgorithmInput): Sequence<VisualizationStep> {
        val grid = input as AlgorithmInput.GridInput

        return sequence {
            val stack = ArrayDeque<Pair<Int, Int>>()
            val parent = mutableMapOf<Pair<Int, Int>, Pair<Int, Int>?>()
            val processed = mutableSetOf<Pair<Int, Int>>()
            val inStack = mutableSetOf(grid.start)
            var found = false

            stack.addLast(grid.start)
            parent[grid.start] = null

            while (stack.isNotEmpty() && !found) {
                val current = stack.removeLast()
                if (current in processed) continue
                processed.add(current)
                inStack.remove(current)

                if (current == grid.end) { found = true; break }

                // Reverse so right/down are explored first (stack is LIFO)
                for (neighbor in neighbors(current, grid.width, grid.height, grid.walls).reversed()) {
                    if (neighbor !in processed) {
                        if (neighbor !in inStack) {
                            inStack.add(neighbor)
                            parent[neighbor] = current
                        }
                        stack.addLast(neighbor)
                    }
                }

                yield(
                    VisualizationStep.Grid(
                        cells = buildCells(grid.width, grid.height, grid.walls, grid.start, grid.end, processed, inStack),
                        frontier = inStack.toSet(),
                        path = emptyList(),
                        metrics = GridMetrics(processed.size.toLong(), 0L),
                    )
                )
            }

            val path = if (found) tracePath(grid.end, parent) else emptyList()
            yield(
                VisualizationStep.Grid(
                    cells = buildCells(grid.width, grid.height, grid.walls, grid.start, grid.end, processed, emptySet(), path.toSet()),
                    frontier = emptySet(),
                    path = path,
                    metrics = GridMetrics(processed.size.toLong(), (path.size - 1).coerceAtLeast(0).toLong()),
                )
            )
        }
    }
}
