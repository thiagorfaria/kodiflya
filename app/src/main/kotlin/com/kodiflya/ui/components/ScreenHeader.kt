package com.kodiflya.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.kodiflya.ui.theme.AccentGreen
import com.kodiflya.ui.theme.TextSecondary

@Composable
fun ScreenHeader(algorithmName: String) {
    Column {
        Text(
            text = "Kodiflya",
            style = MaterialTheme.typography.headlineMedium,
            color = AccentGreen,
        )
        Text(
            text = algorithmName,
            style = MaterialTheme.typography.titleMedium,
            color = TextSecondary,
        )
    }
}
