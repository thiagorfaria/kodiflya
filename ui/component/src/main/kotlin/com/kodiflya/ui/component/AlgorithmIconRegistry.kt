package com.kodiflya.ui.component

import com.kodiflya.core.plugin.Category

object AlgorithmIconRegistry {

    fun iconFor(category: Category): AlgorithmIconSource = when (category) {
        Category.SORTING -> AlgorithmIconSource.Vector(NavigationIcons.SortOutlined)
        Category.GRAPH   -> AlgorithmIconSource.Vector(NavigationIcons.GraphOutlined)
        Category.TREES   -> AlgorithmIconSource.Vector(NavigationIcons.TreesOutlined)
        Category.SEARCH  -> AlgorithmIconSource.Vector(NavigationIcons.SearchOutlined)
    }

    fun selectedIconFor(category: Category): AlgorithmIconSource = when (category) {
        Category.SORTING -> AlgorithmIconSource.Vector(NavigationIcons.SortFilled)
        Category.GRAPH   -> AlgorithmIconSource.Vector(NavigationIcons.GraphFilled)
        Category.TREES   -> AlgorithmIconSource.Vector(NavigationIcons.TreesFilled)
        Category.SEARCH  -> AlgorithmIconSource.Vector(NavigationIcons.SearchFilled)
    }
}
