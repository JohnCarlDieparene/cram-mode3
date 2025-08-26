package com.labactivity.crammode

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth

class RegisterActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        auth = FirebaseAuth.getInstance()

        val emailEditText = findViewById<EditText>(R.id.emailEditText)
        val passwordEditText = findViewById<EditText>(R.id.passwordEditText)
        val registerButton = findViewById<Button>(R.id.registerButton)
        val loginRedirectTextView = findViewById<TextView>(R.id.loginRedirectTextView)

        // Redirect to LoginActivity when the user clicks "Already have an account? Login"
        loginRedirectTextView.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()  // Optional: finish RegisterActivity to prevent going back to it
        }

        // Handle registration logic
        registerButton.setOnClickListener {
            val email = emailEditText.text.toString()
            val password = passwordEditText.text.toString()

            if (!email.matches(Regex("^[A-Za-z0-9._%+-]+@email\\.com$"))) {
                emailEditText.error = "Email must end with @email.com"
                return@setOnClickListener
            }

            if (password.length < 6) {
                passwordEditText.error = "Password must be at least 6 characters"
                return@setOnClickListener
            }

            registerUser(email, password)
        }

    }

    private fun registerUser(email: String, password: String) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val user = auth.currentUser
                    Toast.makeText(this, "Registered: ${user?.email}", Toast.LENGTH_SHORT).show()
                    // Redirect to Dashboard
                    startActivity(Intent(this, OCRActivity::class.java))
                    finish()
                } else {
                    Toast.makeText(this, "Registration failed: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                }
            }
    }
}
