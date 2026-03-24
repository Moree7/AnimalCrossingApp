package com.example.animalcrossingapp.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "collectibles")
data class CollectibleDbEntity(
    @PrimaryKey val id: String,
    val name: String,
    val subtitle: String,
    val typeRoute: String,
    val description: String,
    val isDonated: Boolean,

    val userName: String? = null,
    val userSubtitle: String? = null,
    val userDescription: String? = null,

    // nuevo campo para orden manual
    val displayOrder: Int = 0
)