package com.labactivity.crammode

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.labactivity.crammode.com.labactivity.crammode.Summary

class CreateSummaryActivity : AppCompatActivity() {

    private lateinit var topicInput: EditText
    private lateinit var keyTermsInput: EditText
    private lateinit var importantDatesInput: EditText
    private lateinit var summaryInput: EditText
    private lateinit var saveButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_summary)

        topicInput = findViewById(R.id.topicInput)
        keyTermsInput = findViewById(R.id.keyTermsInput)
        importantDatesInput = findViewById(R.id.importantDatesInput)
        summaryInput = findViewById(R.id.summaryInput)
        saveButton = findViewById(R.id.saveSummaryButton)

        saveButton.setOnClickListener {
            saveSummaryToFirebase()
        }
    }

    private fun saveSummaryToFirebase() {
        val topic = topicInput.text.toString().trim()
        val keyTerms = keyTermsInput.text.toString().trim()
        val importantDates = importantDatesInput.text.toString().trim()
        val summaryText = summaryInput.text.toString().trim()

        if (topic.isEmpty() || summaryText.isEmpty()) {
            Toast.makeText(this, "Please fill in the topic and summary.", Toast.LENGTH_SHORT).show()
            return
        }

        val userId = FirebaseAuth.getInstance().currentUser?.uid ?: return
        val summary = Summary(topic, keyTerms, importantDates, summaryText)

        val ref = FirebaseDatabase.getInstance().getReference("summaries").child(userId).child(topic)

        ref.setValue(summary).addOnSuccessListener {
            Toast.makeText(this, "Summary saved!", Toast.LENGTH_SHORT).show()
            finish() // Optional: go back after saving
        }.addOnFailureListener {
            Toast.makeText(this, "Failed to save summary.", Toast.LENGTH_SHORT).show()
        }
    }
}
