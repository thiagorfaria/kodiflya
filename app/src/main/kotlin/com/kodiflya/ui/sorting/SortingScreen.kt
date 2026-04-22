package com.kodiflya.ui.sorting

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
import com.kodiflya.core.plugin.SortMetrics
import com.kodiflya.core.plugin.VizStep
import com.kodiflya.ui.components.AlgorithmChipRow
import com.kodiflya.ui.components.ComplexityCardsRow
import com.kodiflya.ui.components.ControlsRow
import com.kodiflya.ui.components.MetricCard
import com.kodiflya.ui.components.ScreenHeader
import com.kodiflya.ui.components.speedLevels
import com.kodiflya.ui.theme.AccentAmber
import com.kodiflya.ui.theme.AccentGreen
import com.kodiflya.ui.theme.AccentPeach
import com.kodiflya.ui.theme.Background
import com.kodiflya.ui.theme.Surface
import com.kodiflya.ui.theme.SurfaceBorder

@Composable
fun SortingScreen(viewModel: SortingViewModel = hiltViewModel()) {
    val vizState by viewModel.state.collectAsStateWithLifecycle()
    val activeIndex by viewModel.activeIndex.collectAsStateWithLifecycle()
    var speedIndex by remember { mutableFloatStateOf(1f) }

    val step = vizState.currentStep as? VizStep.Sort
    val metrics = step?.metrics ?: SortMetrics(0L, 0L, 0L)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Background)
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        ScreenHeader(algorithmName = viewModel.sortingPlugins[activeIndex].displayName)

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
            MetricCard("Comparisons", metrics.comparisons.toString(), AccentPeach, Modifier.weight(1f))
            MetricCard("Swaps", metrics.swaps.toString(), AccentGreen, Modifier.weight(1f))
            MetricCard("Array reads", metrics.arrayReads.toString(), AccentAmber, Modifier.weight(1f))
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
            SortingCanvas(
                step = step,
                modifier = Modifier.fillMaxSize(),
            )
        }

        ComplexityCardsRow(complexity = viewModel.sortingPlugins[activeIndex].complexity)

        ControlsRow(
            playbackStatus = vizState.playbackStatus,
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
