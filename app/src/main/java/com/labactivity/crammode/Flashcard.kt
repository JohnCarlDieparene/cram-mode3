package com.labactivity.crammode

import java.io.Serializable

data class Flashcard(
    val question: String,
    val answer: String
) : Serializable
