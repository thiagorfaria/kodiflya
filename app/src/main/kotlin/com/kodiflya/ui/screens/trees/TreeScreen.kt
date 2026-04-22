package com.kodiflya.ui.screens.trees

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.kodiflya.algorithms.trees.BST_VALUES
import com.kodiflya.core.plugin.TreeMetrics
import com.kodiflya.core.plugin.VisualizationStep
import com.kodiflya.ui.component.AlgorithmChipRow
import com.kodiflya.ui.component.ComplexityCardsRow
import com.kodiflya.ui.component.ControlsRow
import com.kodiflya.ui.component.MetricCard
import com.kodiflya.ui.component.ScreenHeader
import com.kodiflya.ui.component.speedLevels
import com.kodiflya.ui.theme.AccentAmber
import com.kodiflya.ui.theme.AccentGreen
import com.kodiflya.ui.theme.AccentPeach
import com.kodiflya.ui.theme.Background
import com.kodiflya.ui.theme.ElementDefault
import com.kodiflya.ui.theme.Surface
import com.kodiflya.ui.theme.SurfaceBorder
import com.kodiflya.ui.theme.TextSecondary
import com.kodiflya.ui.theme.MetricLabel as MetricLabelColor

@Composable
fun TreeScreen(viewModel: TreeViewModel = hiltViewModel()) {
    val visualizationState by viewModel.state.collectAsStateWithLifecycle()
    val activeIndex by viewModel.activeIndex.collectAsStateWithLifecycle()
    var speedIndex by remember { mutableFloatStateOf(1f) }

    val step = visualizationState.currentStep as? VisualizationStep.Tree
    val metrics = step?.metrics ?: TreeMetrics(0L, BST_VALUES.size.toLong(), 3L)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Background)
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        ScreenHeader(algorithmName = viewModel.treePlugins[activeIndex].displayName)

        AlgorithmChipRow(
            plugins = viewModel.treePlugins,
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
            MetricCard("Visited", metrics.visited.toString(), AccentGreen, Modifier.weight(1f))
            MetricCard("Total", metrics.totalNodes.toString(), AccentAmber, Modifier.weight(1f))
            MetricCard("Height", metrics.height.toString(), AccentPeach, Modifier.weight(1f))
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .clip(RoundedCornerShape(12.dp))
                .background(Surface)
                .border(1.dp, SurfaceBorder, RoundedCornerShape(12.dp))
                .padding(12.dp),
        ) {
            TreeCanvas(step = step, modifier = Modifier.fillMaxSize())
        }

        TraversalSequenceRow(
            allValues = BST_VALUES.sorted(),
            sequence = step?.traversalSequence ?: emptyList(),
        )

        ComplexityCardsRow(complexity = viewModel.treePlugins[activeIndex].complexity)

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

@Composable
private fun TraversalSequenceRow(
    allValues: List<Int>,
    sequence: List<Int>,
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(6.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = "Order:",
            style = MaterialTheme.typography.labelSmall,
            color = MetricLabelColor,
        )
        allValues.forEach { value ->
            val position = sequence.indexOf(value)
            val isVisited = position >= 0
            Box(
                modifier = Modifier
                    .size(28.dp)
                    .clip(CircleShape)
                    .background(if (isVisited) AccentGreen else ElementDefault)
                    .border(1.dp, if (isVisited) AccentGreen else SurfaceBorder, CircleShape),
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    text = value.toString(),
                    style = MaterialTheme.typography.labelSmall,
                    color = if (isVisited) Background else TextSecondary,
                )
            }
        }
    }
}
