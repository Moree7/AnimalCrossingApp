package com.example.animalcrossingapp.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "collectibles")
data class CollectibleDbEntity(
    @PrimaryKey val id: String,            // "Peces|anchovy" (name EN)
    val name: String,                      // EN (API)
    val subtitle: String,                  // EN (API)
    val typeRoute: String,                 // "Peces", "Bichos", ...
    val description: String,               // EN (API)
    val isDonated: Boolean,                // persistente

    // Overrides del usuario (pueden ser null)
    val userName: String? = null,
    val userSubtitle: String? = null,
    val userDescription: String? = null
)
