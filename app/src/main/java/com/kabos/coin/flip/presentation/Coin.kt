package com.kabos.coin.flip.presentation

import androidx.compose.animation.core.CubicBezierEasing
import androidx.compose.animation.core.animate
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
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
import androidx.wear.compose.material.MaterialTheme

val frontColor = Color.Red
val backColor = Color.Blue

sealed interface CoinUiState {
    object Loading: CoinUiState

    data class Wait(
        val isFront: Boolean,
        val onClick: () -> Unit,
    ) : CoinUiState

    data class Rotating(
        val isFrontAtInitial: Boolean,
        val isFrontAtEnd: Boolean,
        val onComplete: () -> Unit,
    ) : CoinUiState {
        private val numberOfRotation = (3..4).random()
        private val additionalRotation = if (isFrontAtEnd) 0f else 180f
        val initialRotation = if (isFrontAtInitial) 0f else 180f
        val targetRotation = (360f * numberOfRotation) + additionalRotation
        val durationMillis = 1000 * numberOfRotation
    }
}

@Composable
fun CoinRoute(
    viewModel: CoinViewModel,
) {
    when (val state = viewModel.uiState.collectAsState().value) {
        CoinUiState.Loading -> Unit

        is CoinUiState.Wait -> {
            Circle(
                modifier = Modifier
                    .clickable { state.onClick() },
                color = if (state.isFront) frontColor else backColor
            )
        }

        is CoinUiState.Rotating -> {
            RotatingCoin(
                initialRotation = state.initialRotation,
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
    initialRotation: Float,
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
        easing = CubicBezierEasing(0.22f, 1f, 0.36f, 1f)
    )

    LaunchedEffect(targetRotation) {
        animate(
            initialValue = initialRotation,
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
        color = if (isFront) frontColor else backColor
    )
}

fun isFront(angle: Float): Boolean {
    val value = angle % 360
    return value < 90f || value >= 270f
}

@Composable
private fun Circle(
    modifier: Modifier = Modifier,
    color: Color = frontColor,
) {
    Canvas(
        modifier = modifier
            .fillMaxSize()
            .padding(32.dp),
    ) {
        drawCircle(color = color)
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewFlipCoin() {
    RotatingCoin(
        onComplete = {},
        initialRotation = 0f,
        targetRotation = 720f,
        durationMillis = 5000
    )
}
