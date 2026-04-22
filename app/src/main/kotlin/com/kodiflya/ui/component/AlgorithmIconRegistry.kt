package com.kodiflya.ui.component

import com.kodiflya.core.plugin.Category

object AlgorithmIconRegistry {

    fun iconFor(category: Category): AlgorithmIconSource = when (category) {
        Category.SORTING -> TODO("Provide ic_algo_sorting.xml drawable asset")
        Category.GRAPH   -> TODO("Provide ic_algo_graph.xml drawable asset")
        Category.TREES   -> TODO("Provide ic_algo_trees.xml drawable asset")
    }

    fun selectedIconFor(category: Category): AlgorithmIconSource = when (category) {
        Category.SORTING -> TODO("Provide ic_algo_sorting_filled.xml drawable asset")
        Category.GRAPH   -> TODO("Provide ic_algo_graph_filled.xml drawable asset")
        Category.TREES   -> TODO("Provide ic_algo_trees_filled.xml drawable asset")
    }
}
