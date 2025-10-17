// --- CohereApi.kt ---
package com.labactivity.crammode

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.POST

interface CohereApi {

    // ✅ v2 Chat endpoint
    @Headers("Content-Type: application/json")
    @POST("v2/chat")
    fun chat(
        @Header("Authorization") auth: String,   // "Bearer YOUR_API_KEY"
        @Body request: ChatRequest
    ): Call<ChatResponse>

    // ✅ v1 Generate endpoint for Flashcards
    @Headers("Content-Type: application/json")
    @POST("generate")
    fun generateFlashcards(
        @Header("Authorization") auth: String,
        @Body request: FlashcardRequest
    ): Call<FlashcardResponse>

    // ✅ v1 Generate endpoint for Quiz
    @Headers("Content-Type: application/json")
    @POST("generate")
    fun generateQuiz(
        @Header("Authorization") auth: String,
        @Body request: QuizRequest
    ): Call<QuizResponse>
}
