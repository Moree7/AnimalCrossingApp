package com.example.animalcrossingapp.data.local

import com.example.animalcrossingapp.R
import com.example.animalcrossingapp.data.i18n.OfflineTranslations
import com.example.animalcrossingapp.domain.CollectibleType
import com.example.animalcrossingapp.ui.model.CollectibleUi
import java.util.Locale

fun CollectibleDbEntity.toUi(offline: OfflineTranslations): CollectibleUi {
    val type = CollectibleType.fromRoute(typeRoute)

    val translatedName = offline.translateName(type.routeValue, name)
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
        originalName = name,
        originalSubtitle = subtitle,
        originalDescription = description,
        userName = userName,
        userSubtitle = userSubtitle,
        userDescription = userDescription,
        imageResId = imageForCollectible(type, name),
        displayOrder = displayOrder
    )
}

private val fishImages = mapOf(
    "carp" to R.drawable.fish_carp,
    "anchovy" to R.drawable.fish_anchovy,
    "angelfish" to R.drawable.fish_angelfish,
    "arapaima" to R.drawable.fish_arapaima,
    "amazon leaffish" to R.drawable.fish_amazon_leaffish,
    "achilles surgeonfish" to R.drawable.fish_achilles_surgeonfish
)

private val seaImages = mapOf(
    "sea grapes" to R.drawable.sea_sea_grapes
)

private val bugImages = mapOf(
    "agrias butterfly" to R.drawable.bug_agrias_butterfly,
    "ant" to R.drawable.bug_ant,
    "atlas moth" to R.drawable.bug_atlas_moth
)

private val fossilImages = mapOf(
    "acanthostega" to R.drawable.fossil_acanthostega,
    "amber" to R.drawable.fossil_amber,
    "ammonite" to R.drawable.fossil_ammonite
)

private val artImages = mapOf(
    "beautiful statue" to R.drawable.art_beautiful_statue
)

private fun imageForCollectible(type: CollectibleType, name: String): Int? {
    val normalized = name.trim().lowercase(Locale.ROOT)

    return when (type) {
        CollectibleType.PECES -> fishImages[normalized]
        CollectibleType.PESCA_SUBMARINA -> seaImages[normalized]
        CollectibleType.BICHOS -> bugImages[normalized]
        CollectibleType.FOSIL -> fossilImages[normalized]
        CollectibleType.OBRA_ARTE -> artImages[normalized]
    }
}