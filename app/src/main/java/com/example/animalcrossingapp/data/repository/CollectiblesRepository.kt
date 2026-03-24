package com.example.animalcrossingapp.data.repository

import com.example.animalcrossingapp.data.i18n.OfflineTranslations
import com.example.animalcrossingapp.data.local.CollectibleDao
import com.example.animalcrossingapp.data.local.CollectibleDbEntity
import com.example.animalcrossingapp.data.local.toUi
import com.example.animalcrossingapp.data.remote.NookipediaService
import com.example.animalcrossingapp.domain.CollectibleType
import com.example.animalcrossingapp.ui.model.CollectibleUi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.util.Locale

class CollectiblesRepository(
    private val api: NookipediaService,
    private val dao: CollectibleDao,
    private val offline: OfflineTranslations = OfflineTranslations.empty()
) {

    fun observeByType(typeRoute: String): Flow<List<CollectibleUi>> =
        dao.observeByType(typeRoute).map { list -> list.map { it.toUi(offline) } }

    suspend fun getById(id: String): CollectibleUi {
        val entity = dao.getById(id) ?: error("Item no encontrado: $id")
        return entity.toUi(offline)
    }

    suspend fun refresh(type: CollectibleType) {
        val typeRoute = type.routeValue

        val entities: List<CollectibleDbEntity> = when (type) {
            CollectibleType.PECES -> {
                api.getFish().mapNotNull { dto ->
                    val nameEn = dto.name?.trim().orEmpty()
                    if (nameEn.isBlank()) return@mapNotNull null
                    val key = nameEn.lowercase(Locale.ROOT)
                    CollectibleDbEntity(
                        id = "${typeRoute}|$key",
                        name = nameEn,
                        subtitle = dto.catchphrase?.trim().orEmpty(),
                        typeRoute = typeRoute,
                        description = dto.catchphrase?.trim().orEmpty(),
                        isDonated = false,
                        displayOrder = 0
                    )
                }
            }

            CollectibleType.PESCA_SUBMARINA -> {
                api.getSeaCreatures().mapNotNull { dto ->
                    val nameEn = dto.name?.trim().orEmpty()
                    if (nameEn.isBlank()) return@mapNotNull null
                    val key = nameEn.lowercase(Locale.ROOT)
                    CollectibleDbEntity(
                        id = "${typeRoute}|$key",
                        name = nameEn,
                        subtitle = dto.catchphrase?.trim().orEmpty(),
                        typeRoute = typeRoute,
                        description = dto.catchphrase?.trim().orEmpty(),
                        isDonated = false,
                        displayOrder = 0
                    )
                }
            }

            CollectibleType.BICHOS -> {
                api.getBugs().mapNotNull { dto ->
                    val nameEn = dto.name?.trim().orEmpty()
                    if (nameEn.isBlank()) return@mapNotNull null
                    val key = nameEn.lowercase(Locale.ROOT)
                    CollectibleDbEntity(
                        id = "${typeRoute}|$key",
                        name = nameEn,
                        subtitle = dto.catchphrase?.trim().orEmpty(),
                        typeRoute = typeRoute,
                        description = dto.catchphrase?.trim().orEmpty(),
                        isDonated = false,
                        displayOrder = 0
                    )
                }
            }

            CollectibleType.FOSIL -> {
                api.getFossilsIndividuals().mapNotNull { dto ->
                    val nameEn = dto.name?.trim().orEmpty()
                    if (nameEn.isBlank()) return@mapNotNull null
                    val key = nameEn.lowercase(Locale.ROOT)
                    CollectibleDbEntity(
                        id = "${typeRoute}|$key",
                        name = nameEn,
                        subtitle = "",
                        typeRoute = typeRoute,
                        description = dto.museumPhrase?.trim().orEmpty(),
                        isDonated = false,
                        displayOrder = 0
                    )
                }
            }

            CollectibleType.OBRA_ARTE -> {
                api.getArt().mapNotNull { dto ->
                    val nameEn = dto.name?.trim().orEmpty()
                    if (nameEn.isBlank()) return@mapNotNull null
                    val key = nameEn.lowercase(Locale.ROOT)
                    CollectibleDbEntity(
                        id = "${typeRoute}|$key",
                        name = nameEn,
                        subtitle = "",
                        typeRoute = typeRoute,
                        description = dto.museumDesc?.trim().orEmpty(),
                        isDonated = false,
                        displayOrder = 0
                    )
                }
            }
        }

        val existingOverrides = dao.getUserOverridesByType(typeRoute).associateBy { it.id }
        val existingDonatedIds = dao.getDonatedIdsByType(typeRoute).toHashSet()
        val currentItems = dao.getAllByType(typeRoute).associateBy { it.id }

        val merged = entities.mapIndexed { index, e ->
            val ov = existingOverrides[e.id]
            val previous = currentItems[e.id]
            e.copy(
                isDonated = e.id in existingDonatedIds,
                userName = ov?.userName,
                userSubtitle = ov?.userSubtitle,
                userDescription = ov?.userDescription,
                displayOrder = previous?.displayOrder ?: index
            )
        }

        dao.clearType(typeRoute)
        dao.upsertAll(merged)
    }

    suspend fun setDonated(id: String, donated: Boolean) {
        dao.setDonated(id, donated)
    }

    suspend fun updateUserOverrides(
        id: String,
        userName: String?,
        userSubtitle: String?,
        userDescription: String?
    ) {
        dao.updateUserOverrides(
            id = id,
            userName = userName?.takeIf { it.isNotBlank() },
            userSubtitle = userSubtitle?.takeIf { it.isNotBlank() },
            userDescription = userDescription?.takeIf { it.isNotBlank() }
        )
    }

    suspend fun restoreDefaults(id: String) {
        dao.updateUserOverrides(
            id = id,
            userName = null,
            userSubtitle = null,
            userDescription = null
        )
    }

    suspend fun addManualItem(
        type: CollectibleType,
        name: String,
        subtitle: String,
        description: String
    ) {
        val typeRoute = type.routeValue
        val nameTrim = name.trim()
        require(nameTrim.isNotBlank()) { "El nombre es obligatorio" }

        val key = nameTrim.lowercase(Locale.ROOT)
        val id = "${typeRoute}|manual_$key"
        val nextOrder = dao.getAllByType(typeRoute).size

        val entity = CollectibleDbEntity(
            id = id,
            name = nameTrim,
            subtitle = subtitle.trim(),
            typeRoute = typeRoute,
            description = description.trim(),
            isDonated = false,
            displayOrder = nextOrder
        )
        dao.upsert(entity)
    }

    suspend fun deleteById(id: String) {
        dao.deleteById(id)
    }

    suspend fun deleteAndReturnBackup(id: String): CollectibleDbEntity? {
        val entity = dao.getById(id)
        if (entity != null) dao.deleteById(id)
        return entity
    }

    suspend fun restoreDeletedItem(entity: CollectibleDbEntity) {
        dao.upsert(entity)
    }

    suspend fun reorderItems(items: List<CollectibleUi>) {
        items.forEachIndexed { index, item ->
            dao.updateDisplayOrder(item.id, index)
        }
    }
}