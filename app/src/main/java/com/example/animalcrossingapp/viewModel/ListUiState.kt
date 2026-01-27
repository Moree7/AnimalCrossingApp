package com.example.animalcrossingapp.viewModel

import com.example.animalcrossingapp.ui.model.CollectibleUi

data class ListUiState(
    val items: List<CollectibleUi> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)
