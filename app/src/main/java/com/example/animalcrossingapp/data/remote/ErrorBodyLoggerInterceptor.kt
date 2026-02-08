package com.example.animalcrossingapp.data.remote

import android.util.Log
import okhttp3.Interceptor
import okhttp3.Response

class ErrorBodyLoggerInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val response = chain.proceed(chain.request())

        if (!response.isSuccessful) {
            val peek = response.peekBody(1024 * 1024).string()
            Log.e("NOOKIPEDIA", "HTTP ${response.code} body: $peek")
        }
        return response
    }
}
