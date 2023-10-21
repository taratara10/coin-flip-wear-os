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
                onClick = ::flip
            )
        }
    }

    private fun flip() {
        val randomBoolean = Random.nextBoolean()
        _uiState.update {
            CoinUiState.Rotating(
                isFront = randomBoolean,
                onComplete = {
                    setup(isFront = randomBoolean)
                }
            )
        }
    }
}
