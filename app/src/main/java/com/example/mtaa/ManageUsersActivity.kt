package com.example.mtaa

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.*
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mtaa.adapters.ContactsAdapter
import com.example.mtaa.api.ApiClient
import com.example.mtaa.models.ContactList
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ManageUsersActivity : AppCompatActivity() {

    private lateinit var btnRemoveUser: Button
    private lateinit var btnAddUser: Button
    private lateinit var btnHome: TextView
    private lateinit var btnBack: ImageView
    private lateinit var btnProfile: ImageView
    private lateinit var rvContacts: RecyclerView
    private lateinit var rvUsers: RecyclerView
    private lateinit var etUserId: EditText


    private lateinit var allContacts: List<ContactList>
    private lateinit var allUsers: List<ContactList>

    companion object {
        private const val TAG: String = "ManageUsersActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_manage_users)

        rvContacts = findViewById(R.id.rvContacts)
        rvUsers = findViewById(R.id.rvUsers)
        etUserId = findViewById(R.id.etUserId)
        btnRemoveUser = findViewById(R.id.btnRemoveUser)
        btnAddUser = findViewById(R.id.btnAddUser)
        btnHome = findViewById(R.id.btnHome)
        btnProfile = findViewById(R.id.btnProfile)
        btnBack = findViewById(R.id.btnBack)

        fetchContacts()
//        fetchUsers()

        btnHome.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        btnProfile.setOnClickListener {
            val intent = Intent(applicationContext, ProfileActivity::class.java)
            startActivity(intent)
        }

        btnBack.setOnClickListener { finish() }

//        btnAddUser.setOnClickListener { addUser() }
//
//        btnRemoveUser.setOnClickListener { removeUser() }

    }

//    private fun addUser() {
//        ApiClient.getApiService(applicationContext)
//            .addUserToCall(,etUserId.text.toString().trim().toInt())
//            .enqueue(object : Callback<List<ContactList>> {
//                override fun onFailure(call: Call<List<ContactList>>, t: Throwable) {
//                    handleFailure(t)
//                }
//
//                override fun onResponse(
//                    call: Call<List<ContactList>>, response: Response<List<ContactList>>
//                ) {
//                    if (response.isSuccessful) {
//                        handleSuccessfulResponse(response)
//                    } else {
//                        handleNotSuccessfulResponse(response)
//                    }
//                }
//            })
//    }
//
//    private fun removeUser() {
//        ApiClient.getApiService(applicationContext)
//            .removeUserFromCall(,etUserId.text.toString().trim().toInt())
//            .enqueue(object : Callback<List<ContactList>> {
//                override fun onFailure(call: Call<List<ContactList>>, t: Throwable) {
//                    handleFailure(t)
//                }
//
//                override fun onResponse(
//                    call: Call<List<ContactList>>, response: Response<List<ContactList>>
//                ) {
//                    if (response.isSuccessful) {
//                        handleSuccessfulResponse(response)
//                    } else {
//                        handleNotSuccessfulResponse(response)
//                    }
//                }
//            })
//    }


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
                        handleSuccessfulResponseContacts(response)
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

    private fun handleSuccessfulResponseContacts(response: Response<List<ContactList>>) {
        allContacts = response.body()!!
        Log.d(TAG, "Received ${allContacts.size} contacts")
        showContacts(allContacts)
    }

    private fun showContacts(contacts: List<ContactList>) {
        rvContacts.layoutManager = LinearLayoutManager(applicationContext)
        rvContacts.adapter = ContactsAdapter(contacts)
    }

//    private fun fetchUsers() {
//        ApiClient.getApiService(applicationContext)
//            .getUsersOfCall()
//            .enqueue(object : Callback<List<ContactList>> {
//                override fun onFailure(call: Call<List<ContactList>>, t: Throwable) {
//                    handleFailure(t)
//                }
//
//                override fun onResponse(
//                    call: Call<List<ContactList>>, response: Response<List<ContactList>>
//                ) {
//                    if (response.isSuccessful) {
//                        handleSuccessfulResponse(response)
//                    } else {
//                        handleNotSuccessfulResponse(response)
//                    }
//                }
//            })
//    }


    private fun handleSuccessfulResponse(response: Response<List<ContactList>>) {
        allUsers = response.body()!!
        Log.d(TAG, "Received ${allUsers.size} contacts")
        showUsers(allUsers)
    }

    private fun handleNotSuccessfulResponse(response: Response<List<ContactList>>) {
        val msg = "${response.code()} ${response.errorBody()!!.string()}"
        Log.d(TAG, "onResponse: $msg")
        Toast.makeText(applicationContext, "Error: $msg", Toast.LENGTH_LONG).show()
    }

    private fun showUsers(users: List<ContactList>) {
        rvUsers.layoutManager = LinearLayoutManager(applicationContext)
        rvUsers.adapter = ContactsAdapter(users)
    }

}