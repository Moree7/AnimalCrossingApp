package com.example.animalcrossingapp.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.animalcrossingapp.data.repository.CollectiblesRepository
import com.example.animalcrossingapp.ui.model.CollectibleUi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch


class DetailViewModel(
    private val repo: CollectiblesRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(DetailUiState())
    val uiState: StateFlow<DetailUiState> = _uiState

    fun load(itemId: String) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)

            runCatching {
                repo.getById(itemId)
            }.onSuccess { item ->
                _uiState.value = DetailUiState(
                    isLoading = false,
                    item = item,
                    error = null
                )
            }.onFailure {
                _uiState.value = DetailUiState(
                    isLoading = false,
                    item = null,
                    error = it.message
                )
            }
        }
    }
}