package com.example.animalcrossingapp.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.animalcrossingapp.data.repository.CollectiblesRepository

class HomeDashboardViewModelFactory(
    private val repo: CollectiblesRepository
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HomeDashboardViewModel::class.java)) {
            return HomeDashboardViewModel(repo) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}