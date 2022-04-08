package com.example.mtaa.adapters

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.mtaa.MeetingSettingsActivity
import com.example.mtaa.R
import com.example.mtaa.models.MeetingResponse
import com.example.mtaa.utilities.Utils

class MeetingsAdapter(val meetings: List<MeetingResponse>) :
    RecyclerView.Adapter<MeetingsAdapter.MeetingViewHolder>() {

    class MeetingViewHolder(val view: View) : RecyclerView.ViewHolder(view)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MeetingViewHolder {
        return MeetingViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.meeting_layout, parent, false)
        )
    }

    override fun getItemCount() = meetings.size

    override fun onBindViewHolder(holder: MeetingViewHolder, position: Int) {
        val meeting = meetings[position]

        val tvTitle = holder.view.findViewById<TextView>(R.id.tvTitle)
        val tvTime = holder.view.findViewById<TextView>(R.id.tvTime)
        val tvDuration = holder.view.findViewById<TextView>(R.id.tvDuration)

        holder.view.setOnClickListener {
            val intent = Intent(holder.view.context, MeetingSettingsActivity::class.java)
            intent.putExtra("meeting", meeting)
            holder.view.context.startActivity(intent)
        }

        // fill text views
        val title = meeting.title
        val time = Utils.formatTime(meeting.date)
        val duration = "${meeting.duration} minutes"

        tvTitle.text = title
        tvTime.text = time
        tvDuration.text = duration
    }
}