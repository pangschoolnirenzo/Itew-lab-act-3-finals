package com.example.tasknote.data.remote

import com.example.tasknote.BuildConfig
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory
import java.util.concurrent.TimeUnit

object NetworkModule {
    // TODO: replace <your-id> with your own MockAPI.io project id. Must end with "/".
    private const val BASE_URL = "https://<your-id>.mockapi.io/api/v1/"

    private val json = Json {
        ignoreUnknownKeys = true // survive new server fields
        coerceInputValues = true // null for a non-null field -> default value
    }

    private val okHttp = OkHttpClient.Builder()
        .addInterceptor(HttpLoggingInterceptor().apply {
            // BODY logging prints full payloads: debug builds only
            level = if (BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BODY
            else HttpLoggingInterceptor.Level.NONE
        })
        .connectTimeout(15, TimeUnit.SECONDS)
        .readTimeout(15, TimeUnit.SECONDS)
        .build()

    val api: TaskApiService = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(okHttp)
        .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
        .build()
        .create(TaskApiService::class.java)
}
