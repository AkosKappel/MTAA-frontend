package com.example.mtaa

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import com.example.mtaa.webrtc.Constants
import com.google.android.material.button.MaterialButton
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class CallActivity : AppCompatActivity() {

    val db = Firebase.firestore
    private lateinit var start_meeting: MaterialButton
    private lateinit var join_meeting: MaterialButton
    private lateinit var meeting_id: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_start_call)
//        setContentView(R.layout.activity_start)

        start_meeting = findViewById(R.id.start_meeting)
        join_meeting = findViewById(R.id.join_meeting)
        meeting_id = findViewById(R.id.meeting_id)

        Constants.isIntiatedNow = true
        Constants.isCallEnded = true
        start_meeting.setOnClickListener {
            val meetingId = meeting_id.text.toString().trim()
            if (meetingId.isEmpty() || meetingId.toIntOrNull() == null) {
                meeting_id.error = "Please enter valid meeting id"
            } else {
                db.collection("calls")
                        .document(meeting_id.text.toString())
                        .get()
                        .addOnSuccessListener {
                            if (it["type"]=="OFFER" || it["type"]=="ANSWER" || it["type"]=="END_CALL") {
                                meeting_id.error = "Please enter new meeting ID"
                            } else {
                                val intent = Intent(this@CallActivity, RTCActivity::class.java)
                                intent.putExtra("meetingID", meetingId)
                                intent.putExtra("isJoin",false)
                                startActivity(intent)
                            }
                        }
                        .addOnFailureListener {
                            meeting_id.error = "Please enter new meeting ID"
                        }
            }
        }
        join_meeting.setOnClickListener {
            val meetingId = meeting_id.text.toString().trim()
            if (meetingId.isEmpty() || meetingId.toIntOrNull() == null) {
                meeting_id.error = "Please enter valid meeting id"
            } else {
                val intent = Intent(this@CallActivity, RTCActivity::class.java)
                intent.putExtra("meetingID", meetingId)
                intent.putExtra("isJoin",true)
                startActivity(intent)
            }
        }
    }
}