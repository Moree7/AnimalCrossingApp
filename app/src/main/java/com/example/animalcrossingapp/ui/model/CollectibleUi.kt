package com.example.animalcrossingapp.ui.model

import com.example.animalcrossingapp.domain.CollectibleType

data class CollectibleUi(
    val id: String,

    // “Display” (ya traducido/offline y/o override)
    val name: String,
    val subtitle: String,
    val description: String,

    val type: CollectibleType,
    val isDonated: Boolean,

    // Original EN (DB)
    val originalName: String,
    val originalSubtitle: String,
    val originalDescription: String,

    // Overrides (lo que el usuario escribió)
    val userName: String?,
    val userSubtitle: String?,
    val userDescription: String?
)
