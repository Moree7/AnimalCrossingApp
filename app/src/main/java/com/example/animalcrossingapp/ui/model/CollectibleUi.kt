package com.example.animalcrossingapp.ui.model

import com.example.animalcrossingapp.domain.CollectibleType

data class CollectibleUi(
    val id: String,

    val name: String,
    val subtitle: String,
    val description: String,

    val type: CollectibleType,
    val isDonated: Boolean,

    val originalName: String,
    val originalSubtitle: String,
    val originalDescription: String,

    val userName: String?,
    val userSubtitle: String?,
    val userDescription: String?,

    val imageResId: Int? = null,
    val displayOrder: Int = 0
)