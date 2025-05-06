package com.labactivity.crammode

import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class CreateActivity : AppCompatActivity() {

    private lateinit var selectionSpinner: Spinner
    private lateinit var flashcardLayout: LinearLayout
    private lateinit var quizLayout: LinearLayout

    // Flashcard
    private lateinit var questionInput: EditText
    private lateinit var answerInput: EditText
    private lateinit var saveFlashcardButton: Button

    // Quiz
    private lateinit var quizQuestionInput: EditText
    private lateinit var optionAInput: EditText
    private lateinit var optionBInput: EditText
    private lateinit var optionCInput: EditText
    private lateinit var optionDInput: EditText
    private lateinit var correctAnswerRadioGroup: RadioGroup
    private lateinit var radioA: RadioButton
    private lateinit var radioB: RadioButton
    private lateinit var radioC: RadioButton
    private lateinit var radioD: RadioButton
    private lateinit var saveQuizButton: Button

    private val db = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create)

        // Spinner and Layouts
        selectionSpinner = findViewById(R.id.selectionSpinner)
        flashcardLayout = findViewById(R.id.flashcardLayout)
        quizLayout = findViewById(R.id.quizLayout)

        // Flashcard fields
        questionInput = findViewById(R.id.questionInput)
        answerInput = findViewById(R.id.answerInput)
        saveFlashcardButton = findViewById(R.id.saveFlashcardButton)

        // Quiz fields
        quizQuestionInput = findViewById(R.id.quizQuestionInput)
        optionAInput = findViewById(R.id.optionAInput)
        optionBInput = findViewById(R.id.optionBInput)
        optionCInput = findViewById(R.id.optionCInput)
        optionDInput = findViewById(R.id.optionDInput)
        correctAnswerRadioGroup = findViewById(R.id.correctAnswerRadioGroup)
        radioA = findViewById(R.id.radioA)
        radioB = findViewById(R.id.radioB)
        radioC = findViewById(R.id.radioC)
        radioD = findViewById(R.id.radioD)
        saveQuizButton = findViewById(R.id.saveQuizButton)

        // Spinner selection
        selectionSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val selected = selectionSpinner.selectedItem.toString()
                flashcardLayout.visibility = if (selected == "Flashcard") View.VISIBLE else View.GONE
                quizLayout.visibility = if (selected == "Quiz") View.VISIBLE else View.GONE
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

        // Save flashcard
        saveFlashcardButton.setOnClickListener {
            val userId = auth.currentUser?.uid
            if (userId == null) {
                Toast.makeText(this, "User not logged in", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val question = questionInput.text.toString().trim()
            val answer = answerInput.text.toString().trim()

            if (question.isEmpty() || answer.isEmpty()) {
                Toast.makeText(this, "Please enter both question and answer", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (question.equals(answer, ignoreCase = true)) {
                Toast.makeText(this, "Question and answer cannot be the same", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val userFlashcardsRef = db.collection("users").document(userId).collection("flashcards")

            userFlashcardsRef
                .whereEqualTo("question", question)
                .get()
                .addOnSuccessListener { documents ->
                    if (!documents.isEmpty) {
                        Toast.makeText(this, "This flashcard already exists", Toast.LENGTH_SHORT).show()
                    } else {
                        val flashcard = hashMapOf("question" to question, "answer" to answer)
                        userFlashcardsRef
                            .add(flashcard)
                            .addOnSuccessListener {
                                Toast.makeText(this, "Flashcard saved!", Toast.LENGTH_SHORT).show()
                                questionInput.text.clear()
                                answerInput.text.clear()
                            }
                            .addOnFailureListener {
                                Toast.makeText(this, "Error saving flashcard", Toast.LENGTH_SHORT).show()
                            }
                    }
                }
                .addOnFailureListener {
                    Toast.makeText(this, "Error checking for duplicate flashcard", Toast.LENGTH_SHORT).show()
                }
        }

        // Save quiz
        saveQuizButton.setOnClickListener {
            val userId = auth.currentUser?.uid
            if (userId == null) {
                Toast.makeText(this, "User not logged in", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val question = quizQuestionInput.text.toString().trim()
            val optionA = optionAInput.text.toString().trim()
            val optionB = optionBInput.text.toString().trim()
            val optionC = optionCInput.text.toString().trim()
            val optionD = optionDInput.text.toString().trim()

            val selectedRadioId = correctAnswerRadioGroup.checkedRadioButtonId
            val correctAnswer = when (selectedRadioId) {
                R.id.radioA -> optionA
                R.id.radioB -> optionB
                R.id.radioC -> optionC
                R.id.radioD -> optionD
                else -> null
            }

            if (question.isEmpty() || correctAnswer.isNullOrEmpty()) {
                Toast.makeText(this, "Please enter all quiz details and select a correct answer", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (question.equals(correctAnswer, ignoreCase = true)) {
                Toast.makeText(this, "Question and correct answer cannot be the same", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val userQuizzesRef = db.collection("users").document(userId).collection("quizzes")

            userQuizzesRef
                .whereEqualTo("question", question)
                .get()
                .addOnSuccessListener { documents ->
                    if (!documents.isEmpty) {
                        Toast.makeText(this, "This quiz question already exists", Toast.LENGTH_SHORT).show()
                    } else {
                        val quiz = hashMapOf(
                            "question" to question,
                            "optionA" to optionA,
                            "optionB" to optionB,
                            "optionC" to optionC,
                            "optionD" to optionD,
                            "correctAnswer" to correctAnswer
                        )

                        userQuizzesRef
                            .add(quiz)
                            .addOnSuccessListener {
                                Toast.makeText(this, "Quiz saved!", Toast.LENGTH_SHORT).show()
                                quizQuestionInput.text.clear()
                                optionAInput.text.clear()
                                optionBInput.text.clear()
                                optionCInput.text.clear()
                                optionDInput.text.clear()
                                correctAnswerRadioGroup.clearCheck()
                            }
                            .addOnFailureListener {
                                Toast.makeText(this, "Error saving quiz", Toast.LENGTH_SHORT).show()
                            }
                    }
                }
                .addOnFailureListener {
                    Toast.makeText(this, "Error checking for duplicate quiz", Toast.LENGTH_SHORT).show()
                }
        }
    }
}
