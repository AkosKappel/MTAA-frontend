package com.example.mtaa

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.*
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mtaa.adapters.ContactsAdapter
import com.example.mtaa.adapters.MeetingsAdapter
import com.example.mtaa.api.ApiClient
import com.example.mtaa.models.ContactList
import com.example.mtaa.models.MeetingResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

class ManageContactsActivity : AppCompatActivity() {

    private lateinit var etContactId: EditText
    private lateinit var rvContacts: RecyclerView
    private lateinit var btnRemoveContact: Button
    private lateinit var btnAddContact: Button
    private lateinit var btnHome: TextView
    private lateinit var btnBack: ImageView
    private lateinit var btnProfile: ImageView

    private lateinit var allContacts: List<ContactList>

    companion object {
        private const val TAG: String = "ManageContactsActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_manage_contacts)

        etContactId = findViewById(R.id.etContactId)
        btnRemoveContact = findViewById(R.id.btnRemoveContact)
        rvContacts = findViewById(R.id.rvContacts)
        btnAddContact = findViewById(R.id.btnAddContact)
        btnHome = findViewById(R.id.btnHome)
        btnProfile = findViewById(R.id.btnProfile)
        btnBack = findViewById(R.id.btnBack)

        fetchContacts()

        btnHome.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        btnProfile.setOnClickListener {
            val intent = Intent(applicationContext, ProfileActivity::class.java)
            startActivity(intent)
        }

        btnBack.setOnClickListener { finish() }

        btnAddContact.setOnClickListener {
            addContact()
        }

        btnRemoveContact.setOnClickListener {
            removeContact()
        }
    }

    private fun addContact() {
        ApiClient.getApiService(applicationContext)
            .addContact(etContactId.text.toString().trim().toInt())
            .enqueue(object : Callback<List<ContactList>> {
                override fun onFailure(call: Call<List<ContactList>>, t: Throwable) {
                    handleFailure(t)
                }

                override fun onResponse(
                    call: Call<List<ContactList>>, response: Response<List<ContactList>>
                ) {
                    if (response.isSuccessful) {
                        handleSuccessfulResponse(response)
                    } else {
                        handleNotSuccessfulResponse(response)
                    }
                }
            })
    }

    private fun removeContact() {
        ApiClient.getApiService(applicationContext)
            .removeContact(etContactId.text.toString().trim().toInt())
            .enqueue(object : Callback<List<ContactList>> {
                override fun onFailure(call: Call<List<ContactList>>, t: Throwable) {
                    handleFailure(t)
                }

                override fun onResponse(
                    call: Call<List<ContactList>>, response: Response<List<ContactList>>
                ) {
                    if (response.isSuccessful) {
                        handleSuccessfulResponse(response)
                    } else {
                        handleNotSuccessfulResponse(response)
                    }
                }
            })
    }


    private fun fetchContacts() {
        ApiClient.getApiService(applicationContext)
            .getContacts()
            .enqueue(object : Callback<List<ContactList>> {
                override fun onFailure(call: Call<List<ContactList>>, t: Throwable) {
                    handleFailure(t)
                }

                override fun onResponse(
                    call: Call<List<ContactList>>, response: Response<List<ContactList>>
                ) {
                    if (response.isSuccessful) {
                        handleSuccessfulResponse(response)
                    } else {
                        handleNotSuccessfulResponse(response)
                    }
                }
            })
    }

    private fun handleFailure(t: Throwable) {
        Log.d(TAG, "onFailure: ${t.message.toString()}")
        Toast.makeText(applicationContext, t.message, Toast.LENGTH_LONG).show()
    }

    private fun handleSuccessfulResponse(response: Response<List<ContactList>>) {
        allContacts = response.body()!!
        Log.d(TAG, "Received ${allContacts.size} contacts")
        showContacts(allContacts)
    }

    private fun handleNotSuccessfulResponse(response: Response<List<ContactList>>) {
        val msg = "${response.code()} ${response.errorBody()!!.string()}"
        Log.d(TAG, "onResponse: $msg")
        Toast.makeText(applicationContext, "Error: $msg", Toast.LENGTH_LONG).show()
    }

    private fun showContacts(contacts: List<ContactList>) {
        rvContacts.layoutManager = LinearLayoutManager(applicationContext)
        rvContacts.adapter = ContactsAdapter(contacts)
    }
}