package com.example.animalcrossingapp.viewModel

import com.example.animalcrossingapp.data.remote.NookFishDto

data class FishUiState(
    val fish: List<NookFishDto> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)
