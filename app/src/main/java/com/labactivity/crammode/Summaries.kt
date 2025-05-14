package com.labactivity.crammode

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity

class Summaries : AppCompatActivity()  {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_summaries)

        val imageDash10 = findViewById<ImageView>(R.id.imageDash10)
        val imageUser10 = findViewById<ImageView>(R.id.imageUser10)
        val summaryButton1 = findViewById<Button>(R.id.summaryButton1)

        imageDash10.setOnClickListener {
            val intent = Intent(this, DashboardActivity::class.java)
            startActivity(intent)
        }

        imageUser10.setOnClickListener {
            val intent = Intent(this, Profile::class.java)
            startActivity(intent)
        }

        summaryButton1.setOnClickListener {
            startActivity(Intent(this, SummaryActivity::class.java))
        }

    }
}