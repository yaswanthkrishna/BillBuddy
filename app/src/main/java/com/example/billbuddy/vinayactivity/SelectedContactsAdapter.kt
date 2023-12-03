// SelectedContactsAdapter.kt
package com.example.billbuddy.vinayactivity

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.billbuddy.R

class SelectedContactsAdapter(
    private val selectedContacts: List<Contact>,
    private val onRemoveClickListener: (Contact) -> Unit
) : RecyclerView.Adapter<SelectedContactsAdapter.ViewHolder>() {

    // ViewHolder class
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val cardView: CardView = itemView.findViewById(R.id.imgCv)
        val tvContactName: TextView = itemView.findViewById(R.id.tvContactNameTemp)
        val imgRemove: ImageView = itemView.findViewById(R.id.imgRemove)
    }

    // Create ViewHolder instances
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.temp_contact_item_layout, parent, false)
        return ViewHolder(view)
    }

    // Bind data to ViewHolder
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val contact = selectedContacts[position]

        // Bind data to views
        holder.tvContactName.text = contact.name

        // Set click listener for removing the contact
        holder.imgRemove.setOnClickListener {
            onRemoveClickListener.invoke(contact)
        }
    }

    // Return the size of your data set
    override fun getItemCount(): Int {
        return selectedContacts.size
    }
}
