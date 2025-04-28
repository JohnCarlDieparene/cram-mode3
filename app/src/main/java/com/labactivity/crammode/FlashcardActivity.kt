package com.labactivity.crammode

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class FlashcardActivity : AppCompatActivity() {

    private lateinit var flashcardText: TextView
    private lateinit var toggleButton: Button
    private lateinit var nextButton: Button
    private lateinit var prevButton: Button

    private val flashcards = listOf(
        Pair("What is the capital of France?", "Paris"),
        Pair("What does HTML stand for?", "HyperText Markup Language"),
        Pair("What is 2 + 2?", "4")
    )

    private var currentIndex = 0
    private var isAnswerShown = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_flashcard)

        flashcardText = findViewById(R.id.flashcardText)
        toggleButton = findViewById(R.id.toggleButton)
        nextButton = findViewById(R.id.nextButton)
        prevButton = findViewById(R.id.prevButton)

        showQuestion()

        toggleButton.setOnClickListener {
            if (isAnswerShown) {
                showQuestion()
            } else {
                showAnswer()
            }
        }

        nextButton.setOnClickListener {
            currentIndex = (currentIndex + 1) % flashcards.size
            showQuestion()
        }

        prevButton.setOnClickListener {
            currentIndex = if (currentIndex - 1 < 0) flashcards.size - 1 else currentIndex - 1
            showQuestion()
        }
    }

    private fun showQuestion() {
        flashcardText.text = flashcards[currentIndex].first
        toggleButton.text = "Show Answer"
        isAnswerShown = false
    }

    private fun showAnswer() {
        flashcardText.text = flashcards[currentIndex].second
        toggleButton.text = "Show Question"
        isAnswerShown = true
    }
}
