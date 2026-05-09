package com.kodiflya.ui.screens.sorting

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.kodiflya.core.plugin.VisualizationStep
import com.kodiflya.ui.component.speedLevels
import com.kodiflya.ui.screens.AlgorithmScreenLayout

@Composable
fun SortingScreen(viewModel: SortingViewModel = hiltViewModel()) {
    val visualizationState by viewModel.state.collectAsStateWithLifecycle()
    val activeIndex by viewModel.activeIndex.collectAsStateWithLifecycle()
    var speedIndex by remember { mutableFloatStateOf(1f) }

    val step = visualizationState.currentStep as? VisualizationStep.Sort
    val metricValues = step?.metricValues ?: List(viewModel.categoryPlugins[activeIndex].metricLabels.size) { 0L }

    AlgorithmScreenLayout(
        plugins = viewModel.categoryPlugins,
        activeIndex = activeIndex,
        onSelectAlgorithm = { index ->
            viewModel.selectAlgorithm(index)
        },
        metricValues = metricValues,
        playbackStatus = visualizationState.playbackStatus,
        speedIndex = speedIndex,
        onSpeedChange = { index ->
            viewModel.setSpeed(speedLevels[index.toInt().coerceIn(0, speedLevels.lastIndex)])
        },
        onPlay = viewModel::play,
        onPause = viewModel::pause,
        onReset = viewModel::reset,
        onReplay = viewModel::replay,
        canvas = { SortingCanvas(step = step, modifier = Modifier.fillMaxSize()) },
    )
}
