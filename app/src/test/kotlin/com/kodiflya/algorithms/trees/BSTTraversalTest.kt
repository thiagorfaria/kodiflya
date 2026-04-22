package com.kodiflya.algorithms.trees

import com.kodiflya.core.plugin.VisualizationStep
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class BSTTraversalTest {

    @Test
    fun `Inorder produces sorted sequence 1-2-3-4-5-6-7`() {
        val steps = BSTInorder().let { it.steps(it.initialData()) }.toList()
        val lastStep = steps.last() as VisualizationStep.Tree
        assertEquals(listOf(1, 2, 3, 4, 5, 6, 7), lastStep.traversalSequence)
    }

    @Test
    fun `Preorder visits root first`() {
        val steps = BSTPreorder().let { it.steps(it.initialData()) }.toList()
        val lastStep = steps.last() as VisualizationStep.Tree
        assertEquals(BST_ROOT, lastStep.traversalSequence.first())
        assertEquals(listOf(4, 2, 1, 3, 6, 5, 7), lastStep.traversalSequence)
    }

    @Test
    fun `Postorder visits root last`() {
        val steps = BSTPostorder().let { it.steps(it.initialData()) }.toList()
        val lastStep = steps.last() as VisualizationStep.Tree
        assertEquals(BST_ROOT, lastStep.traversalSequence.last())
        assertEquals(listOf(1, 3, 2, 5, 7, 6, 4), lastStep.traversalSequence)
    }

    @Test
    fun `all traversals visit all 7 nodes`() {
        listOf(BSTInorder(), BSTPreorder(), BSTPostorder()).forEach { algo ->
            val lastStep = algo.steps(algo.initialData()).toList().last() as VisualizationStep.Tree
            assertEquals(7, lastStep.traversalSequence.size, "${algo.displayName}: not all nodes visited")
            assertEquals(BST_VALUES.sorted(), lastStep.traversalSequence.sorted(), "${algo.displayName}: wrong nodes visited")
        }
    }

    @Test
    fun `each step carries full node state map for stateless rendering`() {
        listOf(BSTInorder(), BSTPreorder(), BSTPostorder()).forEach { algo ->
            algo.steps(algo.initialData()).forEach { step ->
                check(step is VisualizationStep.Tree)
                assertEquals(BST_VALUES.size, step.nodeStates.size, "${algo.displayName}: incomplete node state map")
            }
        }
    }

    @Test
    fun `traversal sequence grows by one per step`() {
        listOf(BSTInorder(), BSTPreorder(), BSTPostorder()).forEach { algo ->
            val steps = algo.steps(algo.initialData()).toList().dropLast(1) // exclude final summary step
            steps.forEachIndexed { i, step ->
                check(step is VisualizationStep.Tree)
                assertEquals(i + 1, step.traversalSequence.size, "${algo.displayName} step $i: wrong sequence size")
            }
        }
    }
}
