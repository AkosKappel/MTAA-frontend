package com.example.mtaa

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mtaa.adapters.ContactsAdapter
import com.example.mtaa.api.ApiClient
import com.example.mtaa.models.Contact
import com.example.mtaa.utilities.Utils
import com.example.mtaa.utilities.Validator
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ManageContactsActivity : AppCompatActivity() {

    // toolbar elements
    private lateinit var btnProfile: ImageView
    private lateinit var btnHome: TextView
    private lateinit var btnBack: ImageView

    // window elements
    private lateinit var etContactId: EditText
    private lateinit var btnRemoveContact: Button
    private lateinit var btnAddContact: Button
    private lateinit var rvContacts: RecyclerView
    private lateinit var tvEmpty: TextView

    private lateinit var allContacts: List<Contact>

    companion object {
        private const val TAG: String = "ManageContactsActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_manage_contacts)

        btnProfile = findViewById(R.id.btnProfile)
        btnHome = findViewById(R.id.btnHome)
        btnBack = findViewById(R.id.btnBack)
        etContactId = findViewById(R.id.etContactId)
        btnRemoveContact = findViewById(R.id.btnRemoveContact)
        btnAddContact = findViewById(R.id.btnAddContact)
        rvContacts = findViewById(R.id.rvContacts)
        tvEmpty = findViewById(R.id.tvEmpty)

        fetchContacts()

        btnHome.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        btnProfile.setOnClickListener {
            val intent = Intent(applicationContext, ProfileActivity::class.java)
            startActivity(intent)
        }

        btnAddContact.setOnClickListener {
            if (!Validator.validateID(etContactId)) {
                return@setOnClickListener
            }
            addContact()
        }

        btnRemoveContact.setOnClickListener {
            if (!Validator.validateID(etContactId)) {
                return@setOnClickListener
            }
            removeContact()
        }

        btnBack.setOnClickListener { finish() }
    }

    private fun addContact() {
        ApiClient.getApiService(applicationContext)
            .addContact(etContactId.text.toString().trim().toInt())
            .enqueue(object : Callback<List<Contact>> {
                override fun onFailure(call: Call<List<Contact>>, t: Throwable) {
                    handleFailure(t)
                }

                override fun onResponse(
                    call: Call<List<Contact>>, response: Response<List<Contact>>
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
            .enqueue(object : Callback<List<Contact>> {
                override fun onFailure(call: Call<List<Contact>>, t: Throwable) {
                    handleFailure(t)
                }

                override fun onResponse(
                    call: Call<List<Contact>>, response: Response<List<Contact>>
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
            .enqueue(object : Callback<List<Contact>> {
                override fun onFailure(call: Call<List<Contact>>, t: Throwable) {
                    handleFailure(t)
                }

                override fun onResponse(
                    call: Call<List<Contact>>, response: Response<List<Contact>>
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

    private fun handleSuccessfulResponse(response: Response<List<Contact>>) {
        allContacts = response.body()?.sortedBy { it.id }!!
        Log.d(TAG, "Received ${allContacts.size} contacts")
        etContactId.setText("")

//        if (allContacts.isEmpty()) {
//            rvContacts.visibility = View.GONE
//            tvEmpty.visibility = View.VISIBLE
//        } else {
//            rvContacts.visibility = View.VISIBLE
//            tvEmpty.visibility = View.GONE
//        }

        showContacts(allContacts)
    }

    private fun handleNotSuccessfulResponse(response: Response<List<Contact>>) {
        val errorBody = response.errorBody()?.string()
        val jsonObject = errorBody?.let { JSONObject(it) }
        val detail = Utils.getErrorBodyDetail(jsonObject)
        Log.d(TAG, "onResponse: ${response.code()} $detail")
        Toast.makeText(applicationContext, "Error: $detail", Toast.LENGTH_LONG).show()
    }

    private fun showContacts(contacts: List<Contact>) {
        rvContacts.layoutManager = LinearLayoutManager(applicationContext)
        val adapter = ContactsAdapter(contacts)
        rvContacts.adapter = adapter
        adapter.setOnItemClickListener(object : ContactsAdapter.OnItemClickListener {
            override fun onItemClick(position: Int) {
                etContactId.setText(contacts[position].id.toString())
            }
        })
    }
}