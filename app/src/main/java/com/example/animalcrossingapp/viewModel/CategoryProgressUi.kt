package com.example.animalcrossingapp.viewModel

import com.example.animalcrossingapp.domain.CollectibleType

data class ProgressSummaryUi(
    val donated: Int = 0,
    val total: Int = 0,
    val percent: Int = 0
)

data class CategoryProgressUi(
    val type: CollectibleType,
    val title: String,
    val progress: ProgressSummaryUi
)