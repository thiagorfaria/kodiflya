package com.kodiflya.ui.screens.trees

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kodiflya.core.engine.PlaybackEngineFactory
import com.kodiflya.core.plugin.AlgorithmPlugin
import com.kodiflya.core.plugin.Category
import com.kodiflya.core.plugin.VisualizationState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class TreeViewModel @Inject constructor(
    plugins: @JvmSuppressWildcards Set<AlgorithmPlugin>,
    private val engineFactory: PlaybackEngineFactory,
) : ViewModel() {

    val treePlugins: List<AlgorithmPlugin> =
        plugins.filter { it.category == Category.TREES }.sortedBy { it.order }

    private val _activeIndex = MutableStateFlow(0)
    val activeIndex: StateFlow<Int> = _activeIndex.asStateFlow()

    private var engine = engineFactory.create(treePlugins[0], viewModelScope)

    val state: StateFlow<VisualizationState> get() = engine.state

    fun selectAlgorithm(index: Int) {
        if (index == _activeIndex.value) return
        engine.pause()
        _activeIndex.value = index
        engine = engineFactory.create(treePlugins[index], viewModelScope)
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
