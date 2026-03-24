package com.example.animalcrossingapp.data

import android.content.Context
import com.example.animalcrossingapp.data.i18n.OfflineTranslations
import com.example.animalcrossingapp.data.local.DatabaseModule
import com.example.animalcrossingapp.data.remote.NetworkModule
import com.example.animalcrossingapp.data.repository.CollectiblesRepository

object RepositoryProvider {

    @Volatile private var repo: CollectiblesRepository? = null

    fun collectiblesRepository(context: Context): CollectiblesRepository {
        return repo ?: synchronized(this) {
            repo ?: run {
                val appContext = context.applicationContext
                val dao = DatabaseModule.db(appContext).collectibleDao()
                val api = NetworkModule.api

                // Carga traducciones offline desde assets/es_collectibles.json
                val offlineTranslations = try {
                    OfflineTranslations.load(appContext, "es_collectibles.json")
                } catch (e: Exception) {
                    // Si algo va mal, NO reventamos la app: seguimos sin traducción.
                    OfflineTranslations.empty()
                }

                // IMPORTANTE: tu CollectiblesRepository debe tener este constructor:
                // CollectiblesRepository(api, dao, offlineTranslations)
                val built = CollectiblesRepository(api, dao, offlineTranslations)

                repo = built
                built
            }
        }
    }
}
