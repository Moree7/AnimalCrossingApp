package com.example.animalcrossingapp.viewModel

import com.example.animalcrossingapp.ui.model.CollectibleUi

data class DetailUiState(
    val isLoading: Boolean = false,
    val item: CollectibleUi? = null,
    val error: String? = null
)
