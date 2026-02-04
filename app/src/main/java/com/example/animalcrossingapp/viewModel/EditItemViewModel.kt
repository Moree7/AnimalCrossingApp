package com.example.animalcrossingapp.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.animalcrossingapp.data.repository.CollectiblesRepository
import com.example.animalcrossingapp.ui.model.CollectibleUi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class EditItemUiState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val item: CollectibleUi? = null
)

class EditItemViewModel(
    private val repo: CollectiblesRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(EditItemUiState())
    val uiState: StateFlow<EditItemUiState> = _uiState.asStateFlow()

    fun load(itemId: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            try {
                val item = repo.getById(itemId)
                _uiState.update { it.copy(isLoading = false, item = item) }
            } catch (e: Exception) {
                _uiState.update { it.copy(isLoading = false, error = e.message ?: "Error cargando item") }
            }
        }
    }

    fun saveEdits(id: String, userName: String?, userSubtitle: String?, userDescription: String?) {
        viewModelScope.launch {
            try {
                repo.updateUserOverrides(id, userName, userSubtitle, userDescription)
                val refreshed = repo.getById(id)
                _uiState.update { it.copy(item = refreshed) }
            } catch (e: Exception) {
                _uiState.update { it.copy(error = e.message ?: "Error guardando cambios") }
            }
        }
    }

    fun restoreDefaults(id: String) {
        saveEdits(id, null, null, null)
    }
}
