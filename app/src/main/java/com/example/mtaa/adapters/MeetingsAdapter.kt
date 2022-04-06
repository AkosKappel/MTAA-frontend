package com.example.mtaa.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.mtaa.R
import com.example.mtaa.models.MeetingResponse

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
        val tvTitle = holder.view.findViewById<TextView>(R.id.tvTitle)
        val tvTime = holder.view.findViewById<TextView>(R.id.tvTime)
        val tvDuration = holder.view.findViewById<TextView>(R.id.tvDuration)
        val btnDetails = holder.view.findViewById<Button>(R.id.btnDetail)

        val meeting = meetings[position]
        val hours = meeting.date.hours.toString().padStart(2, '0')
        val minutes = meeting.date.minutes.toString().padStart(2, '0')
        val time = "$hours:$minutes"
        val duration = "${meeting.duration} minutes"

        tvTitle.text = meeting.title
        tvTime.text = time
        tvDuration.text = duration

        btnDetails.setOnClickListener {
            // TODO: open meeting details
        }
    }
}