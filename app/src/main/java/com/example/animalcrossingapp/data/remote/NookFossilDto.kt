package com.example.animalcrossingapp.data.remote

import com.squareup.moshi.Json

data class NookFossilDto(
    @Json(name = "name") val name: String?,
    @Json(name = "museum_phrase") val museumPhrase: String? // si no existe, será null y no rompe
)
