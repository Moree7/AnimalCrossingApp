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

    private val _uiState = MutableStateFlow(DetailUiState(isLoading = true))
    val uiState: StateFlow<DetailUiState> = _uiState.asStateFlow()

    fun load(id: String) {
        viewModelScope.launch {
            _uiState.value = DetailUiState(isLoading = true)
            try {
                val item = repo.getById(id)
                _uiState.value = DetailUiState(item = item, isLoading = false)
            } catch (e: Exception) {
                _uiState.value = DetailUiState(error = e.message, isLoading = false)
            }
        }
    }

    fun setDonated(donated: Boolean) {
        val id = _uiState.value.item?.id ?: return
        viewModelScope.launch {
            repo.setDonated(id, donated)
            val updated = repo.getById(id)
            _uiState.value = _uiState.value.copy(item = updated)
        }
    }

    fun delete(onDone: () -> Unit) {
        val id = _uiState.value.item?.id ?: return
        viewModelScope.launch {
            repo.deleteById(id)
            onDone()
        }
    }
}
