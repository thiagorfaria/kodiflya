package com.kodiflya.ui.screens.sorting

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kodiflya.core.engine.PlaybackEngineFactory
import com.kodiflya.core.plugin.AlgorithmPlugin
import com.kodiflya.core.plugin.Category
import com.kodiflya.core.plugin.Complexity
import com.kodiflya.core.plugin.VisualizationState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class SortingViewModel @Inject constructor(
    plugins: @JvmSuppressWildcards Set<AlgorithmPlugin>,
    private val engineFactory: PlaybackEngineFactory,
) : ViewModel() {

    val sortingPlugins: List<AlgorithmPlugin> =
        plugins.filter { it.category == Category.SORTING }.sortedBy { it.order }

    private val _activeIndex = MutableStateFlow(0)
    val activeIndex: StateFlow<Int> = _activeIndex.asStateFlow()

    private var engine = engineFactory.create(sortingPlugins[0], viewModelScope)

    private val _state = MutableStateFlow(engine.state.value)
    val state: StateFlow<VisualizationState> get() = engine.state

    val algorithmName: String get() = sortingPlugins[_activeIndex.value].displayName
    val complexity: Complexity get() = sortingPlugins[_activeIndex.value].complexity

    fun selectAlgorithm(index: Int) {
        if (index == _activeIndex.value) return
        engine.pause()
        _activeIndex.value = index
        engine = engineFactory.create(sortingPlugins[index], viewModelScope)
    }

    fun play() = engine.play()
    fun pause() = engine.pause()
    fun reset() = engine.reset()
    fun replay() { engine.reset(); engine.play() }
    fun setSpeed(multiplier: Float) = engine.setSpeed(multiplier)

    override fun onCleared() {
        super.onCleared()
        engine.pause()
    }
}
