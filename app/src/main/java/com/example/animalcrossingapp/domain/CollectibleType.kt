package com.example.animalcrossingapp.domain

enum class CollectibleType(val routeValue: String) {
    PECES("Peces"),
    PESCA_SUBMARINA("Pesca_submarina"),
    BICHOS("Bichos"),
    FOSIL("Fosil"),
    OBRA_ARTE("Obra_Arte");

    companion object {
        fun fromRoute(route: String?): CollectibleType {
            val normalized = route
                .orEmpty()
                .trim()

            return entries.firstOrNull { it.routeValue.equals(normalized, ignoreCase = true) }
                ?: PECES // fallback seguro
        }
    }
}
