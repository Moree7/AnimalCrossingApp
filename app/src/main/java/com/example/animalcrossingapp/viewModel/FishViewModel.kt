package com.example.animalcrossingapp.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.animalcrossingapp.data.repository.FishRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class FishViewModel(
    private val repo: FishRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(FishUiState(isLoading = false))
    val uiState: StateFlow<FishUiState> = _uiState.asStateFlow()

    fun loadFish() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            try {
                val fish = repo.getFish()
                _uiState.value = FishUiState(fish = fish, isLoading = false, error = null)
            } catch (e: Exception) {
                _uiState.value = FishUiState(fish = emptyList(), isLoading = false, error = e.message)
            }
        }
    }
}
