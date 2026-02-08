package com.example.animalcrossingapp.ui.common

import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun LiquidGradientBackground(
    modifier: Modifier = Modifier,
    colorA: Color,
    colorB: Color
) {
    val t = rememberInfiniteTransition(label = "liquid")

    val phase by t.animateFloat(
        initialValue = 0f,
        targetValue = (Math.PI * 2.0).toFloat(),
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 4200),
            repeatMode = RepeatMode.Restart
        ),
        label = "phase"
    )

    val wobble by t.animateFloat(
        initialValue = 0.85f,
        targetValue = 1.15f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 2600),
            repeatMode = RepeatMode.Reverse
        ),
        label = "wobble"
    )

    Canvas(modifier = modifier) {
        val w = size.width
        val h = size.height

        // Dos focos que se “mueven” para efecto líquido
        val c1 = Offset(
            x = w * (0.50f + 0.22f * cos(phase)),
            y = h * (0.45f + 0.18f * sin(phase))
        )
        val c2 = Offset(
            x = w * (0.50f + 0.22f * cos(phase + 1.7f)),
            y = h * (0.55f + 0.18f * sin(phase + 1.7f))
        )

        val brush = Brush.radialGradient(
            colors = listOf(
                colorA.copy(alpha = 0.95f),
                colorB.copy(alpha = 0.90f),
                colorA.copy(alpha = 0.75f)
            ),
            center = c1,
            radius = (w.coerceAtLeast(h) * 0.85f) * wobble
        )

        val brush2 = Brush.radialGradient(
            colors = listOf(
                colorB.copy(alpha = 0.70f),
                colorA.copy(alpha = 0.55f),
                Color.Transparent
            ),
            center = c2,
            radius = (w.coerceAtLeast(h) * 0.95f) * (2f - wobble)
        )

        drawRect(brush = brush)
        drawRect(brush = brush2)
    }
}
