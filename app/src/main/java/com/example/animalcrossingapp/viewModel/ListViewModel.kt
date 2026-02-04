package com.example.animalcrossingapp.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.animalcrossingapp.data.repository.CollectiblesRepository
import com.example.animalcrossingapp.domain.CollectibleType
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class ListViewModel(
    private val repo: CollectiblesRepository
) : ViewModel() {

    // Estado UI:
    // - isLoading: se usa solo hasta que Room emite la primera lista.
    // - isRefreshing: se usa para refresco de red (pull-to-refresh / auto-refresh).
    private val _uiState = MutableStateFlow(
        ListUiState(isLoading = true, isRefreshing = false)
    )

    // ✅ Tipado explícito: evita inferencias raras en Compose
    val uiState: StateFlow<ListUiState> = _uiState.asStateFlow()

    // Job de observación de Room (se cancela al cambiar de categoría)
    private var observeJob: Job? = null

    // Job de refresco de red (evita refresh simultáneos)
    private var refreshJob: Job? = null

    /**
     * load() = SOLO observar Room.
     * No llama a red.
     */
    fun load(type: CollectibleType) {
        observeJob?.cancel()

        // “Loading” solo hasta la primera emisión de Room
        _uiState.value = _uiState.value.copy(isLoading = true, error = null)

        observeJob = viewModelScope.launch {
            try {
                repo.observeByType(type.routeValue).collectLatest { list ->
                    _uiState.value = _uiState.value.copy(
                        items = list,
                        isLoading = false,
                        error = null
                    )
                }
            } catch (ce: CancellationException) {
                // Normal al cambiar de pantalla/tipo
                throw ce
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = e.message ?: "Error observando datos"
                )
            }
        }
    }

    /**
     * refresh() = SOLO red + upsert en Room.
     * Room actualizará la lista automáticamente.
     */
    fun refresh(type: CollectibleType) {
        if (refreshJob?.isActive == true) return

        refreshJob = viewModelScope.launch {
            try {
                _uiState.value = _uiState.value.copy(isRefreshing = true, error = null)
                repo.refresh(type)
                _uiState.value = _uiState.value.copy(isRefreshing = false)
            } catch (ce: CancellationException) {
                _uiState.value = _uiState.value.copy(isRefreshing = false)
                throw ce
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
