package com.kodiflya.algorithms.graph

import com.kodiflya.core.plugin.AlgorithmInput
import com.kodiflya.core.plugin.AlgorithmPlugin
import com.kodiflya.core.plugin.Category
import com.kodiflya.core.plugin.ColorRole
import com.kodiflya.core.plugin.BigO
import com.kodiflya.core.plugin.Complexity
import com.kodiflya.core.plugin.GridMetrics
import com.kodiflya.core.plugin.MetricLabel
import com.kodiflya.core.plugin.VisualizationStep
import java.util.PriorityQueue

class Dijkstra : AlgorithmPlugin {

    override val id = "dijkstra"
    override val displayName = "Dijkstra"
    override val category = Category.GRAPH
    override val order = 2
    override val complexity = Complexity(
        bestCase = BigO.O_V_PLUS_E,
        averageCase = BigO.O_V_PLUS_E_LOG_V,
        worstCase = BigO.O_V_PLUS_E_LOG_V,
        spaceComplexity = BigO.O_V,
    )
    override val metricLabels = listOf(
        MetricLabel("Visited", ColorRole.PURPLE),
        MetricLabel("Frontier", ColorRole.PEACH),
        MetricLabel("Path length", ColorRole.GREEN),
    )

    override fun initialData() = defaultGridInput()

    override fun steps(input: AlgorithmInput): Sequence<VisualizationStep> {
        val grid = input as AlgorithmInput.GridInput

        return sequence {
            // dist: distance from start; pq entry = (distance, position)
            val pq = PriorityQueue<Pair<Int, Pair<Int, Int>>>(compareBy { it.first })
            val dist = mutableMapOf<Pair<Int, Int>, Int>().withDefault { Int.MAX_VALUE }
            val parent = mutableMapOf<Pair<Int, Int>, Pair<Int, Int>?>()
            val processed = mutableSetOf<Pair<Int, Int>>()
            val inQueue = mutableSetOf(grid.start)
            var found = false

            pq.add(Pair(0, grid.start))
            dist[grid.start] = 0
            parent[grid.start] = null

            while (pq.isNotEmpty() && !found) {
                val (d, current) = pq.poll()
                if (current in processed) continue
                processed.add(current)
                inQueue.remove(current)

                if (current == grid.end) { found = true; break }

                for (neighbor in neighbors(current, grid.width, grid.height, grid.walls)) {
                    val newDist = d + 1
                    if (newDist < dist.getValue(neighbor)) {
                        dist[neighbor] = newDist
                        parent[neighbor] = current
                        if (neighbor !in processed) {
                            inQueue.add(neighbor)
                            pq.add(Pair(newDist, neighbor))
                        }
                    }
                }

                yield(
                    VisualizationStep.Grid(
                        cells = buildCells(grid.width, grid.height, grid.walls, grid.start, grid.end, processed, inQueue),
                        frontier = inQueue.toSet(),
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
