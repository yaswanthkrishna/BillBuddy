package com.example.billbuddy.vinayactivity

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.billbuddy.R

class ContactsAdapter(
    private var contacts: List<Contact>,
    private val onContactSelected: (Contact) -> Unit
) : RecyclerView.Adapter<ContactsAdapter.ViewHolder>() {

    // ViewHolder class
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvContactName: TextView = itemView.findViewById(R.id.tvContactName)
        val tvContactNumber: TextView = itemView.findViewById(R.id.tvContactNumber)

        fun bind(contact: Contact, onContactSelected: (Contact) -> Unit) {
            tvContactName.text = contact.name
            tvContactNumber.text = contact.phoneNumber

            // Set a click listener to handle contact selection
            itemView.setOnClickListener {
                onContactSelected.invoke(contact)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_contact, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val contact = contacts[position]
        holder.bind(contact, onContactSelected)
    }

    override fun getItemCount(): Int {
        return contacts.size
    }

    fun updateData(newContacts: List<Contact>) {
        contacts = newContacts
        notifyDataSetChanged()
    }
}
