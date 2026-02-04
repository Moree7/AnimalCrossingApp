package com.example.animalcrossingapp.data.remote

import com.squareup.moshi.Json

data class NookArtDto(
    val name: String?,
    @Json(name = "museum_desc") val museumDesc: String? // <-- si el JSON se llama así
)

