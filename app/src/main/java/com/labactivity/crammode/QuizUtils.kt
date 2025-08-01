package com.labactivity.crammode

import com.labactivity.crammode.model.QuizQuestion

object QuizUtils {
    fun parseQuizQuestions(raw: String): List<QuizQuestion> {
        val questions = mutableListOf<QuizQuestion>()

        // Split into blocks using "Question:" or "Tanong:"
        val blocks = raw.trim().split(Regex("""(?=(Question|Tanong):)""", RegexOption.IGNORE_CASE))

        for (block in blocks) {
            val questionMatch = Regex("""(Question|Tanong):\s*(.+?)\n""", RegexOption.IGNORE_CASE).find(block)
            val choicesMatch = Regex("""[A-D]\.\s*(.+?)\n""").findAll(block)
            val answerMatch = Regex("""(Answer|Sagot):\s*([A-D])""", RegexOption.IGNORE_CASE).find(block)

            if (questionMatch != null && answerMatch != null) {
                val question = questionMatch.groupValues[2].trim()
                val choices = choicesMatch.map { it.groupValues[1].trim() }.toList()
                val correctLetter = answerMatch.groupValues[2].uppercase()
                val correctIndex = "ABCD".indexOf(correctLetter)
                val correctAnswer = choices.getOrNull(correctIndex).orEmpty()

                if (question.isNotEmpty() && choices.size == 4 && correctAnswer.isNotEmpty()) {
                    questions.add(QuizQuestion(question, choices, correctAnswer))
                }
            }
        }

        return questions
    }
}
