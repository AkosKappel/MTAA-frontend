package com.example.mtaa

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.*

class ManageContactsActivity : AppCompatActivity() {

    private lateinit var etContactId: EditText
    private lateinit var btnRemoveContact: Button
    private lateinit var btnAddContact: Button
    private lateinit var btnHome: TextView
    private lateinit var btnBack: ImageView
    private lateinit var btnProfile: ImageView

    companion object {
        private const val TAG: String = "ManageContactsActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_manage_contacts)

        btnRemoveContact = findViewById(R.id.btnRemoveContact)
        btnAddContact = findViewById(R.id.btnAddContact)
        btnHome = findViewById(R.id.btnHome)
        btnProfile = findViewById(R.id.btnProfile)
        btnBack = findViewById(R.id.btnBack)

        btnHome.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        btnProfile.setOnClickListener {
            val intent = Intent(applicationContext, ProfileActivity::class.java)
            startActivity(intent)
        }

        btnBack.setOnClickListener { finish() }
    }
}