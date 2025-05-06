package com.labactivity.crammode

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query

class DisplaySummariesActivity : AppCompatActivity() {

    private lateinit var adapter: SummaryAdapter
    private lateinit var summaryRecyclerView: RecyclerView  // RecyclerView to display summaries

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_display_summaries) // Make sure this layout has a RecyclerView

        summaryRecyclerView = findViewById(R.id.summaryRecyclerView)  // Initialize RecyclerView
        setupRecyclerView()
    }

    private fun setupRecyclerView() {
        val userId = FirebaseAuth.getInstance().currentUser?.uid ?: return

        // Query to get the summaries for the logged-in user
        val query: Query = FirebaseFirestore.getInstance()
            .collection("summaries")
            .document(userId)
            .collection("user_summaries")
            .orderBy("topic", Query.Direction.ASCENDING)

        // Set up FirestoreRecyclerOptions for the adapter
        val options = FirestoreRecyclerOptions.Builder<Summary>()
            .setQuery(query, Summary::class.java)
            .setLifecycleOwner(this) // Ensures the adapter is lifecycle-aware
            .build()

        adapter = SummaryAdapter(options)  // Initialize the adapter
        summaryRecyclerView.layoutManager = LinearLayoutManager(this)  // Set layout manager for RecyclerView
        summaryRecyclerView.adapter = adapter  // Set the adapter to RecyclerView
    }

    override fun onStart() {
        super.onStart()
        adapter.startListening()  // Start listening for updates from Firestore
    }

    override fun onStop() {
        super.onStop()
        adapter.stopListening()  // Stop listening when activity is stopped
    }
}
