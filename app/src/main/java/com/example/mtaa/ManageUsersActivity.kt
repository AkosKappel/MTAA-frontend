package com.example.mtaa

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mtaa.adapters.ContactsAdapter
import com.example.mtaa.api.ApiClient
import com.example.mtaa.models.Contact
import com.example.mtaa.models.MeetingResponse
import com.example.mtaa.utilities.Utils
import com.example.mtaa.utilities.Validator
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ManageUsersActivity : AppCompatActivity() {

    // toolbar elements
    private lateinit var btnProfile: ImageView
    private lateinit var btnHome: TextView
    private lateinit var btnBack: ImageView

    // window elements
    private lateinit var btnRemoveUser: Button
    private lateinit var btnAddUser: Button
    private lateinit var rvContacts: RecyclerView
    private lateinit var rvUsers: RecyclerView
    private lateinit var etUserId: EditText
    private lateinit var selectedMeeting: MeetingResponse

    private lateinit var allContacts: List<Contact>
    private lateinit var allUsers: List<Contact>

    companion object {
        private const val TAG: String = "ManageUsersActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_manage_users)

        btnProfile = findViewById(R.id.btnProfile)
        btnHome = findViewById(R.id.btnHome)
        btnBack = findViewById(R.id.btnBack)
        rvContacts = findViewById(R.id.rvContacts)
        rvUsers = findViewById(R.id.rvUsers)
        etUserId = findViewById(R.id.etUserId)
        btnRemoveUser = findViewById(R.id.btnRemoveUser)
        btnAddUser = findViewById(R.id.btnAddUser)

        allUsers = ArrayList()
        allContacts = ArrayList()
        selectedMeeting = intent.getSerializableExtra("meeting") as MeetingResponse

//        allUsers = selectedMeeting.users
//        showUsers()
        fetchUsers()
        fetchContacts()

        btnHome.setOnClickListener {
            val intent = Intent(applicationContext, MainActivity::class.java)
            startActivity(intent)
        }

        btnProfile.setOnClickListener {
            val intent = Intent(applicationContext, ProfileActivity::class.java)
            startActivity(intent)
        }

        btnAddUser.setOnClickListener {
            if (!Validator.validateID(etUserId) ||
                !Validator.validateNotOwner(etUserId, selectedMeeting.ownerId)
            ) {
                return@setOnClickListener
            }
            addUser()
        }

        btnRemoveUser.setOnClickListener {
            if (!Validator.validateID(etUserId) ||
                !Validator.validateNotOwner(etUserId, selectedMeeting.ownerId)
            ) {
                return@setOnClickListener
            }
            val removeId = etUserId.text.toString().trim().toInt()
            removeUser(removeId)
        }

        btnBack.setOnClickListener { finish() }
    }

    private fun addUser() {
        ApiClient.getApiService(applicationContext)
            .addUserToMeeting(selectedMeeting.id, etUserId.text.toString().trim().toInt())
            .enqueue(object : Callback<List<Contact>> {
                override fun onFailure(call: Call<List<Contact>>, t: Throwable) {
                    handleFailure(t)
                }

                override fun onResponse(
                    call: Call<List<Contact>>, response: Response<List<Contact>>
                ) {
                    if (response.isSuccessful) {
                        handleSuccessfulResponseUsers(response)
                    } else {
                        handleNotSuccessfulResponse(response)
                    }
                }
            })
    }

    private fun removeUser(removeId: Int) {
        ApiClient.getApiService(applicationContext)
            .removeUserFromMeeting(selectedMeeting.id, removeId)
            .enqueue(object : Callback<List<Contact>> {
                override fun onFailure(call: Call<List<Contact>>, t: Throwable) {
                    handleFailure(t)
                }

                override fun onResponse(
                    call: Call<List<Contact>>, response: Response<List<Contact>>
                ) {
                    if (response.isSuccessful) {
                        handleSuccessfulResponseUsers(response)
                    } else {
                        handleNotSuccessfulResponse(response)
                    }
                }
            })
    }

    private fun fetchContacts() {
        ApiClient.getApiService(applicationContext)
            .getContacts()
            .enqueue(object : Callback<List<Contact>> {
                override fun onFailure(call: Call<List<Contact>>, t: Throwable) {
                    handleFailure(t)
                }

                override fun onResponse(
                    call: Call<List<Contact>>, response: Response<List<Contact>>
                ) {
                    if (response.isSuccessful) {
                        handleSuccessfulResponseContacts(response)
                    } else {
                        handleNotSuccessfulResponse(response)
                    }
                }
            })
    }

    private fun fetchUsers() {
        ApiClient.getApiService(applicationContext)
            .getUsersInMeeting(selectedMeeting.id)
            .enqueue(object : Callback<List<Contact>> {
                override fun onFailure(call: Call<List<Contact>>, t: Throwable) {
                    handleFailure(t)
                }

                override fun onResponse(
                    call: Call<List<Contact>>, response: Response<List<Contact>>
                ) {
                    if (response.isSuccessful) {
                        handleSuccessfulResponseUsers(response)
                    } else {
                        handleNotSuccessfulResponse(response)
                    }
                }
            })
    }

    private fun handleSuccessfulResponseContacts(response: Response<List<Contact>>) {
        allContacts = response.body()!!
        Log.d(TAG, "Received ${allContacts.size} contacts")
        showContacts()
    }

    private fun handleSuccessfulResponseUsers(response: Response<List<Contact>>) {
        allUsers = response.body()!!
        Log.d(TAG, "Received ${allUsers.size} users")
        etUserId.setText("")
        showUsers()
    }

    private fun handleNotSuccessfulResponse(response: Response<List<Contact>>) {
        val errorBody = response.errorBody()?.string()
        val jsonObject = errorBody?.let { JSONObject(it) }
        val detail = Utils.getErrorBodyDetail(jsonObject)
        Log.d(TAG, "onResponse: ${response.code()} $detail")
        Toast.makeText(applicationContext, "Error: $detail", Toast.LENGTH_LONG).show()
    }

    private fun handleFailure(t: Throwable) {
        Log.d(TAG, "onFailure: ${t.message.toString()}")
        Toast.makeText(applicationContext, t.message, Toast.LENGTH_LONG).show()
    }

    private fun showContacts() {
        rvContacts.layoutManager = LinearLayoutManager(applicationContext)
        val adapter = ContactsAdapter(allContacts)
        rvContacts.adapter = adapter
        adapter.setOnItemClickListener(object : ContactsAdapter.OnItemClickListener {
            override fun onItemClick(position: Int) {
                etUserId.setText(allContacts[position].id.toString())
            }
        })
    }

    private fun showUsers() {
        rvUsers.layoutManager = LinearLayoutManager(applicationContext)
        val adapter = ContactsAdapter(allUsers)
        rvUsers.adapter = adapter
        adapter.setOnItemClickListener(object : ContactsAdapter.OnItemClickListener {
            override fun onItemClick(position: Int) {
                etUserId.setText(allUsers[position].id.toString())
            }
        })
    }
}