package com.example.animalcrossingapp.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.animalcrossingapp.data.repository.CollectiblesRepository
import com.example.animalcrossingapp.domain.CollectibleType
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ListViewModel(
    private val repo: CollectiblesRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(ListUiState(isLoading = true))
    val uiState: StateFlow<ListUiState> = _uiState.asStateFlow()

    private var observeJob: Job? = null

    fun load(type: CollectibleType) {
        observeJob?.cancel()
        _uiState.value = _uiState.value.copy(isLoading = true, error = null)

        // ✅ AQUÍ está el arreglo: observeByType espera String => type.routeValue
        observeJob = viewModelScope.launch {
            repo.observeByType(type.routeValue).collect { list ->
                _uiState.value = ListUiState(
                    items = list,
                    isLoading = false,
                    error = null
                )
            }
        }
    }

    fun refresh(type: CollectibleType) {
        viewModelScope.launch {
            try {
                _uiState.value = _uiState.value.copy(isLoading = true, error = null)
                repo.refresh(type) // llama API y guarda en Room
                // No hace falta tocar items: el observeByType se actualiza solo
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = e.message ?: "Error refrescando"
                )
            }
        }
    }

    fun setDonated(id: String, donated: Boolean) {
        viewModelScope.launch {
            try {
                repo.setDonated(id, donated)
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    error = e.message ?: "Error actualizando donado"
                )
            }
        }
    }
}
