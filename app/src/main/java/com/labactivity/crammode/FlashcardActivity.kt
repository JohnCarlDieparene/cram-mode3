package com.labactivity.crammode

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.FirebaseFirestore

class FlashcardActivity : AppCompatActivity() {

    private val db = FirebaseFirestore.getInstance()
    private lateinit var flashcardsList: MutableList<Map<String, String>>
    private lateinit var questionTextView: TextView
    private lateinit var answerTextView: TextView
    private lateinit var toggleButton: Button
    private lateinit var prevButton: Button
    private lateinit var nextButton: Button
    private var currentIndex = 0
    private var isAnswerVisible = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_flashcard)

        // Initialize views
        questionTextView = findViewById(R.id.questionTextView)
        answerTextView = findViewById(R.id.answerTextView)
        toggleButton = findViewById(R.id.toggleButton)
        prevButton = findViewById(R.id.prevButton)
        nextButton = findViewById(R.id.nextButton)

        flashcardsList = mutableListOf()

        // Button listeners
        toggleButton.setOnClickListener {
            isAnswerVisible = !isAnswerVisible
            answerTextView.visibility = if (isAnswerVisible) View.VISIBLE else View.GONE
            toggleButton.text = if (isAnswerVisible) "Hide Answer" else "Show Answer"
        }

        prevButton.setOnClickListener {
            if (flashcardsList.isNotEmpty()) {
                currentIndex = if (currentIndex - 1 < 0) flashcardsList.size - 1 else currentIndex - 1
                isAnswerVisible = false
                showFlashcard()
            }
        }

        nextButton.setOnClickListener {
            if (flashcardsList.isNotEmpty()) {
                currentIndex = (currentIndex + 1) % flashcardsList.size
                isAnswerVisible = false
                showFlashcard()
            }
        }

        fetchFlashcards()
    }

    private fun fetchFlashcards() {
        db.collection("flashcards")
            .get()
            .addOnSuccessListener { result ->
                flashcardsList.clear()
                for (document in result) {
                    val question = document.getString("question") ?: ""
                    val answer = document.getString("answer") ?: ""
                    flashcardsList.add(mapOf("question" to question, "answer" to answer))
                }
                if (flashcardsList.isNotEmpty()) {
                    showFlashcard()
                } else {
                    questionTextView.text = "No flashcards found."
                    answerTextView.text = ""
                }
            }
            .addOnFailureListener {
                Toast.makeText(this, "Failed to load flashcards", Toast.LENGTH_SHORT).show()
            }
    }

    private fun showFlashcard() {
        val flashcard = flashcardsList[currentIndex]
        questionTextView.text = flashcard["question"]
        answerTextView.text = flashcard["answer"]
        answerTextView.visibility = if (isAnswerVisible) View.VISIBLE else View.GONE
        toggleButton.text = if (isAnswerVisible) "Hide Answer" else "Show Answer"
    }
}
