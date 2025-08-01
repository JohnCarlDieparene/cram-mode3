package com.labactivity.crammode

data class FlashcardResponse(
    val generations: List<Generation>
)

data class Generation(
    val text: String
)
