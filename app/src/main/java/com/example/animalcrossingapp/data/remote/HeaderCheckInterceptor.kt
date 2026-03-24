package com.example.animalcrossingapp.data.remote

import android.util.Log
import okhttp3.Interceptor
import okhttp3.Response

class HeaderCheckInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val req = chain.request()
        Log.d("NOOKIPEDIA", "REQUEST url=${req.url}")
        Log.d("NOOKIPEDIA", "REQUEST X-API-KEY present=${req.header("X-API-KEY") != null}")
        Log.d("NOOKIPEDIA", "REQUEST Accept-Version=${req.header("Accept-Version")}")

        Log.d("NOOKIPEDIA", "X-API-KEY raw='${req.header("X-API-KEY")}'")
        Log.d("NOOKIPEDIA", "X-API-KEY bytes=${req.header("X-API-KEY")?.toByteArray()?.joinToString()}")

        return chain.proceed(req)
    }
}
