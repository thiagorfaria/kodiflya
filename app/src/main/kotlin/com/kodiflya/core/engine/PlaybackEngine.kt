package com.kodiflya.core.engine

import com.kodiflya.core.plugin.AlgorithmInput
import com.kodiflya.core.plugin.AlgorithmPlugin
import com.kodiflya.core.plugin.PlaybackStatus
import com.kodiflya.core.plugin.VizState
import com.kodiflya.core.plugin.VizStep
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class PlaybackEngine @AssistedInject constructor(
    @Assisted private val plugin: AlgorithmPlugin,
    @Assisted private val scope: CoroutineScope,
) {
    private var currentInput: AlgorithmInput = plugin.initialData()

    private val _state = MutableStateFlow(VizState.idle())
    val state: StateFlow<VizState> = _state.asStateFlow()

    // Step delay in ms. 400ms = 1×. Speed multiplier divides this value.
    private val _speedMs = MutableStateFlow(400L)
    val speedMs: StateFlow<Long> = _speedMs.asStateFlow()

    private var playbackJob: Job? = null
    private var stepCursor: Int = 0
    private var allSteps: List<VizStep>? = null

    fun play() {
        if (playbackJob?.isActive == true) return
        playbackJob = scope.launch {
            val steps = allSteps
                ?: plugin.steps(currentInput).toList().also { allSteps = it }

            _state.update { it.copy(totalSteps = steps.size) }

            for (i in stepCursor until steps.size) {
                val step = steps[i]
                _state.update {
                    it.copy(
                        currentStep = step,
                        stepIndex = i,
                        playbackStatus = PlaybackStatus.PLAYING,
                    )
                }
                stepCursor = i + 1
                delay(_speedMs.value)
            }
            _state.update { it.copy(playbackStatus = PlaybackStatus.DONE) }
        }
    }

    fun pause() {
        playbackJob?.cancel()
        _state.update { it.copy(playbackStatus = PlaybackStatus.PAUSED) }
    }

    fun reset() {
        playbackJob?.cancel()
        stepCursor = 0
        allSteps = null
        currentInput = plugin.initialData()
        _state.value = VizState.idle()
    }

    // Multiplier: 0.5 → 800ms, 1.0 → 400ms, 2.0 → 200ms, 4.0 → 100ms, 8.0 → 50ms
    // Floor at 8ms to avoid UI thread starvation.
    fun setSpeed(multiplier: Float) {
        _speedMs.value = (400f / multiplier).toLong().coerceIn(8L, 800L)
    }
}
