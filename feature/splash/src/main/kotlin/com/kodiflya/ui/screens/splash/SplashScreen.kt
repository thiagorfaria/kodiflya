package com.kodiflya.ui.screens.splash

import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp
import androidx.compose.runtime.withFrameMillis
import com.kodiflya.ui.component.KodiflyaLogoMark
import com.kodiflya.ui.theme.SpaceMonoFamily
import kotlin.math.PI
import kotlin.math.sin

@Composable
fun SplashScreen(onDone: () -> Unit) {
    var elapsedMs by remember { mutableLongStateOf(0L) }

    LaunchedEffect(Unit) {
        val startTime = withFrameMillis { it }
        while (elapsedMs < 4400L) {
            elapsedMs = withFrameMillis { it } - startTime
        }
        onDone()
    }

    val infiniteTransition = rememberInfiniteTransition(label = "cursor")
    val cursorAlpha by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 0f,
        animationSpec = infiniteRepeatable(tween(525), RepeatMode.Reverse),
        label = "cursor",
    )

    val primary = MaterialTheme.colorScheme.primary
    val onSurface = MaterialTheme.colorScheme.onSurface
    val muted2 = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.6f)

    val e = elapsedMs.toFloat()

    val cornerAlpha = (e / 300f).coerceIn(0f, 1f)
    val promptAlpha = (e / 400f).coerceIn(0f, 1f)
    val typedText = "kodiflya".take(
        ((e - 1000f).coerceAtLeast(0f) / 175f).toInt().coerceIn(0, 8)
    )
    val showCursor = e < 2600f
    val xProgress = ((e - 2400f) / 350f).coerceIn(0f, 1f)
    val xScale = if (xProgress > 0f)
        0.4f + xProgress * 0.65f + sin(xProgress * PI.toFloat()) * 0.2f
    else 0f
    val readyText = "✔ ready".take(
        ((e - 2800f).coerceAtLeast(0f) / 85f).toInt().coerceIn(0, 7)
    )
    val progressFill = (e / 3700f).coerceIn(0f, 1f)
    val hintAlpha = if (e >= 2400f) 0.7f else 0f
    val taglineAlpha = if (e >= 3000f) 0.9f else 0f
    val screenAlpha = if (e < 3700f) 1f else (1f - (e - 3700f) / 400f).coerceIn(0f, 1f)

    Box(
        modifier = Modifier
            .fillMaxSize()
            .graphicsLayer { alpha = screenAlpha }
            .background(MaterialTheme.colorScheme.background)
            .drawBehind {
                drawRect(
                    brush = Brush.radialGradient(
                        colors = listOf(primary.copy(alpha = 0.08f), Color.Transparent),
                        center = Offset(size.width * 0.5f, size.height * 0.38f),
                        radius = size.width * 0.7f,
                    ),
                )
            },
    ) {
        Row(
            modifier = Modifier
                .align(Alignment.TopStart)
                .fillMaxWidth()
                .padding(top = 60.dp, start = 24.dp, end = 24.dp)
                .graphicsLayer { alpha = cornerAlpha },
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            KodiflyaLogoMark(size = 22.dp)
            Text(
                text = "V 0.1",
                style = TextStyle(
                    fontFamily = SpaceMonoFamily,
                    fontWeight = FontWeight.Normal,
                    fontSize = 10.sp,
                    letterSpacing = 0.2.em,
                ),
                color = muted2,
            )
        }

        Column(
            modifier = Modifier
                .align(Alignment.Center)
                .fillMaxWidth()
                .padding(horizontal = 28.dp),
            verticalArrangement = Arrangement.spacedBy(22.dp),
            horizontalAlignment = Alignment.Start,
        ) {
            Row(
                modifier = Modifier.graphicsLayer { alpha = promptAlpha },
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = "~ $ ",
                    style = promptStyle,
                    color = primary.copy(alpha = 0.75f),
                )
                Text(
                    text = typedText,
                    style = promptStyle,
                    color = onSurface,
                )
                if (xProgress > 0f) {
                    Text(
                        text = "(x)",
                        style = promptStyle,
                        color = primary,
                        modifier = Modifier.graphicsLayer {
                            scaleX = xScale
                            scaleY = xScale
                            transformOrigin = TransformOrigin(0f, 0.5f)
                        },
                    )
                }
                if (showCursor) {
                    Box(
                        modifier = Modifier
                            .size(width = 11.dp, height = 22.dp)
                            .graphicsLayer { alpha = cursorAlpha }
                            .background(primary),
                    )
                }
            }

            Row(
                modifier = Modifier.graphicsLayer { alpha = if (e > 2800f) 1f else 0f },
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(text = "> ", style = outputStyle, color = muted2)
                Text(text = readyText, style = outputStyle, color = primary)
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(2.dp)
                    .clip(RoundedCornerShape(1.dp))
                    .background(primary.copy(alpha = 0.12f)),
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth(progressFill)
                        .fillMaxHeight()
                        .background(primary),
                )
            }

            Text(
                text = "see the algorithm",
                style = taglineStyle,
                color = muted2,
                modifier = Modifier.graphicsLayer { alpha = taglineAlpha },
            )
        }

        Text(
            text = "compile your intuition",
            style = bottomHintStyle,
            color = muted2,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 50.dp)
                .graphicsLayer { alpha = hintAlpha },
        )
    }
}

private val promptStyle = TextStyle(
    fontFamily = SpaceMonoFamily,
    fontWeight = FontWeight.Normal,
    fontSize = 22.sp,
    letterSpacing = (-0.01).em,
)

private val outputStyle = TextStyle(
    fontFamily = SpaceMonoFamily,
    fontWeight = FontWeight.Normal,
    fontSize = 14.sp,
    letterSpacing = 0.02.em,
)

private val taglineStyle = TextStyle(
    fontFamily = SpaceMonoFamily,
    fontWeight = FontWeight.Normal,
    fontSize = 11.sp,
    letterSpacing = 0.18.em,
)

private val bottomHintStyle = TextStyle(
    fontFamily = SpaceMonoFamily,
    fontWeight = FontWeight.Normal,
    fontSize = 10.sp,
    letterSpacing = 0.16.em,
)
