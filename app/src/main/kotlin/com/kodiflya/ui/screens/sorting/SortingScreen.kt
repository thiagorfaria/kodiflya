package com.kodiflya.ui.screens.sorting

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.kodiflya.core.plugin.VisualizationStep
import com.kodiflya.ui.component.AlgorithmChipRow
import com.kodiflya.ui.component.ComplexityCardsRow
import com.kodiflya.ui.component.ControlsRow
import com.kodiflya.ui.component.MetricCard
import com.kodiflya.ui.component.ScreenHeader
import com.kodiflya.ui.component.speedLevels
import com.kodiflya.ui.component.toColor

@Composable
fun SortingScreen(viewModel: SortingViewModel = hiltViewModel()) {
    val visualizationState by viewModel.state.collectAsStateWithLifecycle()
    val activeIndex by viewModel.activeIndex.collectAsStateWithLifecycle()
    var speedIndex by remember { mutableFloatStateOf(1f) }

    val activePlugin = viewModel.sortingPlugins[activeIndex]
    val step = visualizationState.currentStep as? VisualizationStep.Sort
    val metricValues = step?.metricValues ?: List(activePlugin.metricLabels.size) { 0L }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        ScreenHeader(algorithmName = activePlugin.displayName)

        AlgorithmChipRow(
            plugins = viewModel.sortingPlugins,
            activeIndex = activeIndex,
            onSelect = { index ->
                viewModel.selectAlgorithm(index)
                speedIndex = 1f
            },
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            activePlugin.metricLabels.zip(metricValues).forEach { (metricLabel, value) ->
                MetricCard(
                    label = metricLabel.label,
                    value = value.toString(),
                    accentColor = metricLabel.colorRole.toColor(),
                    modifier = Modifier.weight(1f),
                )
            }
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .clip(RoundedCornerShape(12.dp))
                .background(MaterialTheme.colorScheme.surface)
                .border(1.dp, MaterialTheme.colorScheme.outline, RoundedCornerShape(12.dp))
                .padding(12.dp),
        ) {
            SortingCanvas(
                step = step,
                modifier = Modifier.fillMaxSize(),
            )
        }

        ComplexityCardsRow(complexity = activePlugin.complexity)

        ControlsRow(
            playbackStatus = visualizationState.playbackStatus,
            speedIndex = speedIndex,
            onPlay = viewModel::play,
            onPause = viewModel::pause,
            onReset = viewModel::reset,
            onReplay = viewModel::replay,
            onSpeedChange = { index ->
                speedIndex = index
                val multiplier = speedLevels[index.toInt().coerceIn(0, speedLevels.lastIndex)]
                viewModel.setSpeed(multiplier)
            },
        )
    }
}
