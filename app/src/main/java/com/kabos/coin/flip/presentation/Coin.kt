package com.kabos.coin.flip.presentation

import androidx.compose.animation.core.CubicBezierEasing
import androidx.compose.animation.core.animate
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

sealed interface CoinState {
    object Wait : CoinState

    data class Rotating(
        val rotationTimes: Int,
        val onComplete: () -> Unit,
    ) : CoinState {
        val targetRotation = 360f * rotationTimes
        val durationMillis  =  1000 * rotationTimes
    }
}

@Composable
fun CoinRoute(
    state: CoinState,
) {
    when (state) {
        CoinState.Wait -> {
            Circle()
        }

        is CoinState.Rotating -> {
            RotatingCoin(
                targetRotation = state.targetRotation,
                durationMillis = state.durationMillis,
                onComplete = {
                    state.onComplete()
                }
            )
        }
    }
}

@Composable
private fun RotatingCoin(
    targetRotation: Float,
    durationMillis: Int,
    onComplete: () -> Unit,
) {
    var currentRotation by remember { mutableFloatStateOf(0f) }
    
    val isFront by remember {
        derivedStateOf { isFront(currentRotation) }
    }
    
    val animationSpec = tween<Float>(
        durationMillis = durationMillis,
        easing = CubicBezierEasing(0.4f, 0.0f, 0.8f, 0.8f)
    )

    LaunchedEffect(targetRotation) {
        animate(
            initialValue = 0f,
            targetValue = targetRotation,
            animationSpec = animationSpec,
        ) { value: Float, _: Float ->
            currentRotation = value
            if (value == targetRotation) {
                onComplete()
            }
        }
    }

    Circle(
        modifier = Modifier
            .graphicsLayer(
                rotationY = currentRotation,
                cameraDistance = 10 * LocalDensity.current.density
            ),
        color = if (isFront) Color.Red else Color.Blue
    )
}

fun isFront(angle: Float): Boolean {
    val value = angle % 360
    return value < 90f || value >= 270f
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
    RotatingCoin(
        onComplete = {},
        targetRotation = 720f,
        durationMillis = 5000
    )
}
