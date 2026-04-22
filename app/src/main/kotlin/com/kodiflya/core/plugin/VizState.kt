package com.kodiflya.core.plugin

data class VizState(
    val currentStep: VizStep?,
    val stepIndex: Int,
    val totalSteps: Int,
    val playbackStatus: PlaybackStatus,
) {
    val progressFraction: Float
        get() = if (totalSteps == 0) 0f else stepIndex.toFloat() / totalSteps

    companion object {
        fun idle() = VizState(
            currentStep = null,
            stepIndex = 0,
            totalSteps = 0,
            playbackStatus = PlaybackStatus.IDLE,
        )
    }
}

enum class PlaybackStatus { IDLE, PLAYING, PAUSED, DONE }
