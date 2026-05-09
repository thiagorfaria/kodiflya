package com.kodiflya.ui.screens.graph

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.kodiflya.core.plugin.AlgorithmInput
import com.kodiflya.core.plugin.VisualizationStep
import com.kodiflya.ui.component.speedLevels
import com.kodiflya.ui.screens.AlgorithmScreenLayout

@Composable
fun GraphScreen(viewModel: GraphViewModel = hiltViewModel()) {
    val visualizationState by viewModel.state.collectAsStateWithLifecycle()
    val activeIndex by viewModel.activeIndex.collectAsStateWithLifecycle()
    var speedIndex by remember { mutableFloatStateOf(1f) }

    val activePlugin = viewModel.categoryPlugins[activeIndex]
    val step = visualizationState.currentStep as? VisualizationStep.Grid
    val metricValues = step?.metricValues ?: List(activePlugin.metricLabels.size) { 0L }
    val initialGrid = activePlugin.initialData() as AlgorithmInput.GridInput

    AlgorithmScreenLayout(
        plugins = viewModel.categoryPlugins,
        activeIndex = activeIndex,
        onSelectAlgorithm = { index ->
            viewModel.selectAlgorithm(index)
            speedIndex = 1f
        },
        metricValues = metricValues,
        playbackStatus = visualizationState.playbackStatus,
        speedIndex = speedIndex,
        onSpeedChange = { index ->
            speedIndex = index
            viewModel.setSpeed(speedLevels[index.toInt().coerceIn(0, speedLevels.lastIndex)])
        },
        onPlay = viewModel::play,
        onPause = viewModel::pause,
        onReset = viewModel::reset,
        onReplay = viewModel::replay,
        canvas = { GraphCanvas(step = step, initialGrid = initialGrid, modifier = Modifier.fillMaxSize()) },
    )
}
