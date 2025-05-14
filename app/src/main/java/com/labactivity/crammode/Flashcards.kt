package com.labactivity.crammode

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity

class Flashcards : AppCompatActivity(){

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_flashcards)

        val imageDash7 = findViewById<ImageView>(R.id.imageDash8)
        val imageUser7 = findViewById<ImageView>(R.id.imageUser8)
        val flashcardButton1 = findViewById<Button>(R.id.flashcardButton1)

        imageDash7.setOnClickListener {
            val intent = Intent(this, DashboardActivity::class.java)
            startActivity(intent)
        }

        imageUser7.setOnClickListener {
            val intent = Intent(this, Profile::class.java)
            startActivity(intent)
        }

        flashcardButton1.setOnClickListener {
            startActivity(Intent(this, FlashcardActivity::class.java))
        }

    }
}