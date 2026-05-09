package com.kodiflya.ui.screens

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kodiflya.core.engine.PlaybackEngineFactory
import com.kodiflya.core.plugin.AlgorithmPlugin
import com.kodiflya.core.plugin.Category
import com.kodiflya.core.plugin.VisualizationState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

abstract class AlgorithmViewModel(
    plugins: Set<AlgorithmPlugin>,
    private val engineFactory: PlaybackEngineFactory,
    category: Category,
) : ViewModel() {

    val categoryPlugins: List<AlgorithmPlugin> =
        plugins.filter { it.category == category }.sortedBy { it.order }

    private val _activeIndex = MutableStateFlow(0)
    val activeIndex: StateFlow<Int> = _activeIndex.asStateFlow()

    private var engine = engineFactory.create(categoryPlugins[0], viewModelScope)

    val state: StateFlow<VisualizationState> get() = engine.state

    fun selectAlgorithm(index: Int) {
        if (index == _activeIndex.value) return
        engine.reset()
        _activeIndex.value = index
        engine = engineFactory.create(categoryPlugins[index], viewModelScope)
    }

    fun play() = engine.play()
    fun pause() = engine.pause()
    fun reset() = engine.reset()
    fun replay() { engine.reset(); engine.play() }
    fun setSpeed(multiplier: Float) = engine.setSpeed(multiplier)

    override fun onCleared() { super.onCleared(); engine.pause() }
}
