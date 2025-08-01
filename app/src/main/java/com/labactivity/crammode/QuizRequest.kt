package com.labactivity.crammode

data class QuizRequest(
    val model: String = "command-r-plus",
    val prompt: String,
    val max_tokens: Int = 1500,
    val temperature: Double = 0.7
)
