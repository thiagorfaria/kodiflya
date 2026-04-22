package com.kodiflya.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.kodiflya.core.plugin.AlgorithmPlugin
import com.kodiflya.ui.theme.AccentGreen
import com.kodiflya.ui.theme.Background
import com.kodiflya.ui.theme.ElementDefault
import com.kodiflya.ui.theme.SurfaceBorder
import com.kodiflya.ui.theme.TextSecondary

@Composable
fun AlgorithmChipRow(
    plugins: List<AlgorithmPlugin>,
    activeIndex: Int,
    onSelect: (Int) -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .horizontalScroll(rememberScrollState()),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        plugins.forEachIndexed { index, plugin ->
            val isActive = index == activeIndex
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(20.dp))
                    .background(if (isActive) AccentGreen else ElementDefault)
                    .border(1.dp, if (isActive) AccentGreen else SurfaceBorder, RoundedCornerShape(20.dp))
                    .clickable { onSelect(index) }
                    .padding(horizontal = 14.dp, vertical = 6.dp),
            ) {
                Text(
                    text = plugin.displayName,
                    style = MaterialTheme.typography.labelMedium,
                    color = if (isActive) Background else TextSecondary,
                )
            }
        }
    }
}
