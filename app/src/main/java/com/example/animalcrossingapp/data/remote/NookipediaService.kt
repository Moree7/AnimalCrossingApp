package com.example.animalcrossingapp.data.remote

import retrofit2.http.GET

interface NookipediaService {

    // Peces
    @GET("nh/fish")
    suspend fun getFish(): List<NookFishDto>

    // Pesca submarina (sea creatures)
    @GET("nh/sea")
    suspend fun getSeaCreatures(): List<NookSeaDto>

    // Bichos
    @GET("nh/bugs")
    suspend fun getBugs(): List<NookBugDto>

    // Fósiles
    @GET("nh/fossils/individuals")
    suspend fun getFossilsIndividuals(): List<NookFossilDto>


    // Obras de arte
    @GET("nh/art")
    suspend fun getArt(): List<NookArtDto>
}
