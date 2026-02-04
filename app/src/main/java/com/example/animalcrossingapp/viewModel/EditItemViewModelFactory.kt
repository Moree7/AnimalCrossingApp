package com.example.animalcrossingapp.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.animalcrossingapp.data.repository.CollectiblesRepository

class EditItemViewModelFactory(
    private val repo: CollectiblesRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(EditItemViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return EditItemViewModel(repo) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
