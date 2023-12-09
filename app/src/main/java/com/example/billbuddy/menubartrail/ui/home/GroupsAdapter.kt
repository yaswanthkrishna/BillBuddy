package com.example.billbuddy.menubartrail.ui.home

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.billbuddy.Group
import com.example.billbuddy.GroupDetail
import com.example.billbuddy.R

class GroupsAdapter(private var groups: List<GroupDetail>) : RecyclerView.Adapter<GroupsAdapter.GroupViewHolder>() {
    companion object {
        private fun Double.format(digits: Int): String {
            return String.format("%.${digits}f", this)
        }
    }
    class GroupViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val nameTextView: TextView = view.findViewById(R.id.groupName)
        val amountTextView: TextView = view.findViewById(R.id.tvAmount_group)
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GroupViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_group, parent, false)
        return GroupViewHolder(view)
    }
    override fun onBindViewHolder(holder: GroupViewHolder, position: Int) {
        val group = groups[position]
        holder.nameTextView.text = group.name
        val balance = group.totalOwes - group.totalOwed

        holder.amountTextView.text = if (balance >= 0) {
            holder.amountTextView.setTextColor(Color.GREEN)
            "$${balance.format(2)}"
        } else {
            holder.amountTextView.setTextColor(Color.RED)
            "-$${(-balance).format(2)}"
        }
    }

    override fun getItemCount(): Int = groups.size
    fun updateList(newGroups: List<GroupDetail>) {
        groups = newGroups
        notifyDataSetChanged()
    }
}
