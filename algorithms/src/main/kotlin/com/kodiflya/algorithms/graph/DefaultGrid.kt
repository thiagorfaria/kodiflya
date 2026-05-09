package com.kodiflya.algorithms.graph

import com.kodiflya.core.plugin.AlgorithmInput
import com.kodiflya.core.plugin.CellState

internal const val GRID_SIZE = 9
internal val DEFAULT_START = Pair(0, 0)
internal val DEFAULT_END = Pair(8, 8)

// Fixed maze — solvable, interesting enough to show search differences between algorithms.
// Coordinates: Pair(col, row) — col=x, row=y, origin top-left.
internal val DEFAULT_WALLS: Set<Pair<Int, Int>> = setOf(
    Pair(1,1), Pair(2,1), Pair(3,1), Pair(4,1), Pair(5,1), Pair(6,1),
    Pair(6,2),
    Pair(0,3), Pair(1,3), Pair(2,3), Pair(3,3), Pair(6,3),
    Pair(5,4), Pair(6,4),
    Pair(1,5), Pair(2,5), Pair(3,5), Pair(4,5), Pair(5,5),
    Pair(1,6), Pair(7,6),
    Pair(4,7), Pair(5,7), Pair(6,7), Pair(7,7),
)

internal fun defaultGridInput() = AlgorithmInput.GridInput(
    width = GRID_SIZE,
    height = GRID_SIZE,
    walls = DEFAULT_WALLS,
    start = DEFAULT_START,
    end = DEFAULT_END,
)

// 4-directional neighbors within bounds, excluding walls.
internal fun neighbors(
    pos: Pair<Int, Int>,
    width: Int,
    height: Int,
    walls: Set<Pair<Int, Int>>,
): List<Pair<Int, Int>> {
    val (col, row) = pos
    return listOf(
        Pair(col + 1, row),
        Pair(col, row + 1),
        Pair(col - 1, row),
        Pair(col, row - 1),
    ).filter { (c, r) -> c in 0 until width && r in 0 until height && Pair(c, r) !in walls }
}

// Builds a full 81-cell snapshot for stateless rendering.
internal fun buildCells(
    width: Int,
    height: Int,
    walls: Set<Pair<Int, Int>>,
    start: Pair<Int, Int>,
    end: Pair<Int, Int>,
    processed: Set<Pair<Int, Int>>,
    frontier: Set<Pair<Int, Int>>,
    path: Set<Pair<Int, Int>> = emptySet(),
): Map<Pair<Int, Int>, CellState> = buildMap {
    for (row in 0 until height) {
        for (col in 0 until width) {
            val pos = Pair(col, row)
            put(pos, when (pos) {
                start -> CellState.START
                end -> CellState.END
                in walls -> CellState.WALL
                in path -> CellState.PATH
                in frontier -> CellState.FRONTIER
                in processed -> CellState.VISITED
                else -> CellState.OPEN
            }
            )
        }
    }
}

// Traces path back from end to start using parent map.
internal fun tracePath(
    end: Pair<Int, Int>,
    parent: Map<Pair<Int, Int>, Pair<Int, Int>?>,
): List<Pair<Int, Int>> {
    val path = mutableListOf<Pair<Int, Int>>()
    var current: Pair<Int, Int>? = end
    while (current != null) {
        path.add(current)
        current = parent[current]
    }
    return path.reversed()
}
