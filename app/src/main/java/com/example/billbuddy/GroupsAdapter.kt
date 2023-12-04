package com.example.billbuddy

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class GroupsAdapter(private var groups: List<Group>) : RecyclerView.Adapter<GroupsAdapter.GroupViewHolder>() {
    class GroupViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val nameTextView: TextView = view.findViewById(R.id.tvGroupName)
        val amountTextView: TextView = view.findViewById(R.id.tvAmount)
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GroupViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_group, parent, false)
        return GroupViewHolder(view)
    }
    override fun onBindViewHolder(holder: GroupViewHolder, position: Int) {
        val group = groups[position]
        holder.nameTextView.text = group.name
        holder.amountTextView.text = if (group.amountDue > 0) {
            holder.amountTextView.setTextColor(Color.GREEN)
            "$${group.amountDue}"
        } else {
            holder.amountTextView.setTextColor(Color.RED)
            "-$${group.amountOwed}"
        }
    }
    override fun getItemCount(): Int = groups.size
    fun updateList(newGroups: List<Group>) {
        groups = newGroups
        notifyDataSetChanged()
    }
}