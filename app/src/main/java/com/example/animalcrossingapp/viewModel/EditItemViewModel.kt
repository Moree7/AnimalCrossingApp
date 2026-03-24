package com.example.animalcrossingapp.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.animalcrossingapp.data.local.CollectibleDbEntity
import com.example.animalcrossingapp.data.repository.CollectiblesRepository
import com.example.animalcrossingapp.ui.model.CollectibleUi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class EditItemUiState(
    val isLoading: Boolean = false,
    val item: CollectibleUi? = null,
    val error: String? = null
)

class EditItemViewModel(
    private val repo: CollectiblesRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(EditItemUiState())
    val uiState: StateFlow<EditItemUiState> = _uiState.asStateFlow()

    private var deletedBackup: CollectibleDbEntity? = null

    fun load(itemId: String) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)

            runCatching {
                repo.getById(itemId)
            }.onSuccess { item ->
                _uiState.value = EditItemUiState(
                    isLoading = false,
                    item = item,
                    error = null
                )
            }.onFailure { e ->
                _uiState.value = EditItemUiState(
                    isLoading = false,
                    item = null,
                    error = e.message ?: "Error al cargar el ítem"
                )
            }
        }
    }

    fun saveItem(
        itemId: String,
        name: String,
        subtitle: String,
        description: String
    ) {
        viewModelScope.launch {
            repo.updateUserOverrides(
                id = itemId,
                userName = name,
                userSubtitle = subtitle,
                userDescription = description
            )
        }
    }

    fun deleteItem(itemId: String, onDeleted: (Boolean) -> Unit) {
        viewModelScope.launch {
            runCatching {
                repo.deleteAndReturnBackup(itemId)
            }.onSuccess { backup ->
                deletedBackup = backup
                onDeleted(backup != null)
            }.onFailure {
                onDeleted(false)
            }
        }
    }

    fun restoreDeletedItem() {
        viewModelScope.launch {
            deletedBackup?.let {
                repo.restoreDeletedItem(it)
                deletedBackup = null
            }
        }
    }
}