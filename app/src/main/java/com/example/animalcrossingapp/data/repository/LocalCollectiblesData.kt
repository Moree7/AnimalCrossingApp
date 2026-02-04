package com.example.animalcrossingapp.data.repository

import com.example.animalcrossingapp.data.local.CollectibleDbEntity
import com.example.animalcrossingapp.domain.CollectibleType

/**
 * Datos locales opcionales (seed/fallback).
 * Si NO lo usas, puedes borrar este fichero.
 */
object LocalCollectiblesData {

    fun sampleFor(type: CollectibleType): List<CollectibleDbEntity> {
        return when (type) {

            CollectibleType.PECES -> listOf(
                CollectibleDbEntity(
                    id = "${type.routeValue}|Atún",
                    name = "Atún",
                    subtitle = "Un pez enorme.",
                    typeRoute = type.routeValue,
                    description = "Ejemplo local (sin API).",
                    isDonated = false
                )
            )

            CollectibleType.PESCA_SUBMARINA -> listOf(
                CollectibleDbEntity(
                    id = "${type.routeValue}|Pulpo",
                    name = "Pulpo",
                    subtitle = "Te mira con curiosidad.",
                    typeRoute = type.routeValue,
                    description = "Ejemplo local (sin API).",
                    isDonated = false
                )
            )

            CollectibleType.BICHOS -> listOf(
                CollectibleDbEntity(
                    id = "${type.routeValue}|Mariposa común",
                    name = "Mariposa común",
                    subtitle = "Vuela tranquila.",
                    typeRoute = type.routeValue,
                    description = "Ejemplo local (sin API).",
                    isDonated = false
                )
            )

            CollectibleType.FOSIL -> listOf(
                CollectibleDbEntity(
                    id = "${type.routeValue}|Ámbar",
                    name = "Ámbar",
                    subtitle = "",
                    typeRoute = type.routeValue,
                    description = "Ejemplo local (sin API).",
                    isDonated = false
                )
            )

            CollectibleType.OBRA_ARTE -> listOf(
                CollectibleDbEntity(
                    id = "${type.routeValue}|Cuadro famoso",
                    name = "Cuadro famoso",
                    subtitle = "",
                    typeRoute = type.routeValue,
                    description = "Ejemplo local (sin API).",
                    isDonated = false
                )
            )
        }
    }
}
