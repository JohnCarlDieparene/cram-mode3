package com.labactivity.crammode.utils

import com.labactivity.crammode.Flashcard

object FlashcardUtils {

    fun parseFlashcards(raw: String): List<Flashcard> {
        val flashcards = mutableListOf<Flashcard>()

        // Primary regex parser (strict format)
        val pattern = Regex(
            """Q:\s*(.+?)\s*(?:\r?\n)+A:\s*(.+?)(?=(?:\r?\n)+Q:|\z)""",
            setOf(RegexOption.DOT_MATCHES_ALL, RegexOption.IGNORE_CASE)
        )

        val matches = pattern.findAll(raw)
        for (match in matches) {
            val question = match.groupValues[1].trim()
            val answer = match.groupValues[2].trim()
            if (question.isNotEmpty() && answer.isNotEmpty()) {
                flashcards.add(Flashcard(question, answer))
            }
        }


        if (flashcards.isEmpty()) {
            val parts = raw.split(Regex("""(?=\nQ:)"""))
            for (part in parts) {
                val qMatch = Regex("""Q:\s*(.+)""").find(part)
                val aMatch = Regex("""A:\s*(.+)""").find(part)

                val question = qMatch?.groupValues?.get(1)?.trim() ?: ""
                val answer = aMatch?.groupValues?.get(1)?.trim() ?: ""

                if (question.isNotEmpty() && answer.isNotEmpty()) {
                    flashcards.add(Flashcard(question, answer))
                }
            }
        }

        return flashcards
    }
}
