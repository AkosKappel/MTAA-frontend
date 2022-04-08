package com.example.mtaa.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.mtaa.R
import com.example.mtaa.models.ContactList

class ContactsAdapter(val contacts: List<ContactList>) :
    RecyclerView.Adapter<ContactsAdapter.ContactViewHolder>() {

    class ContactViewHolder(val view: View) : RecyclerView.ViewHolder(view)

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactViewHolder {
            return ContactViewHolder(
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.contact_list_layout, parent, false)
            )
        }

        override fun getItemCount() = contacts.size

        override fun onBindViewHolder(holder: ContactViewHolder, position: Int) {
            val tvUserID = holder.view.findViewById<TextView>(R.id.tvUserID)
            val tvUserEmail = holder.view.findViewById<TextView>(R.id.tvUserEmail)

            val contact = contacts[position]
            val id = "ID: " + contact.id.toString()
            val email = "Email: " + contact.email

            tvUserID.text = id
            tvUserEmail.text = email
        }
}