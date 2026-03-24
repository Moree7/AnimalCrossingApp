package com.example.animalcrossingapp.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.animalcrossingapp.data.local.CollectibleDbEntity
import com.example.animalcrossingapp.data.repository.CollectiblesRepository
import com.example.animalcrossingapp.domain.CollectibleType
import com.example.animalcrossingapp.ui.model.CollectibleUi
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class ListViewModel(
    private val repo: CollectiblesRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(ListUiState())
    val uiState: StateFlow<ListUiState> = _uiState.asStateFlow()

    private var observeJob: Job? = null
    private var deletedBackup: CollectibleDbEntity? = null
    private var currentType: CollectibleType? = null
    private var hasAutoRefreshed = false

    fun load(type: CollectibleType) {
        currentType = type
        observeJob?.cancel()

        _uiState.value = _uiState.value.copy(
            isLoading = true,
            error = null
        )

        observeJob = viewModelScope.launch {
            repo.observeByType(type.routeValue).collectLatest { list ->
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    items = list,
                    error = null
                )

                // Si la BD está vacía tras abrir pantalla, cargamos desde API una sola vez
                if (list.isEmpty() && !hasAutoRefreshed) {
                    hasAutoRefreshed = true
                    refresh(type)
                }
            }
        }
    }

    fun refresh(type: CollectibleType) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                isLoading = true,
                error = null
            )

            runCatching {
                repo.refresh(type)
            }.onFailure { e ->
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = e.message ?: "Error refrescando datos"
                )
            }
        }
    }

    fun retryRefresh() {
        currentType?.let { refresh(it) }
    }

    fun setDonated(id: String, donated: Boolean) {
        viewModelScope.launch {
            repo.setDonated(id, donated)
        }
    }

    fun deleteItem(id: String, onDone: (Boolean) -> Unit) {
        viewModelScope.launch {
            runCatching {
                repo.deleteAndReturnBackup(id)
            }.onSuccess { backup ->
                deletedBackup = backup
                onDone(backup != null)
            }.onFailure {
                onDone(false)
            }
        }
    }

    fun restoreLastDeletedItem() {
        viewModelScope.launch {
            deletedBackup?.let {
                repo.restoreDeletedItem(it)
                deletedBackup = null
            }
        }
    }

    fun reorderItems(items: List<CollectibleUi>) {
        viewModelScope.launch {
            repo.reorderItems(items)
        }
    }
}