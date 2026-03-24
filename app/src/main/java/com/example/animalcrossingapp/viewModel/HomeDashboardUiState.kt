package com.example.animalcrossingapp.viewModel

data class HomeDashboardUiState(
    val total: Int = 0,
    val donated: Int = 0,
    val percentage: Int = 0,
    val isLoading: Boolean = false,
    val achievements: List<AchievementUi> = emptyList(),
    val overall: ProgressSummaryUi = ProgressSummaryUi(),
    val categories: List<CategoryProgressUi> = emptyList()
)