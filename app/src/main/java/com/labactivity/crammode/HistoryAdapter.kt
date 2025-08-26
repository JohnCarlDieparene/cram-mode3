package com.labactivity.crammode

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.labactivity.crammode.model.StudyHistory
import java.text.SimpleDateFormat
import java.util.*

class HistoryAdapter(private val items: List<StudyHistory>) :
    RecyclerView.Adapter<HistoryAdapter.HistoryViewHolder>() {

    inner class HistoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val txtDate: TextView = itemView.findViewById(R.id.txtDate)
        val txtInput: TextView = itemView.findViewById(R.id.txtInput)
        val txtOutput: TextView = itemView.findViewById(R.id.txtOutput)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_study_history, parent, false)
        return HistoryViewHolder(view)
    }

    override fun onBindViewHolder(holder: HistoryViewHolder, position: Int) {
        val item = items[position]
        val sdf = SimpleDateFormat("MMM dd, yyyy hh:mm a", Locale.getDefault())

        holder.txtDate.text = sdf.format(Date(item.timestamp))
        holder.txtInput.text = "Input:\n${item.inputText.trim()}"
        holder.txtOutput.text = "Result:\n${item.resultText.trim()}"
    }

    override fun getItemCount(): Int = items.size
}
