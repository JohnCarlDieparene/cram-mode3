package com.labactivity.crammode

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.labactivity.crammode.databinding.ActivityHistoryBinding
import com.labactivity.crammode.model.StudyHistory

class HistoryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHistoryBinding
    private val firestore = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHistoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.recyclerHistory.layoutManager = LinearLayoutManager(this)

        val currentUser = auth.currentUser

        if (currentUser == null) {
            Log.e("HistoryActivity", "No logged-in user")
            binding.txtEmptyMessage.text = "Please log in to view history."
            binding.txtEmptyMessage.visibility = View.VISIBLE
            binding.recyclerHistory.visibility = View.GONE
            return
        }

        val userId = currentUser.uid

        // ✅ Log for debug
        Log.d("HistoryActivity", "Fetching history for UID: $userId")

        firestore.collection("study_history")
            .whereEqualTo("uid", userId)
            .get()
            .addOnSuccessListener { result ->
                val list = result.toObjects(StudyHistory::class.java).sortedByDescending { it.timestamp }

                if (list.isEmpty()) {
                    binding.txtEmptyMessage.text = "No history found."
                    binding.txtEmptyMessage.visibility = View.VISIBLE
                    binding.recyclerHistory.visibility = View.GONE
                } else {
                    binding.txtEmptyMessage.visibility = View.GONE
                    binding.recyclerHistory.visibility = View.VISIBLE
                    binding.recyclerHistory.adapter = HistoryAdapter(list)
                }
            }
            .addOnFailureListener { e ->
                Log.e("HistoryActivity", "Firestore fetch failed: ${e.message}", e)
                binding.txtEmptyMessage.text = "Failed to load history: ${e.message}"
                binding.txtEmptyMessage.visibility = View.VISIBLE
                binding.recyclerHistory.visibility = View.GONE
            }
    }
}
