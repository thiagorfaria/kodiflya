package com.kodiflya.algorithms.trees

import com.kodiflya.core.plugin.AlgorithmInput
import com.kodiflya.core.plugin.AlgorithmPlugin
import com.kodiflya.core.plugin.Category
import com.kodiflya.core.plugin.ColorRole
import com.kodiflya.core.plugin.Complexity
import com.kodiflya.core.plugin.MetricLabel
import com.kodiflya.core.plugin.NodeState
import com.kodiflya.core.plugin.TreeMetrics
import com.kodiflya.core.plugin.VizStep

class BSTPreorder : AlgorithmPlugin {

    override val id = "bst_preorder"
    override val displayName = "Preorder"
    override val category = Category.TREES
    override val order = 1
    override val complexity = Complexity(
        bestCase = "O(n)",
        averageCase = "O(n)",
        worstCase = "O(n)",
        spaceComplexity = "O(h)",
    )
    override val metricLabels = listOf(
        MetricLabel("Visited", ColorRole.GREEN),
        MetricLabel("Total", ColorRole.AMBER),
        MetricLabel("Height", ColorRole.PEACH),
    )

    override fun initialData() = defaultTreeInput()

    override fun steps(input: AlgorithmInput): Sequence<VizStep> {
        val n = BST_VALUES.size.toLong()
        val h = 3L

        return sequence {
            val nodeStates = initialNodeStates()
            val sequence = mutableListOf<Int>()

            suspend fun SequenceScope<VizStep>.preorder(nodeValue: Int?) {
                if (nodeValue == null) return
                val node = BST_NODES[nodeValue] ?: return
                nodeStates[nodeValue] = NodeState.ACTIVE
                sequence.add(nodeValue)
                yield(VizStep.Tree(nodeStates.toMap(), sequence.toList(), TreeMetrics(sequence.size.toLong(), n, h)))
                nodeStates[nodeValue] = NodeState.VISITED
                preorder(node.left)
                preorder(node.right)
            }

            preorder(BST_ROOT)
            yield(VizStep.Tree(nodeStates.toMap(), sequence.toList(), TreeMetrics(sequence.size.toLong(), n, h)))
        }
    }
}
