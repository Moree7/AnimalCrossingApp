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

    // ---------- OBSERVE LIST ----------
    fun observeByType(typeRoute: String): Flow<List<CollectibleUi>> =
        dao.observeByType(typeRoute).map { list -> list.map { it.toUi(offline) } }

    // ---------- GET BY ID ----------
    suspend fun getById(id: String): CollectibleUi {
        val entity = dao.getById(id) ?: error("Item no encontrado: $id")
        return entity.toUi(offline)
    }

    // ---------- REFRESH FROM API ----------
    suspend fun refresh(type: CollectibleType) {
        val typeRoute = type.routeValue

        val entities: List<CollectibleDbEntity> = when (type) {
            CollectibleType.PECES -> {
                api.getFish()
                    .mapNotNull { dto ->
                        val nameEn = dto.name?.trim().orEmpty()
                        if (nameEn.isBlank()) return@mapNotNull null
                        val key = nameEn.lowercase(Locale.ROOT)
                        CollectibleDbEntity(
                            id = "${typeRoute}|$key",
                            name = nameEn,
                            subtitle = dto.catchphrase?.trim().orEmpty(),
                            typeRoute = typeRoute,
                            description = dto.catchphrase?.trim().orEmpty(),
                            isDonated = false
                        )
                    }
            }

            CollectibleType.PESCA_SUBMARINA -> {
                api.getSeaCreatures()
                    .mapNotNull { dto ->
                        val nameEn = dto.name?.trim().orEmpty()
                        if (nameEn.isBlank()) return@mapNotNull null
                        val key = nameEn.lowercase(Locale.ROOT)
                        CollectibleDbEntity(
                            id = "${typeRoute}|$key",
                            name = nameEn,
                            subtitle = dto.catchphrase?.trim().orEmpty(),
                            typeRoute = typeRoute,
                            description = dto.catchphrase?.trim().orEmpty(),
                            isDonated = false
                        )
                    }
            }

            CollectibleType.BICHOS -> {
                api.getBugs()
                    .mapNotNull { dto ->
                        val nameEn = dto.name?.trim().orEmpty()
                        if (nameEn.isBlank()) return@mapNotNull null
                        val key = nameEn.lowercase(Locale.ROOT)
                        CollectibleDbEntity(
                            id = "${typeRoute}|$key",
                            name = nameEn,
                            subtitle = dto.catchphrase?.trim().orEmpty(),
                            typeRoute = typeRoute,
                            description = dto.catchphrase?.trim().orEmpty(),
                            isDonated = false
                        )
                    }
            }

            CollectibleType.FOSIL -> {
                api.getFossilsIndividuals()
                    .mapNotNull { dto ->
                        val nameEn = dto.name?.trim().orEmpty()
                        if (nameEn.isBlank()) return@mapNotNull null
                        val key = nameEn.lowercase(Locale.ROOT)
                        val desc = dto.museumPhrase?.trim().orEmpty()
                        CollectibleDbEntity(
                            id = "${typeRoute}|$key",
                            name = nameEn,
                            subtitle = "",
                            typeRoute = typeRoute,
                            description = desc,
                            isDonated = false
                        )
                    }
            }

            CollectibleType.OBRA_ARTE -> {
                api.getArt()
                    .mapNotNull { dto ->
                        val nameEn = dto.name?.trim().orEmpty()
                        if (nameEn.isBlank()) return@mapNotNull null
                        val key = nameEn.lowercase(Locale.ROOT)
                        val desc = dto.museumDesc?.trim().orEmpty()
                        CollectibleDbEntity(
                            id = "${typeRoute}|$key",
                            name = nameEn,
                            subtitle = "",
                            typeRoute = typeRoute,
                            description = desc,
                            isDonated = false
                        )
                    }
            }
        }

        // ✅ Preserva DONATED y overrides existentes si ya estaban
        // (Upsert no borra columnas si tú no las cambias, pero aquí estás creando entities nuevas.
        // Para no pisar donados/overrides, leemos lo existente y lo reinyectamos.)
        val existingOverrides = dao.getUserOverridesByType(typeRoute).associateBy { it.id }
        val existingDonatedIds = dao.getDonatedIdsByType(typeRoute).toHashSet()

        val merged = entities.map { e ->
            val ov = existingOverrides[e.id]
            e.copy(
                isDonated = e.id in existingDonatedIds,
                userName = ov?.userName,
                userSubtitle = ov?.userSubtitle,
                userDescription = ov?.userDescription
            )
        }

        dao.clearType(typeRoute)
        dao.upsertAll(merged)
    }

    // ---------- DONATED ----------
    suspend fun setDonated(id: String, donated: Boolean) {
        dao.setDonated(id, donated)
    }

    // ---------- USER OVERRIDES ----------
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

    // ---------- ADD MANUAL ITEM ----------
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

        val entity = CollectibleDbEntity(
            id = id,
            name = nameTrim,
            subtitle = subtitle.trim(),
            typeRoute = typeRoute,
            description = description.trim(),
            isDonated = false
        )
        dao.upsert(entity)
    }

    // ---------- DELETE ----------
    suspend fun deleteById(id: String) {
        dao.deleteById(id)
    }
}
