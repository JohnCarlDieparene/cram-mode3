package com.labactivity.crammode.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class QuizQuestion(
    val question: String,
    val options: List<String>,
    val answer: String
) : Parcelable
