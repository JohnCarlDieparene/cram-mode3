package com.labactivity.crammode.utils

import com.labactivity.crammode.Flashcard


object FlashcardUtils {

    fun parseFlashcards(raw: String): List<Flashcard> {
        val flashcards = mutableListOf<Flashcard>()

        val pattern = Regex("""Q:\s*(.*?)\s*A:\s*(.*?)(?=\nQ:|\z)""", setOf(RegexOption.DOT_MATCHES_ALL, RegexOption.IGNORE_CASE))
        val matches = pattern.findAll(raw)

        for (match in matches) {
            val question = match.groupValues[1].trim()
            val answer = match.groupValues[2].trim()

            if (question.isNotEmpty() && answer.isNotEmpty()) {
                flashcards.add(Flashcard(question, answer))
            }
        }

        return flashcards
    }

}
