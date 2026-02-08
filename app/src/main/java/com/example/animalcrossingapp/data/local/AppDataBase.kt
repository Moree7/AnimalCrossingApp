package com.example.animalcrossingapp.data.local

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [CollectibleDbEntity::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun collectibleDao(): CollectibleDao
}
