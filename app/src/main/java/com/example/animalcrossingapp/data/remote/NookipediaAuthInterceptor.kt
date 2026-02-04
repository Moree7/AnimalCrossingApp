package com.example.animalcrossingapp.data.remote

import android.util.Log
import com.example.animalcrossingapp.BuildConfig
import okhttp3.Interceptor
import okhttp3.Response

class NookipediaAuthInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val key = BuildConfig.NOOKIPEDIA_API_KEY.trim()

        val req = chain.request().newBuilder()
            .header("X-API-KEY", key)
            .header("Accept-Version", "1.7.0")
            .build()

        Log.d("NOOKIPEDIA", "AuthInterceptor keyLen=${key.length} last4=${key.takeLast(4)}")
        return chain.proceed(req)
    }
}
