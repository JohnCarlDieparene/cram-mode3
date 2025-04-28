package com.labactivity.crammode

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth

class DashboardActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)

        auth = FirebaseAuth.getInstance()

        val welcomeText = findViewById<TextView>(R.id.welcomeText)
        val startReviewButton = findViewById<Button>(R.id.startReviewButton)
        val logoutButton = findViewById<Button>(R.id.logoutButton)

        val user = auth.currentUser
        welcomeText.text = "Welcome, ${user?.email}"

        val reviewButton = findViewById<Button>(R.id.startReviewButton)
        val flashcardButton = findViewById<Button>(R.id.flashcardButton)
        val quizButton: Button = findViewById(R.id.quizButton)

        quizButton.setOnClickListener {
            val intent = Intent(this, QuizActivity::class.java)
            startActivity(intent)
        }

        flashcardButton.setOnClickListener {
            startActivity(Intent(this, FlashcardActivity::class.java))
        }

        reviewButton.setOnClickListener {
            startActivity(Intent(this, ReviewActivity::class.java))
        }


        startReviewButton.setOnClickListener {
            Toast.makeText(this, "Start reviewing coming soon!", Toast.LENGTH_SHORT).show()
        }

        logoutButton.setOnClickListener {
            auth.signOut()
            startActivity(Intent(this, AuthActivity::class.java))
            finish()
        }
    }
}
