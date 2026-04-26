package com.kodiflya.ui.screens.graph

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
import com.kodiflya.core.plugin.AlgorithmInput
import com.kodiflya.core.plugin.GridMetrics
import com.kodiflya.core.plugin.VisualizationStep
import com.kodiflya.ui.component.AlgorithmChipRow
import com.kodiflya.ui.component.ComplexityCardsRow
import com.kodiflya.ui.component.ControlsRow
import com.kodiflya.ui.component.MetricCard
import com.kodiflya.ui.component.ScreenHeader
import com.kodiflya.ui.component.speedLevels

@Composable
fun GraphScreen(viewModel: GraphViewModel = hiltViewModel()) {
    val visualizationState by viewModel.state.collectAsStateWithLifecycle()
    val activeIndex by viewModel.activeIndex.collectAsStateWithLifecycle()
    var speedIndex by remember { mutableFloatStateOf(1f) }

    val step = visualizationState.currentStep as? VisualizationStep.Grid
    val metrics = step?.metrics ?: GridMetrics(0L, 0L)
    val frontierSize = step?.frontier?.size?.toLong() ?: 0L
    val initialGrid = viewModel.graphPlugins[activeIndex].initialData() as AlgorithmInput.GridInput

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        ScreenHeader(algorithmName = viewModel.graphPlugins[activeIndex].displayName)

        AlgorithmChipRow(
            plugins = viewModel.graphPlugins,
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
            MetricCard("Visited", metrics.visited.toString(), MaterialTheme.colorScheme.tertiary, Modifier.weight(1f))
            MetricCard("Frontier", frontierSize.toString(), MaterialTheme.colorScheme.error, Modifier.weight(1f))
            MetricCard("Path length", metrics.pathLength.toString(), MaterialTheme.colorScheme.primary, Modifier.weight(1f))
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
            GraphCanvas(step = step, initialGrid = initialGrid, modifier = Modifier.fillMaxSize())
        }

        ComplexityCardsRow(complexity = viewModel.graphPlugins[activeIndex].complexity)

        ControlsRow(
            playbackStatus = visualizationState.playbackStatus,
            speedIndex = speedIndex,
            onPlay = viewModel::play,
            onPause = viewModel::pause,
            onReset = viewModel::reset,
            onSpeedChange = { index ->
                speedIndex = index
                val multiplier = speedLevels[index.toInt().coerceIn(0, speedLevels.lastIndex)]
                viewModel.setSpeed(multiplier)
            },
        )
    }
}
