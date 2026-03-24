package com.example.animalcrossingapp.viewModel

import com.example.animalcrossingapp.ui.model.CollectibleUi

data class ListUiState(
    val isLoading: Boolean = false,
    val items: List<CollectibleUi> = emptyList(),
    val error: String? = null
)