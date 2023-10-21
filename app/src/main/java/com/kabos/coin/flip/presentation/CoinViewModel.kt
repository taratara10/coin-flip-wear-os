package com.kabos.coin.flip.presentation

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlin.random.Random

class CoinViewModel : ViewModel() {

    private val _uiState: MutableStateFlow<CoinUiState> = MutableStateFlow(CoinUiState.Loading)
    val uiState: StateFlow<CoinUiState> = _uiState.asStateFlow()

    init {
        setup(isFront = true)
    }

    private fun setup(isFront: Boolean) {
        _uiState.update {
            CoinUiState.Wait(
                isFront = isFront,
                onClick = {
                    flip(displayingSurface = isFront)
                }
            )
        }
    }

    private fun flip(displayingSurface: Boolean) {
        val randomBoolean = Random.nextBoolean()
        _uiState.update {
            CoinUiState.Rotating(
                isFrontAtInitial = displayingSurface,
                isFrontAtEnd = randomBoolean,
                onComplete = {
                    setup(isFront = randomBoolean)
                }
            )
        }
    }
}
