package com.example.animalcrossingapp.data.local

import android.content.Context
import androidx.room.Room

object DatabaseModule {

    @Volatile
    private var INSTANCE: AppDatabase? = null

    fun db(context: Context): AppDatabase {
        return INSTANCE ?: synchronized(this) {
            val instance = Room.databaseBuilder(
                context.applicationContext,
                AppDatabase::class.java,
                "animal_crossing.db"
            )
                .fallbackToDestructiveMigration()
                .build()

            INSTANCE = instance
            instance
        }
    }
}
