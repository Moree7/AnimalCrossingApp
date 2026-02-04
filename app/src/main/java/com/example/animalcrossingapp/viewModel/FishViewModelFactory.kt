package com.example.animalcrossingapp.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.animalcrossingapp.data.repository.FishRepository

class FishViewModelFactory(
    private val repo: FishRepository
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(FishViewModel::class.java)) {
            return FishViewModel(repo) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
