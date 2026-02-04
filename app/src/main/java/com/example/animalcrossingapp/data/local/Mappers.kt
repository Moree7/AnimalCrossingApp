package com.example.animalcrossingapp.data.local

import com.example.animalcrossingapp.data.i18n.OfflineTranslations
import com.example.animalcrossingapp.domain.CollectibleType
import com.example.animalcrossingapp.ui.model.CollectibleUi

fun CollectibleDbEntity.toUi(offline: OfflineTranslations): CollectibleUi {
    val type = CollectibleType.fromRoute(typeRoute)

    // ✅ Traducción OFFLINE del nombre (si existe), si no, devuelve el inglés.
    val translatedName = offline.translateName(type.routeValue, name)

    // Si algún día añades traducciones para subtitle/description, aquí sería el sitio:
    val translatedSubtitle = subtitle
    val translatedDescription = description

    val displayName = userName?.takeIf { it.isNotBlank() } ?: translatedName
    val displaySubtitle = userSubtitle?.takeIf { it.isNotBlank() } ?: translatedSubtitle
    val displayDescription = userDescription?.takeIf { it.isNotBlank() } ?: translatedDescription

    return CollectibleUi(
        id = id,
        name = displayName,
        subtitle = displaySubtitle,
        description = displayDescription,
        type = type,
        isDonated = isDonated,

        // “Original” = lo que viene de la API/DB (EN)
        originalName = name,
        originalSubtitle = subtitle,
        originalDescription = description,

        userName = userName,
        userSubtitle = userSubtitle,
        userDescription = userDescription
    )
}
