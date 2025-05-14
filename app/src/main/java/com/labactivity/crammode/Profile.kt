package com.labactivity.crammode

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth

class Profile : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        // Link the views using findViewById
        val imageDash6 = findViewById<ImageView>(R.id.imageDash6)
        val imageUser6 = findViewById<ImageView>(R.id.imageUser6)
        val logoutButton = findViewById<Button>(R.id.logoutButton)

        imageDash6.setOnClickListener {
            val intent = Intent(this, DashboardActivity::class.java)
            startActivity(intent)
        }

        imageUser6.setOnClickListener {
            val intent = Intent(this, Profile::class.java)
            startActivity(intent)
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
