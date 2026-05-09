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

internal fun defaultTreeInput() = AlgorithmInput.TreeInput(values = BST_VALUES)

internal fun initialNodeStates(): MutableMap<Int, NodeState> =
    BST_VALUES.associateWith { NodeState.DEFAULT }.toMutableMap()
