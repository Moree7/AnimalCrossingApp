package com.example.animalcrossingapp.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface CollectibleDao {

    @Query("""
        SELECT * FROM collectibles
        WHERE typeRoute = :typeRoute
        ORDER BY isDonated ASC, displayOrder ASC, name ASC
    """)
    fun observeByType(typeRoute: String): Flow<List<CollectibleDbEntity>>

    @Query("SELECT * FROM collectibles WHERE id = :id LIMIT 1")
    suspend fun getById(id: String): CollectibleDbEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(entity: CollectibleDbEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertAll(entities: List<CollectibleDbEntity>)

    @Query("DELETE FROM collectibles WHERE typeRoute = :typeRoute")
    suspend fun clearType(typeRoute: String)

    @Query("UPDATE collectibles SET isDonated = :donated WHERE id = :id")
    suspend fun setDonated(id: String, donated: Boolean)

    @Query("""
        UPDATE collectibles
        SET userName = :userName,
            userSubtitle = :userSubtitle,
            userDescription = :userDescription
        WHERE id = :id
    """)
    suspend fun updateUserOverrides(
        id: String,
        userName: String?,
        userSubtitle: String?,
        userDescription: String?
    )

    @Query("DELETE FROM collectibles WHERE id = :id")
    suspend fun deleteById(id: String)

    @Query("UPDATE collectibles SET displayOrder = :order WHERE id = :id")
    suspend fun updateDisplayOrder(id: String, order: Int)

    @Query("SELECT * FROM collectibles WHERE typeRoute = :typeRoute")
    suspend fun getAllByType(typeRoute: String): List<CollectibleDbEntity>

    @Query("""
        SELECT * FROM collectibles
        WHERE typeRoute = :typeRoute
        AND (userName IS NOT NULL OR userSubtitle IS NOT NULL OR userDescription IS NOT NULL)
    """)
    suspend fun getUserOverridesByType(typeRoute: String): List<CollectibleDbEntity>

    @Query("SELECT id FROM collectibles WHERE typeRoute = :typeRoute AND isDonated = 1")
    suspend fun getDonatedIdsByType(typeRoute: String): List<String>
}