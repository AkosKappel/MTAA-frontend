package com.example.mtaa.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.mtaa.R
import com.example.mtaa.models.Contact

class ContactsAdapter(val contacts: List<Contact>) :
    RecyclerView.Adapter<ContactsAdapter.ContactViewHolder>() {

    private lateinit var listener: OnItemClickListener

    interface OnItemClickListener {
        fun onItemClick(position: Int)
    }

    fun setOnItemClickListener(listener: OnItemClickListener) {
        this.listener = listener
    }

    class ContactViewHolder(val view: View, listener: OnItemClickListener) :
        RecyclerView.ViewHolder(view) {
        init {
            view.setOnClickListener {
                listener.onItemClick(adapterPosition)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactViewHolder {
        return ContactViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.contact_list_layout, parent, false), listener
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