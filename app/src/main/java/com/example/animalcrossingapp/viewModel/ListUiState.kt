package com.example.animalcrossingapp.viewModel

import com.example.animalcrossingapp.ui.model.CollectibleUi

data class ListUiState(
    val items: List<CollectibleUi> = emptyList(),
    val isLoading: Boolean = false,      // “esperando primera emisión de Room”
    val isRefreshing: Boolean = false,   // “refresco de red” pull-to-refresh
    val error: String? = null
)
