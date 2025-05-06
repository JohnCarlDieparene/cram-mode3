package com.labactivity.crammode

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.FirebaseFirestore

class QuizActivity : AppCompatActivity() {

    private lateinit var questionTextView: TextView
    private lateinit var optionARadio: RadioButton
    private lateinit var optionBRadio: RadioButton
    private lateinit var optionCRadio: RadioButton
    private lateinit var optionDRadio: RadioButton
    private lateinit var radioGroup: RadioGroup
    private lateinit var nextButton: Button
    private lateinit var submitButton: Button
    private lateinit var questionCounterText: TextView
    private lateinit var scoreText: TextView
    private lateinit var timerTextView: TextView

    private val db = FirebaseFirestore.getInstance()
    private var quizList: MutableList<Map<String, String>> = mutableListOf()
    private var currentQuestionIndex = 0
    private var score = 0

    private var countDownTimer: CountDownTimer? = null
    private val timePerQuestion: Long = 30_000L // 30 seconds in ms

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_quiz)

        // Initialize UI components
        questionTextView = findViewById(R.id.questionTextView)
        optionARadio = findViewById(R.id.optionARadio)
        optionBRadio = findViewById(R.id.optionBRadio)
        optionCRadio = findViewById(R.id.optionCRadio)
        optionDRadio = findViewById(R.id.optionDRadio)
        radioGroup = findViewById(R.id.optionsRadioGroup)
        nextButton = findViewById(R.id.nextButton)
        submitButton = findViewById(R.id.submitButton)
        questionCounterText = findViewById(R.id.questionCounterText)
        scoreText = findViewById(R.id.scoreText)
        timerTextView = findViewById(R.id.timerTextView)

        fetchQuizzes()

        submitButton.setOnClickListener {
            if (radioGroup.checkedRadioButtonId == -1) {
                Toast.makeText(this, "Please select an answer", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val selectedOption = findViewById<RadioButton>(radioGroup.checkedRadioButtonId).text.toString()
            val correctAnswer = quizList[currentQuestionIndex]["correctAnswer"]

            if (selectedOption.equals(correctAnswer, ignoreCase = true)) {
                score++
            }

            countDownTimer?.cancel()

            submitButton.visibility = View.GONE
            nextButton.visibility = View.VISIBLE
        }

        nextButton.setOnClickListener {
            countDownTimer?.cancel()
            currentQuestionIndex++

            if (currentQuestionIndex < quizList.size) {
                showQuestion()
                nextButton.visibility = View.GONE
                submitButton.visibility = View.VISIBLE
            } else {
                showQuizOverPage()
            }
        }
    }

    private fun fetchQuizzes() {
        db.collection("quizzes")
            .get()
            .addOnSuccessListener { documents ->
                for (doc in documents) {
                    val quiz = mapOf(
                        "question" to (doc.getString("question") ?: ""),
                        "optionA" to (doc.getString("optionA") ?: ""),
                        "optionB" to (doc.getString("optionB") ?: ""),
                        "optionC" to (doc.getString("optionC") ?: ""),
                        "optionD" to (doc.getString("optionD") ?: ""),
                        "correctAnswer" to (doc.getString("correctAnswer") ?: "")
                    )
                    quizList.add(quiz)
                }

                if (quizList.isNotEmpty()) {
                    showQuestion()
                } else {
                    Toast.makeText(this, "No quizzes found", Toast.LENGTH_SHORT).show()
                    finish()
                }
            }
            .addOnFailureListener {
                Toast.makeText(this, "Failed to load quizzes", Toast.LENGTH_SHORT).show()
            }
    }

    private fun showQuestion() {
        val quiz = quizList[currentQuestionIndex]
        questionTextView.text = quiz["question"]
        optionARadio.text = quiz["optionA"]
        optionBRadio.text = quiz["optionB"]
        optionCRadio.text = quiz["optionC"]
        optionDRadio.text = quiz["optionD"]
        radioGroup.clearCheck()

        questionCounterText.text = "Question ${currentQuestionIndex + 1} of ${quizList.size}"
        scoreText.text = "Score: $score"

        startTimer()
    }

    private fun startTimer() {
        countDownTimer?.cancel()

        countDownTimer = object : CountDownTimer(timePerQuestion, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                val secondsLeft = millisUntilFinished / 1000
                timerTextView.text = "Time remaining: $secondsLeft sec"

                // Change text color if time is less than or equal to 10 seconds
                if (secondsLeft <= 10) {
                    timerTextView.setTextColor(Color.RED)
                } else {
                    timerTextView.setTextColor(Color.BLACK)
                }
            }

            override fun onFinish() {
                timerTextView.text = "Time's up!"
                timerTextView.setTextColor(Color.RED)

                if (currentQuestionIndex < quizList.size - 1) {
                    currentQuestionIndex++
                    showQuestion()
                    nextButton.visibility = View.GONE
                    submitButton.visibility = View.VISIBLE
                } else {
                    showQuizOverPage()
                }
            }
        }.start()
    }

    private fun showQuizOverPage() {
        countDownTimer?.cancel()

        val scoreMessage = "Quiz Complete! Your Score: $score/${quizList.size}"
        val scoreDialog = AlertDialog.Builder(this)
            .setTitle("Quiz Over")
            .setMessage(scoreMessage)
            .setPositiveButton("Go Back to Dashboard") { _, _ ->
                val intent = Intent(this, DashboardActivity::class.java)
                startActivity(intent)
                finish()
            }
            .create()

        scoreDialog.show()
    }
}