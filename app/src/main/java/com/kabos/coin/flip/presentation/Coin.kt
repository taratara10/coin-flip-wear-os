package com.kabos.coin.flip.presentation

import androidx.compose.animation.core.CubicBezierEasing
import androidx.compose.animation.core.animate
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun FlipCoin() {
    var doFlip by remember { mutableStateOf(false) }
    var flipRotation by remember { mutableFloatStateOf(0f) }
    val animationSpec = tween<Float>(3000, easing = CubicBezierEasing(0.4f, 0.0f, 0.8f, 0.8f))

    LaunchedEffect(doFlip) {
        animate(
            initialValue = 0f,
            targetValue = 360f,
            animationSpec = animationSpec,
        ) { value: Float, _: Float ->
            flipRotation = value
        }
    }

    val modifier = Modifier
        .graphicsLayer(
            rotationY = flipRotation,
            cameraDistance = 10 * LocalDensity.current.density
        )

    Circle(
        modifier = modifier
            .clickable {
                       doFlip = !doFlip
            },
        color = when {
            flipRotation < 90f -> Color.Red
            flipRotation < 180f -> Color.Blue
            flipRotation < 270f -> Color.Blue
            else -> Color.Red
        }
    )
}

// todo material Youのcolorとか使える？
@Composable
private fun Circle(
    modifier: Modifier = Modifier,
    color: Color = Color.Red,
) {
    Canvas(
        modifier = modifier.size(50.dp),
    ) {
        drawCircle(color = color)
    }
}

@Preview(showBackground = true)
@Composable
private fun PreviewCircle() {
    Circle(modifier = Modifier.size(50.dp))
}

@Preview
@Composable
fun PreviewFlipCoin() {
    FlipCoin()
}
