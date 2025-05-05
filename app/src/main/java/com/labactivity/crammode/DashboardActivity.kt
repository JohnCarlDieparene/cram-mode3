package com.labactivity.crammode

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
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
        val createButton = findViewById<Button>(R.id.createButton)

        createButton.setOnClickListener {
            startActivity(Intent(this, CreateActivity::class.java))
        }

        quizButton.setOnClickListener {
            startActivity(Intent(this, QuizActivity::class.java))
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
            signOut()
        }
    }

    private fun signOut() {
        // Sign out from Firebase
        auth.signOut()

        // Also sign out from Google
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        val googleSignInClient = GoogleSignIn.getClient(this, gso)
        googleSignInClient.signOut().addOnCompleteListener {
            val intent = Intent(this, LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            finish()
        }
    }
}
