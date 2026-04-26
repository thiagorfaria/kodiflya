package com.kodiflya.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.kodiflya.core.plugin.PlaybackStatus

val speedLevels = listOf(0.5f, 1f, 2f, 4f, 8f)
val speedLabels = listOf("0.5×", "1×", "2×", "4×", "8×")

@Composable
fun ControlsRow(
    playbackStatus: PlaybackStatus,
    speedIndex: Float,
    onPlay: () -> Unit,
    onPause: () -> Unit,
    onReset: () -> Unit,
    onSpeedChange: (Float) -> Unit,
) {
    val isPlaying = playbackStatus == PlaybackStatus.PLAYING

    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        Box(
            modifier = Modifier
                .size(48.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.primary)
                .clickable(onClick = if (isPlaying) onPause else onPlay),
            contentAlignment = Alignment.Center,
        ) {
            Icon(
                imageVector = if (isPlaying) KodiflyaIcons.Pause else KodiflyaIcons.Play,
                contentDescription = if (isPlaying) "Pause" else "Play",
                tint = MaterialTheme.colorScheme.background,
                modifier = Modifier.size(24.dp),
            )
        }

        Column(modifier = Modifier.weight(1f)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                Text("0.5×", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.outlineVariant)
                Text(
                    speedLabels[speedIndex.toInt().coerceIn(0, speedLabels.lastIndex)],
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.primary,
                )
                Text("8×", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.outlineVariant)
            }
            Slider(
                value = speedIndex,
                onValueChange = onSpeedChange,
                valueRange = 0f..4f,
                steps = 3,
                colors = SliderDefaults.colors(
                    thumbColor = MaterialTheme.colorScheme.primary,
                    activeTrackColor = MaterialTheme.colorScheme.primary,
                    inactiveTrackColor = MaterialTheme.colorScheme.surfaceVariant,
                ),
            )
        }

        Box(
            modifier = Modifier
                .size(48.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.surfaceVariant)
                .clickable(onClick = onReset),
            contentAlignment = Alignment.Center,
        ) {
            Icon(
                imageVector = KodiflyaIcons.Reset,
                contentDescription = "Reset",
                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.size(24.dp),
            )
        }
    }
}
