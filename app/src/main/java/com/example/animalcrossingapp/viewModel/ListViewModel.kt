package com.example.animalcrossingapp.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.animalcrossingapp.data.repository.CollectiblesRepository
import com.example.animalcrossingapp.domain.CollectibleType
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class ListViewModel(
    private val repo: CollectiblesRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(
        ListUiState(isLoading = true, isRefreshing = false)
    )
    val uiState = _uiState.asStateFlow()

    private var observeJob: Job? = null
    private var refreshJob: Job? = null

    /**
     * Observa ROOM y ordena:
     * - No donados primero
     * - Donados al final
     */
    fun load(type: CollectibleType) {
        observeJob?.cancel()

        _uiState.value = _uiState.value.copy(isLoading = true, error = null)

        observeJob = viewModelScope.launch {
            try {
                repo.observeByType(type.routeValue).collectLatest { list ->

                    val sorted = list.sortedWith(
                        compareBy<com.example.animalcrossingapp.ui.model.CollectibleUi> { it.isDonated }
                            .thenBy { it.name.lowercase() }
                    )

                    _uiState.value = _uiState.value.copy(
                        items = sorted,
                        isLoading = false,
                        error = null
                    )
                }
            } catch (ce: CancellationException) {
                throw ce
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = e.message ?: "Error cargando datos"
                )
            }
        }
    }

    fun refresh(type: CollectibleType) {
        if (refreshJob?.isActive == true) return

        refreshJob = viewModelScope.launch {
            try {
                _uiState.value = _uiState.value.copy(isRefreshing = true, error = null)
                repo.refresh(type)
                _uiState.value = _uiState.value.copy(isRefreshing = false)
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isRefreshing = false,
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

    override fun onCleared() {
        observeJob?.cancel()
        refreshJob?.cancel()
        super.onCleared()
    }
}
