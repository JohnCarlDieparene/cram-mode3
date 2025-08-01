package com.labactivity.crammode

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.POST

interface CohereApi {

    @Headers("Content-Type: application/json")
    @POST("summarize")
    fun summarize(
        @Header("Authorization") auth: String,
        @Body request: CohereRequest
    ): Call<CohereResponse>

    @Headers("Content-Type: application/json")
    @POST("generate")
    fun generateFlashcards(
        @Header("Authorization") auth: String,
        @Body request: FlashcardRequest
    ): Call<FlashcardResponse>

    @Headers("Content-Type: application/json")
    @POST("generate")
    fun generateQuiz(
        @Header("Authorization") auth: String,
        @Body request: QuizRequest
    ): Call<QuizResponse>
}
