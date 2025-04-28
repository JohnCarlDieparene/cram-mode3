package com.labactivity.crammode

import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.FirebaseFirestore
class QuizActivity : AppCompatActivity() {

    private lateinit var questionText: TextView
    private lateinit var answerOptions: RadioGroup
    private lateinit var submitButton: Button
    private lateinit var scoreText: TextView
    private lateinit var nextButton: Button
    private lateinit var quizOverLayout: View
    private lateinit var quizOverText: TextView
    private lateinit var finalScoreText: TextView
    private lateinit var returnButton: Button
    private lateinit var backButton: ImageButton
    private lateinit var questionCounterText: TextView
    private lateinit var timerTextView: TextView // Timer TextView

    private var currentQuestionIndex = 0
    private var score = 0
    private val questions = mutableListOf<Question>()
    private var countDownTimer: CountDownTimer? = null
    private var timeLeftInMillis: Long = 30000 // 30 seconds for each question
    private var isQuizOver = false // Track if the quiz is over

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_quiz)

        // Initialize the views
        questionText = findViewById(R.id.questionText)
        answerOptions = findViewById(R.id.answerOptions)
        submitButton = findViewById(R.id.submitButton)
        scoreText = findViewById(R.id.scoreText)
        nextButton = findViewById(R.id.nextButton)
        quizOverLayout = findViewById(R.id.quizOverLayout)
        quizOverText = findViewById(R.id.quizOverText)
        finalScoreText = findViewById(R.id.finalScoreText)
        returnButton = findViewById(R.id.returnButton)
        backButton = findViewById(R.id.backButton)
        questionCounterText = findViewById(R.id.questionCounterText)
        timerTextView = findViewById(R.id.timerTextView)

        // Set initial visibility
        quizOverLayout.visibility = View.GONE
        returnButton.visibility = View.GONE

        // Load questions from Firebase
        loadQuestionsFromFirebase()

        submitButton.setOnClickListener {
            val selectedRadioButtonId = answerOptions.checkedRadioButtonId
            if (selectedRadioButtonId != -1) {
                val selectedRadioButton = findViewById<RadioButton>(selectedRadioButtonId)
                checkAnswer(selectedRadioButton.text.toString())
            } else {
                Toast.makeText(this, "Please select an answer.", Toast.LENGTH_SHORT).show()
            }
        }

        nextButton.setOnClickListener {
            currentQuestionIndex++
            if (currentQuestionIndex < questions.size) {
                loadQuestion()
            } else {
                showFinalScore()
            }
            startTimer()
        }

        backButton.setOnClickListener {
            onBackPressed()
        }

        returnButton.setOnClickListener {
            finish()
        }
    }

    private fun loadQuestionsFromFirebase() {
        val db = FirebaseFirestore.getInstance()
        db.collection("quizzes")
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    val questionText = document.getString("question") ?: ""
                    val options = document.get("options") as? List<String> ?: emptyList()
                    val correctAnswer = document.getString("correctAnswer") ?: ""
                    if (questionText.isNotEmpty() && options.isNotEmpty() && correctAnswer.isNotEmpty()) {
                        questions.add(Question(questionText, options, correctAnswer))
                    }
                }
                if (questions.isNotEmpty()) {
                    loadQuestion()
                } else {
                    Toast.makeText(this, "No quiz data available.", Toast.LENGTH_SHORT).show()
                }
            }
            .addOnFailureListener { exception ->
                Toast.makeText(this, "Failed to load quiz.", Toast.LENGTH_SHORT).show()
            }
    }

    private fun loadQuestion() {
        val question = questions[currentQuestionIndex]
        questionText.text = question.questionText

        answerOptions.removeAllViews()
        for (answer in question.answers) {
            val radioButton = RadioButton(this)
            radioButton.text = answer
            radioButton.textSize = 16f
            answerOptions.addView(radioButton)
        }
        questionCounterText.text = "Question ${currentQuestionIndex + 1} of ${questions.size}"

        // Cancel the previous timer if it's running, and reset time
        countDownTimer?.cancel()
        timeLeftInMillis = 30000 // Reset to 30 seconds
        startTimer()

        submitButton.visibility = View.VISIBLE
        nextButton.visibility = View.GONE
    }

    private fun startTimer() {
        // Start a new countdown timer if the activity is still active
        if (!isQuizOver && !isFinishing) {
            countDownTimer = object : CountDownTimer(timeLeftInMillis, 1000) {
                override fun onTick(millisUntilFinished: Long) {
                    timeLeftInMillis = millisUntilFinished
                    updateTimerUI()
                }

                override fun onFinish() {
                    timeLeftInMillis = 0
                    updateTimerUI()
                    if (!isQuizOver) {
                        Toast.makeText(this@QuizActivity, "Time's up!", Toast.LENGTH_SHORT).show()
                        checkAnswer("") // Automatically submit the answer if time runs out
                    }
                }
            }.start()
        }
    }

    private fun updateTimerUI() {
        val secondsLeft = (timeLeftInMillis / 1000).toInt()
        timerTextView.text = "Time remaining: $secondsLeft sec"
    }

    private fun checkAnswer(selectedAnswer: String) {
        val correctAnswer = questions[currentQuestionIndex].correctAnswer
        if (selectedAnswer == correctAnswer) {
            score++
        }
        scoreText.text = "Score: $score"
        submitButton.visibility = View.GONE
        nextButton.visibility = View.VISIBLE
    }

    private fun showFinalScore() {
        // Stop the timer and hide the timer UI
        countDownTimer?.cancel()
        timerTextView.visibility = View.GONE // Hide the timer

        questionText.visibility = View.GONE
        answerOptions.visibility = View.GONE
        submitButton.visibility = View.GONE
        nextButton.visibility = View.GONE
        scoreText.visibility = View.GONE
        backButton.visibility = View.GONE

        quizOverLayout.visibility = View.VISIBLE
        finalScoreText.text = "Final Score: $score"
        returnButton.visibility = View.VISIBLE

        isQuizOver = true // Set quiz over flag to true to prevent further Toasts
    }

    override fun onDestroy() {
        super.onDestroy()
        // Cancel timer to avoid memory leaks
        countDownTimer?.cancel()
    }

    data class Question(val questionText: String, val answers: List<String>, val correctAnswer: String)
}

