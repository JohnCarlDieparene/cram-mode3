package com.labactivity.crammode

data class CohereRequest(
    val text: String,
    val length: String = "short",
    val format: String = "paragraph",
    val model: String = "command"
)
