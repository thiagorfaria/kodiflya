package com.kodiflya.di

import com.kodiflya.algorithms.graph.BFS
import com.kodiflya.algorithms.search.BinarySearch
import com.kodiflya.algorithms.search.LinearSearch
import com.kodiflya.algorithms.trees.BSTInorder
import com.kodiflya.algorithms.trees.BSTPostorder
import com.kodiflya.algorithms.trees.BSTPreorder
import com.kodiflya.algorithms.graph.DFS
import com.kodiflya.algorithms.graph.Dijkstra
import com.kodiflya.algorithms.sorting.BubbleSort
import com.kodiflya.algorithms.sorting.InsertionSort
import com.kodiflya.algorithms.sorting.MergeSort
import com.kodiflya.algorithms.sorting.QuickSort
import com.kodiflya.core.plugin.AlgorithmPlugin
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dagger.multibindings.IntoSet

// Adding a new algorithm = one new @Provides @IntoSet function here + one new file in algorithms/.
// No changes to Engine, ViewModel, or UI screens.
@Module
@InstallIn(SingletonComponent::class)
object AlgorithmModule {

    @Provides @IntoSet fun provideBubbleSort(): AlgorithmPlugin = BubbleSort()
    @Provides @IntoSet fun provideInsertionSort(): AlgorithmPlugin = InsertionSort()
    @Provides @IntoSet fun provideMergeSort(): AlgorithmPlugin = MergeSort()
    @Provides @IntoSet fun provideQuickSort(): AlgorithmPlugin = QuickSort()

    @Provides @IntoSet fun provideBFS(): AlgorithmPlugin = BFS()
    @Provides @IntoSet fun provideDFS(): AlgorithmPlugin = DFS()
    @Provides @IntoSet fun provideDijkstra(): AlgorithmPlugin = Dijkstra()

    @Provides @IntoSet fun provideBSTInorder(): AlgorithmPlugin = BSTInorder()
    @Provides @IntoSet fun provideBSTPreorder(): AlgorithmPlugin = BSTPreorder()
    @Provides @IntoSet fun provideBSTPostorder(): AlgorithmPlugin = BSTPostorder()

    @Provides @IntoSet fun provideBinarySearch(): AlgorithmPlugin = BinarySearch()
    @Provides @IntoSet fun provideLinearSearch(): AlgorithmPlugin = LinearSearch()
}
