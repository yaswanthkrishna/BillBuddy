package com.example.billbuddy_login

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class GroupsAdapter(private var groups: List<Group>) : RecyclerView.Adapter<GroupsAdapter.GroupViewHolder>() {

    class GroupViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        // Initialize your views here
        // val textViewName: TextView = view.findViewById(R.id.textViewName)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GroupViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_group, parent, false)
        return GroupViewHolder(view)
    }

    override fun onBindViewHolder(holder: GroupViewHolder, position: Int) {
        val group = groups[position]
        // Bind data to the views
        // holder.textViewName.text = group.name
    }

    override fun getItemCount() = groups.size

    fun updateList(newGroups: List<Group>) {
        groups = newGroups
        notifyDataSetChanged()
    }

    // You might need a method to get the data at a certain position for click events
    fun getGroupAtPosition(position: Int): Group = groups[position]
}
