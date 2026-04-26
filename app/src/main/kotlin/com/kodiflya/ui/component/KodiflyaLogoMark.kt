package com.kodiflya.ui.component

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/*
 * Terminal f(x) mark — 24×24 viewBox.
 * Elements (all coordinates in viewBox units, scaled to render size):
 *   - Tile:      rounded square background, rx=5.4
 *   - Cursor:    solid rect x=6.4 y=5.5 w=2.6 h=13 rx=0.6
 *   - Caret:     M17 6.2 L11 12 L17 17.8, stroke 2.6, round caps
 *   - Baseline:  thin rect x=5 y=20.6 w=14 h=0.9 rx=0.45, 35% opacity
 */
@Composable
fun KodiflyaLogoMark(
    modifier: Modifier = Modifier,
    size: Dp = 40.dp,
) {
    val tile = MaterialTheme.colorScheme.background
    val sage = MaterialTheme.colorScheme.primary

    Canvas(modifier = modifier.size(size)) {
        val s = this.size.width / 24f

        drawRoundRect(
            color = tile,
            size = Size(this.size.width, this.size.height),
            cornerRadius = CornerRadius(5.4f * s),
        )

        drawRoundRect(
            color = sage,
            topLeft = Offset(6.4f * s, 5.5f * s),
            size = Size(2.6f * s, 13f * s),
            cornerRadius = CornerRadius(0.6f * s),
        )

        val caret = Path().apply {
            moveTo(17f * s, 6.2f * s)
            lineTo(11f * s, 12f * s)
            lineTo(17f * s, 17.8f * s)
        }
        drawPath(
            path = caret,
            color = sage,
            style = Stroke(width = 2.6f * s, cap = StrokeCap.Round, join = StrokeJoin.Round),
        )

        drawRoundRect(
            color = sage.copy(alpha = 0.35f),
            topLeft = Offset(5f * s, 20.6f * s),
            size = Size(14f * s, 0.9f * s),
            cornerRadius = CornerRadius(0.45f * s),
        )
    }
}
