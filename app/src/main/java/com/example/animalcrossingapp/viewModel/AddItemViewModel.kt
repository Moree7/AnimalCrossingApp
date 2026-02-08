package com.example.animalcrossingapp.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.animalcrossingapp.data.repository.CollectiblesRepository
import com.example.animalcrossingapp.domain.CollectibleType
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AddItemViewModel(
    private val repo: CollectiblesRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(AddItemUiState())
    val uiState: StateFlow<AddItemUiState> = _uiState.asStateFlow()

    /**
     * typeRoute viene de la navegación: "Peces", "Bichos", etc.
     * Aquí lo convertimos a CollectibleType.
     */
    fun save(
        typeRoute: String,
        name: String,
        subtitle: String,
        description: String,
        onDone: () -> Unit
    ) {
        viewModelScope.launch {
            _uiState.value = AddItemUiState(isSaving = true)

            try {
                val type = CollectibleType.fromRoute(typeRoute)

                // OJO: aquí NO existe "typeRoute = ..."; el repo pide "type = CollectibleType"
                repo.addManualItem(
                    type = type,
                    name = name,
                    subtitle = subtitle,
                    description = description
                )

                _uiState.value = AddItemUiState(isSaving = false)
                onDone()
            } catch (e: Exception) {
                _uiState.value = AddItemUiState(
                    isSaving = false,
                    error = e.message ?: "Error guardando el item"
                )
            }
        }
    }

    fun clearError() {
        _uiState.value = _uiState.value.copy(error = null)
    }
}
