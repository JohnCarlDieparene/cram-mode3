// --- CohereClient.kt ---
package com.labactivity.crammode

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object CohereClient {

    private const val BASE_URL = "https://api.cohere.com/"

    // Configure OkHttp with timeouts (optional but recommended for AI APIs)
    private val okHttpClient = OkHttpClient.Builder()
        .connectTimeout(60, TimeUnit.SECONDS)
        .readTimeout(60, TimeUnit.SECONDS)
        .writeTimeout(60, TimeUnit.SECONDS)
        .build()

    // Retrofit instance
    private val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    // Expose CohereApi
    val api: CohereApi = retrofit.create(CohereApi::class.java)
}
