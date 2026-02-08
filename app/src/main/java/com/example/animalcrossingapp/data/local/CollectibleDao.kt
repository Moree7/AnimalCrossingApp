package com.example.animalcrossingapp.data.local

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface CollectibleDao {

    // LIST
    @Query("""
        SELECT * FROM collectibles
        WHERE typeRoute = :typeRoute
        ORDER BY name COLLATE NOCASE ASC
    """)
    fun observeByType(typeRoute: String): Flow<List<CollectibleDbEntity>>

    @Query("""
        SELECT * FROM collectibles
        WHERE id = :id
        LIMIT 1
    """)
    suspend fun getById(id: String): CollectibleDbEntity?

    // UPSERT
    @Upsert
    suspend fun upsert(entity: CollectibleDbEntity)

    @Upsert
    suspend fun upsertAll(list: List<CollectibleDbEntity>)

    // DONATED
    @Query("UPDATE collectibles SET isDonated = :donated WHERE id = :id")
    suspend fun setDonated(id: String, donated: Boolean)

    @Query("""
        SELECT id FROM collectibles
        WHERE typeRoute = :typeRoute AND isDonated = 1
    """)
    suspend fun getDonatedIdsByType(typeRoute: String): List<String>

    @Query("""
        SELECT COUNT(*) FROM collectibles
        WHERE typeRoute = :typeRoute AND isDonated = 1
    """)
    fun observeDonatedCount(typeRoute: String): Flow<Int>

    @Query("""
        SELECT COUNT(*) FROM collectibles
        WHERE typeRoute = :typeRoute
    """)
    fun observeTotalCount(typeRoute: String): Flow<Int>

    // OVERRIDES
    @Query("""
        SELECT id, userName, userSubtitle, userDescription
        FROM collectibles
        WHERE typeRoute = :typeRoute
          AND (userName IS NOT NULL OR userSubtitle IS NOT NULL OR userDescription IS NOT NULL)
    """)
    suspend fun getUserOverridesByType(typeRoute: String): List<UserOverrideRow>

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

    // DELETE / CLEAR
    @Query("DELETE FROM collectibles WHERE id = :id")
    suspend fun deleteById(id: String)

    @Query("DELETE FROM collectibles WHERE typeRoute = :typeRoute")
    suspend fun clearType(typeRoute: String)
}

data class UserOverrideRow(
    val id: String,
    val userName: String?,
    val userSubtitle: String?,
    val userDescription: String?
)
