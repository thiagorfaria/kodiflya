package com.kodiflya.ui.home

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.kodiflya.core.plugin.AlgorithmPlugin
import com.kodiflya.core.plugin.Category
import com.kodiflya.ui.components.ComplexityCardsRow
import com.kodiflya.ui.theme.AccentGreen
import com.kodiflya.ui.theme.Background
import com.kodiflya.ui.theme.ElementDefault
import com.kodiflya.ui.theme.MetricLabel
import com.kodiflya.ui.theme.SpaceMonoFamily
import com.kodiflya.ui.theme.Surface
import com.kodiflya.ui.theme.SurfaceBorder
import com.kodiflya.ui.theme.TextPrimary
import com.kodiflya.ui.theme.TextSecondary
import java.util.Calendar

@Composable
fun HomeScreen(
    onNavigate: (Category) -> Unit,
    viewModel: HomeViewModel = hiltViewModel(),
) {
    val greeting = remember { timeGreeting() }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Background)
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        HomeHeader(greeting = greeting)

        viewModel.categories.forEach { summary ->
            CategoryCard(
                summary = summary,
                onTap = { onNavigate(summary.category) },
            )
        }

        ComplexitySpotlightCard(
            plugin = viewModel.spotlightPlugin,
            onTap = { onNavigate(viewModel.spotlightPlugin.category) },
        )

        Spacer(modifier = Modifier.height(4.dp))
    }
}

@Composable
private fun HomeHeader(greeting: String) {
    Column {
        Text(
            text = greeting,
            style = MaterialTheme.typography.labelSmall,
            color = TextSecondary,
        )
        Text(
            text = "Kodiflya",
            style = TextStyle(
                fontFamily = SpaceMonoFamily,
                fontWeight = FontWeight.Bold,
                fontSize = 26.sp,
                lineHeight = 34.sp,
            ),
            color = AccentGreen,
        )
    }
}

@Composable
private fun CategoryCard(
    summary: HomeViewModel.CategorySummary,
    onTap: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(Surface)
            .border(1.dp, SurfaceBorder, RoundedCornerShape(12.dp))
            .clickable(onClick = onTap)
            .padding(horizontal = 16.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
            Text(
                text = summary.displayName,
                style = MaterialTheme.typography.titleMedium,
                color = TextPrimary,
            )
            Text(
                text = "${summary.algorithmCount} algorithms",
                style = MaterialTheme.typography.labelSmall,
                color = MetricLabel,
            )
        }

        Box(
            modifier = Modifier
                .size(width = 80.dp, height = 48.dp)
                .clip(RoundedCornerShape(6.dp))
                .background(ElementDefault.copy(alpha = 0.4f)),
        ) {
            when (summary.category) {
                Category.SORTING -> SortingMiniViz(modifier = Modifier.padding(6.dp))
                Category.GRAPH   -> GraphMiniViz(modifier = Modifier.padding(6.dp))
                Category.TREES   -> TreeMiniViz(modifier = Modifier.padding(6.dp))
            }
        }
    }
}

@Composable
private fun ComplexitySpotlightCard(
    plugin: AlgorithmPlugin,
    onTap: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(Surface)
            .border(1.dp, SurfaceBorder, RoundedCornerShape(12.dp))
            .clickable(onClick = onTap)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            Text(
                text = plugin.displayName,
                style = MaterialTheme.typography.titleMedium,
                color = TextPrimary,
            )
            CategoryChip(category = plugin.category)
        }

        ComplexityCardsRow(complexity = plugin.complexity)

        Text(
            text = "See it run →",
            style = MaterialTheme.typography.labelSmall,
            color = AccentGreen,
        )
    }
}

@Composable
private fun CategoryChip(category: Category) {
    val label = when (category) {
        Category.SORTING -> "SORT"
        Category.GRAPH   -> "GRAPH"
        Category.TREES   -> "TREES"
    }
    Text(
        text = label,
        style = TextStyle(
            fontFamily = SpaceMonoFamily,
            fontWeight = FontWeight.Normal,
            fontSize = 11.sp,
            lineHeight = 16.sp,
        ),
        color = AccentGreen,
        modifier = Modifier
            .clip(RoundedCornerShape(4.dp))
            .background(AccentGreen.copy(alpha = 0.12f))
            .padding(horizontal = 6.dp, vertical = 2.dp),
    )
}

private fun timeGreeting(): String {
    val hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY)
    return when {
        hour < 12 -> "Good morning"
        hour < 18 -> "Good afternoon"
        else      -> "Good evening"
    }
}
