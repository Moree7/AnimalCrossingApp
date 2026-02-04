package com.example.animalcrossingapp.ui.debug

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.animalcrossingapp.data.remote.NetworkModule.api
import kotlinx.coroutines.launch

class ApiTestViewModel : ViewModel() {

    fun testFish() {
        viewModelScope.launch {
            try {
                val fish = api.getFish() // ✅ SIN parámetros
                Log.d("NOOKIPEDIA", "Fish count = ${fish.size}")
                Log.d("NOOKIPEDIA", "First fish = ${fish.firstOrNull()}")
            } catch (t: Throwable) {
                Log.e("NOOKIPEDIA", "API error", t)
            }
        }
    }
}
