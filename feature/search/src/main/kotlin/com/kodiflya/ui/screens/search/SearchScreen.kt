package com.kodiflya.ui.screens.search

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
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
import com.kodiflya.core.plugin.BigO
import com.kodiflya.core.plugin.VisualizationStep
import com.kodiflya.ui.component.speedLevels
import com.kodiflya.ui.screens.AlgorithmScreenLayout

@Composable
fun SearchScreen(onComplexityClick: (BigO) -> Unit, viewModel: SearchViewModel = hiltViewModel()) {
    val visualizationState by viewModel.state.collectAsStateWithLifecycle()
    val activeIndex by viewModel.activeIndex.collectAsStateWithLifecycle()
    var speedIndex by remember { mutableFloatStateOf(1f) }

    val step = visualizationState.currentStep as? VisualizationStep.Search
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
        canvas = { SearchCanvas(step = step, modifier = Modifier.fillMaxSize()) },
        extraContent = step?.let { s ->
            { SearchTargetRow(target = s.target, found = s.found != null) }
        },
    )
}

@Composable
private fun SearchTargetRow(target: Int, found: Boolean) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = "Target:",
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.outlineVariant,
        )
        val accentColor = if (found) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.secondary
        Box(
            modifier = Modifier
                .size(28.dp)
                .clip(RoundedCornerShape(6.dp))
                .background(accentColor)
                .border(1.dp, accentColor, RoundedCornerShape(6.dp)),
            contentAlignment = Alignment.Center,
        ) {
            Text(
                text = target.toString(),
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.background,
            )
        }
        if (found) {
            Text(
                text = "found!",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.primary,
            )
        }
    }
}
