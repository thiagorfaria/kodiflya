package com.kodiflya.ui.screens.sorting

import com.kodiflya.core.engine.PlaybackEngineFactory
import com.kodiflya.core.plugin.AlgorithmPlugin
import com.kodiflya.core.plugin.Category
import com.kodiflya.ui.screens.AlgorithmViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SortingViewModel @Inject constructor(
    plugins: @JvmSuppressWildcards Set<AlgorithmPlugin>,
    engineFactory: PlaybackEngineFactory,
) : AlgorithmViewModel(plugins, engineFactory, Category.SORTING)
