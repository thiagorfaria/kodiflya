package com.kodiflya.ui.screens.trees

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
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
import com.kodiflya.core.plugin.BigO
import com.kodiflya.core.plugin.VisualizationStep
import com.kodiflya.ui.component.speedLevels
import com.kodiflya.ui.screens.AlgorithmScreenLayout

@Composable
fun TreeScreen(onComplexityClick: (BigO) -> Unit, viewModel: TreeViewModel = hiltViewModel()) {
    val visualizationState by viewModel.state.collectAsStateWithLifecycle()
    val activeIndex by viewModel.activeIndex.collectAsStateWithLifecycle()
    var speedIndex by remember { mutableFloatStateOf(1f) }

    val step = visualizationState.currentStep as? VisualizationStep.Tree
    val metricValues = step?.metricValues ?: List(viewModel.categoryPlugins[activeIndex].metricLabels.size) { 0L }

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
        onComplexityClick = onComplexityClick,
        canvas = { TreeCanvas(step = step, modifier = Modifier.fillMaxSize()) },
        extraContent = {
            TraversalSequenceRow(
                allValues = step?.nodeStates?.keys?.sorted() ?: emptyList(),
                sequence = step?.traversalSequence ?: emptyList(),
            )
        },
    )
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
            color = MaterialTheme.colorScheme.outlineVariant,
        )
        allValues.forEach { value ->
            val position = sequence.indexOf(value)
            val isVisited = position >= 0
            Box(
                modifier = Modifier
                    .size(28.dp)
                    .clip(CircleShape)
                    .background(if (isVisited) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant)
                    .border(1.dp, if (isVisited) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outline, CircleShape),
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    text = value.toString(),
                    style = MaterialTheme.typography.labelSmall,
                    color = if (isVisited) MaterialTheme.colorScheme.background else MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
        }
    }
}
