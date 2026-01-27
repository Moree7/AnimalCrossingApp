package com.example.animalcrossingapp.data.repository

import com.example.animalcrossingapp.data.remote.NookFishDto
import com.example.animalcrossingapp.data.remote.NookipediaService

class FishRepository(
    private val api: NookipediaService
) {
    suspend fun getFish(): List<NookFishDto> = api.getFish()
}
