package com.kodiflya.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
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
import com.kodiflya.core.plugin.BigO
import com.kodiflya.core.plugin.Complexity
import com.kodiflya.ui.theme.SpaceMonoFamily
import com.kodiflya.ui.component.toColor

@Composable
fun ComplexityCardsRow(
    complexity: Complexity,
    onCardClick: ((BigO) -> Unit)? = null,
) {
    val cards = listOf(
        Triple("Best",  complexity.bestCase,        complexity.bestCase.colorRole.toColor()),
        Triple("Avg",   complexity.averageCase,      complexity.averageCase.colorRole.toColor()),
        Triple("Worst", complexity.worstCase,        complexity.worstCase.colorRole.toColor()),
        Triple("Space", complexity.spaceComplexity,  complexity.spaceComplexity.colorRole.toColor()),
    )
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .horizontalScroll(rememberScrollState()),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        cards.forEach { (caseLabel, bigO, accentColor) ->
            ComplexityCard(
                notation = bigO.label,
                category = caseLabel,
                accentColor = accentColor,
                onClick = onCardClick?.let { callback -> { callback(bigO) } },
            )
        }
    }
}

@Composable
fun ComplexityCard(
    notation: String,
    category: String,
    accentColor: Color,
    onClick: (() -> Unit)? = null,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .widthIn(min = 68.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(MaterialTheme.colorScheme.surface)
            .border(1.dp, MaterialTheme.colorScheme.outline, RoundedCornerShape(8.dp))
            .then(if (onClick != null) Modifier.clickable(onClick = onClick) else Modifier)
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
            color = MaterialTheme.colorScheme.outlineVariant,
            textAlign = TextAlign.Center,
        )
    }
}
