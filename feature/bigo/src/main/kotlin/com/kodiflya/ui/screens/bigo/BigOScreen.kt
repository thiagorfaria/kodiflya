package com.kodiflya.ui.screens.bigo

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kodiflya.core.plugin.BigO
import com.kodiflya.ui.component.ScreenHeader
import com.kodiflya.ui.component.toColor
import com.kodiflya.ui.theme.SpaceMonoFamily

@Composable
fun BigOScreen(highlightBigO: BigO? = null) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        ScreenHeader(subtitle = "Big-O Reference")

        bigOEntries.forEach { info ->
            BigOCard(info = info, highlighted = info.bigO == highlightBigO)
        }
    }
}

@Composable
private fun BigOCard(info: BigOInfo, highlighted: Boolean) {
    val accentColor = info.bigO.colorRole.toColor()
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(
                if (highlighted) accentColor.copy(alpha = 0.08f)
                else MaterialTheme.colorScheme.surface,
            )
            .border(
                width = if (highlighted) 1.5.dp else 1.dp,
                color = if (highlighted) accentColor else MaterialTheme.colorScheme.outline,
                shape = RoundedCornerShape(12.dp),
            )
            .padding(horizontal = 14.dp, vertical = 12.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = info.bigO.label,
                style = TextStyle(
                    fontFamily = SpaceMonoFamily,
                    fontWeight = FontWeight.Bold,
                    fontSize = 22.sp,
                    lineHeight = 28.sp,
                ),
                color = accentColor,
            )
            TierBadge(tier = info.tier, accentColor = accentColor)
        }

        Text(
            text = info.name.uppercase(),
            style = TextStyle(
                fontFamily = SpaceMonoFamily,
                fontWeight = FontWeight.Normal,
                fontSize = 13.sp,
                lineHeight = 18.sp,
            ),
            color = MaterialTheme.colorScheme.outlineVariant,
        )

        HorizontalDivider(
            color = MaterialTheme.colorScheme.outline.copy(alpha = 0.5f),
            thickness = 0.5.dp,
        )

        Text(
            text = info.description,
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )

        LabeledRow(
            label = "LIKE",
            content = info.analogy,
            accentColor = accentColor,
        )

        if (info.examples.isNotEmpty()) {
            LabeledRow(
                label = "ALGOS",
                content = info.examples.joinToString(" · "),
                accentColor = accentColor,
                contentColor = accentColor,
            )
        }
    }
}

@Composable
private fun TierBadge(tier: String, accentColor: Color) {
    Text(
        text = tier,
        style = TextStyle(
            fontFamily = SpaceMonoFamily,
            fontWeight = FontWeight.Normal,
            fontSize = 10.sp,
            lineHeight = 14.sp,
        ),
        color = accentColor,
        modifier = Modifier
            .clip(RoundedCornerShape(4.dp))
            .background(MaterialTheme.colorScheme.background)
            .border(1.dp, accentColor.copy(alpha = 0.4f), RoundedCornerShape(4.dp))
            .padding(horizontal = 8.dp, vertical = 4.dp),
    )
}

@Composable
private fun LabeledRow(
    label: String,
    content: String,
    accentColor: Color,
    contentColor: Color = MaterialTheme.colorScheme.onSurfaceVariant,
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.Top,
    ) {
        Text(
            text = label,
            style = TextStyle(
                fontFamily = SpaceMonoFamily,
                fontWeight = FontWeight.Normal,
                fontSize = 12.sp,
                lineHeight = 22.sp,
            ),
            color = accentColor.copy(alpha = 0.6f),
        )
        Text(
            text = content,
            style = MaterialTheme.typography.labelMedium,
            color = contentColor,
        )
    }
}
