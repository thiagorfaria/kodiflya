package com.kodiflya.ui.screens.home

import androidx.lifecycle.ViewModel
import com.kodiflya.core.plugin.AlgorithmPlugin
import com.kodiflya.core.plugin.Category
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    plugins: @JvmSuppressWildcards Set<AlgorithmPlugin>,
) : ViewModel() {

    data class CategorySummary(
        val category: Category,
        val displayName: String,
        val algorithmCount: Int,
    )

    val categories: List<CategorySummary> = listOf(
        CategorySummary(Category.SORTING, "Sort",  plugins.count { it.category == Category.SORTING }),
        CategorySummary(Category.GRAPH,   "Graph", plugins.count { it.category == Category.GRAPH }),
        CategorySummary(Category.TREES,   "Trees", plugins.count { it.category == Category.TREES }),
    )

    val spotlightPlugin: AlgorithmPlugin =
        plugins.filter { it.category == Category.SORTING }.minByOrNull { it.order }
            ?: plugins.minByOrNull { it.order }!!
}
