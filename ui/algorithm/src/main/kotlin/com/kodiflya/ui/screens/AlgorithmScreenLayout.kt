package com.kodiflya.ui.screens

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
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.kodiflya.core.plugin.AlgorithmPlugin
import com.kodiflya.core.plugin.BigO
import com.kodiflya.core.plugin.PlaybackStatus
import com.kodiflya.ui.component.AlgorithmChipRow
import com.kodiflya.ui.component.ComplexityCardsRow
import com.kodiflya.ui.component.ControlsRow
import com.kodiflya.ui.component.MetricCard
import com.kodiflya.ui.component.ScreenHeader
import com.kodiflya.ui.component.toColor

@Composable
fun AlgorithmScreenLayout(
    plugins: List<AlgorithmPlugin>,
    activeIndex: Int,
    onSelectAlgorithm: (Int) -> Unit,
    metricValues: List<Long>,
    playbackStatus: PlaybackStatus,
    speedIndex: Float,
    onSpeedChange: (Float) -> Unit,
    onPlay: () -> Unit,
    onPause: () -> Unit,
    onReset: () -> Unit,
    onReplay: () -> Unit,
    onComplexityClick: (BigO) -> Unit,
    canvas: @Composable () -> Unit,
    extraContent: (@Composable () -> Unit)? = null,
) {
    val activePlugin = plugins[activeIndex]

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        ScreenHeader(subtitle = activePlugin.displayName)

        AlgorithmChipRow(
            plugins = plugins,
            activeIndex = activeIndex,
            onSelect = onSelectAlgorithm,
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
            canvas()
        }

        extraContent?.invoke()

        ComplexityCardsRow(complexity = activePlugin.complexity, onCardClick = onComplexityClick)

        ControlsRow(
            playbackStatus = playbackStatus,
            speedIndex = speedIndex,
            onPlay = onPlay,
            onPause = onPause,
            onReset = onReset,
            onReplay = onReplay,
            onSpeedChange = onSpeedChange,
        )
    }
}
