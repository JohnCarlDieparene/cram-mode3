package com.labactivity.crammode.model

data class StudyHistory(
    var id: String = "",               // <-- Add this
    val uid: String = "",
    val type: String = "",
    val inputText: String = "",
    val timestamp: Long = 0L,
    val resultText: String = "",
    val quiz: List<QuizQuestion> = emptyList() // ✅ store quiz objects directly
)
