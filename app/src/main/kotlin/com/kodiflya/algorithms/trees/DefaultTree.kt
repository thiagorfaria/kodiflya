package com.kodiflya.algorithms.trees

import com.kodiflya.core.plugin.AlgorithmInput
import com.kodiflya.core.plugin.NodeState

internal data class BSTNode(val value: Int, val left: Int?, val right: Int?)

// Fixed balanced BST — values 1..7, height 3.
//        4
//       / \
//      2   6
//     / \ / \
//    1  3 5  7
internal val BST_ROOT = 4
internal val BST_VALUES = listOf(4, 2, 6, 1, 3, 5, 7)
internal val BST_NODES = mapOf(
    4 to BSTNode(4, left = 2, right = 6),
    2 to BSTNode(2, left = 1, right = 3),
    6 to BSTNode(6, left = 5, right = 7),
    1 to BSTNode(1, left = null, right = null),
    3 to BSTNode(3, left = null, right = null),
    5 to BSTNode(5, left = null, right = null),
    7 to BSTNode(7, left = null, right = null),
)

// Fractional (x, y) positions for canvas rendering — origin top-left.
internal val NODE_POSITIONS: Map<Int, Pair<Float, Float>> = mapOf(
    4 to Pair(0.500f, 0.15f),
    2 to Pair(0.250f, 0.45f),
    6 to Pair(0.750f, 0.45f),
    1 to Pair(0.125f, 0.75f),
    3 to Pair(0.375f, 0.75f),
    5 to Pair(0.625f, 0.75f),
    7 to Pair(0.875f, 0.75f),
)

internal val EDGES = listOf(4 to 2, 4 to 6, 2 to 1, 2 to 3, 6 to 5, 6 to 7)

internal fun defaultTreeInput() = AlgorithmInput.TreeInput(values = BST_VALUES)

internal fun initialNodeStates(): MutableMap<Int, NodeState> =
    BST_VALUES.associateWith { NodeState.DEFAULT }.toMutableMap()
