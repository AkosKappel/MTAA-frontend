package com.example.mtaa

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class ManageUsersActivity : AppCompatActivity() {

    private lateinit var btnRemoveUser: Button
    private lateinit var btnAddUser: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_manage_users)

        btnRemoveUser = findViewById(R.id.btnRemoveUser)
        btnAddUser = findViewById(R.id.btnAddUser)
    }
}