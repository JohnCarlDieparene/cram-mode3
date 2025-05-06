package com.labactivity.crammode

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class CreateSummaryActivity : AppCompatActivity() {

    private lateinit var topicInput: EditText
    private lateinit var keyTermsInput: EditText
    private lateinit var importantDatesInput: EditText
    private lateinit var summaryInput: EditText
    private lateinit var saveButton: Button
    private lateinit var db: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_summary)

        topicInput = findViewById(R.id.topicInput)
        keyTermsInput = findViewById(R.id.keyTermsInput)
        importantDatesInput = findViewById(R.id.importantDatesInput)
        summaryInput = findViewById(R.id.summaryInput)
        saveButton = findViewById(R.id.saveSummaryButton)
        db = FirebaseFirestore.getInstance()

        val viewSummariesButton = findViewById<Button>(R.id.viewSummariesButton)

        viewSummariesButton.setOnClickListener {
            val intent = Intent(this, DisplaySummariesActivity::class.java)
            startActivity(intent)
        }

        saveButton.setOnClickListener {
            saveSummaryToFirebase()
            val intent = Intent(this, DisplaySummariesActivity::class.java)
            startActivity(intent)
             //
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

        // Save summary in Firestore under the user's UID
        val summary = Summary(topic, keyTerms, importantDates, summaryText)
        db.collection("summaries")
            .document(userId)
            .collection("user_summaries")
            .document(topic)  // You can use topic as document ID
            .set(summary)
            .addOnSuccessListener {
                Toast.makeText(this, "Summary saved!", Toast.LENGTH_SHORT).show()
                 // Optionally close the activity after saving
            }
            .addOnFailureListener {
                Toast.makeText(this, "Failed to save summary.", Toast.LENGTH_SHORT).show()
            }
    }
}
