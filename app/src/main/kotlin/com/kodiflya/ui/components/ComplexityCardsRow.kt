package com.kodiflya.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kodiflya.core.plugin.Complexity
import com.kodiflya.ui.theme.AccentAmber
import com.kodiflya.ui.theme.AccentGreen
import com.kodiflya.ui.theme.SpaceMonoFamily
import com.kodiflya.ui.theme.Surface
import com.kodiflya.ui.theme.SurfaceBorder
import com.kodiflya.ui.theme.TextSecondary
import com.kodiflya.ui.theme.MetricLabel as MetricLabelColor

@Composable
fun ComplexityCardsRow(complexity: Complexity) {
    val cards = listOf(
        Triple("Best", complexity.bestCase, AccentGreen),
        Triple("Avg", complexity.averageCase, AccentAmber),
        Triple("Worst", complexity.worstCase, AccentAmber),
        Triple("Space", complexity.spaceComplexity, TextSecondary),
    )
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        cards.forEach { (category, notation, accentColor) ->
            ComplexityCard(
                notation = notation,
                category = category,
                accentColor = accentColor,
                modifier = Modifier.weight(1f),
            )
        }
    }
}

@Composable
fun ComplexityCard(
    notation: String,
    category: String,
    accentColor: Color,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(8.dp))
            .background(Surface)
            .border(1.dp, SurfaceBorder, RoundedCornerShape(8.dp))
            .padding(horizontal = 6.dp, vertical = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = notation,
            style = TextStyle(
                fontFamily = SpaceMonoFamily,
                fontWeight = FontWeight.Normal,
                fontSize = 13.sp,
                lineHeight = 18.sp,
            ),
            color = accentColor,
            textAlign = TextAlign.Center,
        )
        Spacer(modifier = Modifier.height(2.dp))
        Text(
            text = category,
            style = MaterialTheme.typography.labelSmall,
            color = MetricLabelColor,
            textAlign = TextAlign.Center,
        )
    }
}
