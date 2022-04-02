package com.example.mtaa

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class StartCallActivity : AppCompatActivity() {

    private lateinit var btnStartCall: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_start_call)

        btnStartCall = findViewById(R.id.btnStartCall)
    }
}