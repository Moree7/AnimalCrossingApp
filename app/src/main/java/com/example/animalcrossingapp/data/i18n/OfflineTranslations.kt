package com.example.animalcrossingapp.data.i18n

import android.content.Context
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import java.io.BufferedReader

/**
 * Traducciones offline por categoría.
 * JSON esperado (assets/es_collectibles.json):
 * {
 *   "Peces": { "anchovy": "boquerón", ... },
 *   "Bichos": { ... },
 *   ...
 * }
 */
class OfflineTranslations private constructor(
    private val map: Map<String, Map<String, String>>
) {

    fun translateName(categoryRoute: String, englishName: String): String {
        val cat = map[categoryRoute] ?: return englishName
        val key = englishName.trim().lowercase()
        return cat[key] ?: englishName
    }

    fun hasCategory(categoryRoute: String): Boolean = map.containsKey(categoryRoute)

    companion object {

        fun empty(): OfflineTranslations = OfflineTranslations(emptyMap())

        fun from(map: Map<String, Map<String, String>>): OfflineTranslations {
            // Normalizamos claves internas a lowercase para hacer match robusto
            val normalized = map.mapValues { (_, inner) ->
                inner.mapKeys { (k, _) -> k.trim().lowercase() }
            }
            return OfflineTranslations(normalized)
        }

        /**
         * Carga el JSON desde /assets.
         * Lanza excepción si el JSON está mal formado o el archivo no existe.
         */
        fun load(context: Context, assetFileName: String): OfflineTranslations {
            val json = context.assets.open(assetFileName).use { input ->
                input.bufferedReader().use(BufferedReader::readText)
            }

            val moshi = Moshi.Builder().build()

            val innerMapType = Types.newParameterizedType(
                Map::class.java,
                String::class.java,
                String::class.java
            )
            val outerMapType = Types.newParameterizedType(
                Map::class.java,
                String::class.java,
                innerMapType
            )

            val adapter = moshi.adapter<Map<String, Map<String, String>>>(outerMapType)

            val parsed = adapter.fromJson(json)
                ?: throw IllegalStateException("No se pudo parsear $assetFileName (resultado null).")

            return from(parsed)
        }
    }
}
