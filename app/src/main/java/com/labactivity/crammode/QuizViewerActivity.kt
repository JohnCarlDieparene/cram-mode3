package com.labactivity.crammode

import android.graphics.Color
import android.os.*
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.labactivity.crammode.model.QuizQuestion

class QuizViewerActivity : AppCompatActivity() {

    private lateinit var txtQuestion: TextView
    private lateinit var txtScore: TextView
    private lateinit var txtFeedback: TextView
    private lateinit var txtTimer: TextView
    private lateinit var progressBar: ProgressBar

    private lateinit var radioGroup: RadioGroup
    private lateinit var optionA: RadioButton
    private lateinit var optionB: RadioButton
    private lateinit var optionC: RadioButton
    private lateinit var optionD: RadioButton

    private lateinit var btnSubmit: Button
    private lateinit var btnNext: Button

    private var quizList: List<QuizQuestion> = emptyList()
    private var currentIndex = 0
    private var score = 0
    private var answered = false

    private var countDownTimer: CountDownTimer? = null
    private var questionTimeMillis: Long = 15000L // default: 15s

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_quiz_viewer)

        // UI Bindings
        txtQuestion = findViewById(R.id.txtQuestion)
        txtScore = findViewById(R.id.txtScore)
        txtFeedback = findViewById(R.id.txtFeedback)
        txtTimer = findViewById(R.id.txtTimer)
        progressBar = findViewById(R.id.progressBar)

        radioGroup = findViewById(R.id.radioGroup)
        optionA = findViewById(R.id.optionA)
        optionB = findViewById(R.id.optionB)
        optionC = findViewById(R.id.optionC)
        optionD = findViewById(R.id.optionD)

        btnSubmit = findViewById(R.id.btnSubmit)
        btnNext = findViewById(R.id.btnNext)

        // Get data
        quizList = intent.getParcelableArrayListExtra("quizQuestions") ?: emptyList()
        val selectedTimeOption = intent.getStringExtra("timePerQuestion") ?: "medium"
        questionTimeMillis = when (selectedTimeOption) {
            "easy" -> 30000L // 30s
            "medium" -> 15000L // 15s
            "hard" -> 10000L // 10s
            else -> 15000L
        }

        // Start quiz
        if (quizList.isNotEmpty()) {
            updateProgress()
            showQuestion()
        } else {
            txtQuestion.text = "No quiz data found."
            btnSubmit.isEnabled = false
        }

        btnSubmit.setOnClickListener {
            if (!answered) checkAnswer()
        }

        btnNext.setOnClickListener {
            if (currentIndex < quizList.size - 1) {
                currentIndex++
                showQuestion()
                updateProgress()
                answered = false
                txtFeedback.text = ""
                btnNext.visibility = View.GONE
            } else {
                Toast.makeText(this, "Quiz Finished! Score: $score / ${quizList.size}", Toast.LENGTH_LONG).show()
                finish()
            }
        }
    }

    private fun updateProgress() {
        val percent = ((currentIndex + 1).toFloat() / quizList.size * 100).toInt()
        progressBar.progress = percent
        txtScore.text = "Score: $score"
    }

    private fun showQuestion() {
        val q = quizList[currentIndex]
        txtQuestion.text = "Q${currentIndex + 1}: ${q.question}"
        optionA.text = q.options.getOrElse(0) { "" }
        optionB.text = q.options.getOrElse(1) { "" }
        optionC.text = q.options.getOrElse(2) { "" }
        optionD.text = q.options.getOrElse(3) { "" }

        radioGroup.clearCheck()
        setOptionsEnabled(true)
        resetOptionColors()
        txtFeedback.text = ""
        answered = false
        btnNext.visibility = View.GONE

        startQuestionTimer()
    }

    private fun startQuestionTimer() {
        countDownTimer?.cancel()

        countDownTimer = object : CountDownTimer(questionTimeMillis, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                txtTimer.text = "Time left: ${millisUntilFinished / 1000}s"
            }

            override fun onFinish() {
                txtTimer.text = "⏰ Time's up!"
                setOptionsEnabled(false)
                autoSubmit()
            }
        }.start()
    }

    private fun checkAnswer() {
        val selectedId = radioGroup.checkedRadioButtonId
        if (selectedId != -1) {
            val selectedRadio = findViewById<RadioButton>(selectedId)
            val selectedAnswer = selectedRadio.text.toString()
            val correctAnswer = quizList[currentIndex].answer

            countDownTimer?.cancel()
            setOptionsEnabled(false)

            if (selectedAnswer == correctAnswer) {
                txtFeedback.setTextColor(Color.GREEN)
                txtFeedback.text = "✅ Correct!"
                score++
            } else {
                txtFeedback.setTextColor(Color.RED)
                txtFeedback.text = "❌ Incorrect. Correct answer: $correctAnswer"
            }

            updateProgress()
            answered = true
            btnNext.visibility = View.VISIBLE
        } else {
            Toast.makeText(this, "Please select an answer", Toast.LENGTH_SHORT).show()
        }
    }

    private fun autoSubmit() {
        val correctAnswer = quizList[currentIndex].answer
        txtFeedback.setTextColor(Color.RED)
        txtFeedback.text = "⏰ Time's up! Correct answer: $correctAnswer"

        updateProgress()
        answered = true
        btnNext.visibility = View.VISIBLE
    }

    private fun setOptionsEnabled(enabled: Boolean) {
        for (i in 0 until radioGroup.childCount) {
            radioGroup.getChildAt(i).isEnabled = enabled
        }
    }

    private fun resetOptionColors() {
        val defaultColor = Color.BLACK
        optionA.setTextColor(defaultColor)
        optionB.setTextColor(defaultColor)
        optionC.setTextColor(defaultColor)
        optionD.setTextColor(defaultColor)
    }

    override fun onDestroy() {
        super.onDestroy()
        countDownTimer?.cancel()
    }
}
