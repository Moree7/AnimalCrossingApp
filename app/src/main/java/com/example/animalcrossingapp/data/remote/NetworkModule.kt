package com.example.animalcrossingapp.data.remote

import com.example.animalcrossingapp.BuildConfig
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

object NetworkModule {

    private const val BASE_URL = "https://api.nookipedia.com/"

    private val authInterceptor = Interceptor { chain ->
        val req = chain.request().newBuilder()
            .addHeader("X-API-KEY", BuildConfig.NOOKIPEDIA_API_KEY)
            .addHeader("Accept-Version", "1.0.0")
            .build()
        chain.proceed(req)
    }

    private val okHttp = OkHttpClient.Builder()
        .addInterceptor(authInterceptor)
        .build()

    private val moshi: Moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()

    private val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(okHttp)
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .build()

    val api: NookipediaService = retrofit.create(NookipediaService::class.java)
}
