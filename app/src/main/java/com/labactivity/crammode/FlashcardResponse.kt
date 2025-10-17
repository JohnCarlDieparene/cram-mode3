package com.labactivity.crammode

data class FlashcardResponse(
    val generations: List<FlashcardGeneration>
)

data class FlashcardGeneration(
    val text: String
)
