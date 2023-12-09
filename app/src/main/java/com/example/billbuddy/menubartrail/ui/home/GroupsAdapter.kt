package com.example.billbuddy.menubartrail.ui.home

import android.content.Context
import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.billbuddy.Group
import com.example.billbuddy.GroupDetail
import com.example.billbuddy.R

class GroupsAdapter(private val context: Context, private var groups: List<GroupDetail>) : RecyclerView.Adapter<GroupsAdapter.GroupViewHolder>() {
    companion object {
        private fun Double.format(digits: Int): String {
            return String.format("%.${digits}f", this)
        }
    }
    class GroupViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val nameTextView: TextView = view.findViewById(R.id.groupName)
        val amountTextView: TextView = view.findViewById(R.id.tvAmount_group)
        val groupImageView: ImageView = view.findViewById(R.id.rvGroupImage)
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GroupViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_group, parent, false)
        return GroupViewHolder(view)
    }
    override fun onBindViewHolder(holder: GroupViewHolder, position: Int) {
        val group = groups[position]
        holder.nameTextView.text = group.name
        val balance = group.totalOwes - group.totalOwed
        val type = group.type

        holder.amountTextView.text = if (balance >= 0) {
            holder.amountTextView.setTextColor(ContextCompat.getColor(context,R.color.green_2))
            "$${balance.format(2)}"
        } else {
            holder.amountTextView.setTextColor(Color.RED)
            "-$${(-balance).format(2)}"
        }
        when(type){
            "Home" -> {
                holder.groupImageView.setImageResource(R.drawable.ic_home)
                holder.groupImageView.setBackgroundResource(R.drawable.background1)
                holder.groupImageView.background.alpha = 150
            }
            "Trip" -> {
                holder.groupImageView.setImageResource(R.drawable.ic_trip)
                holder.groupImageView.setBackgroundResource(R.drawable.background5)
                holder.groupImageView.background.alpha = 150

            }
            "Couple" -> {
                holder.groupImageView.setImageResource(R.drawable.ic_couple)
                holder.groupImageView.setBackgroundResource(R.drawable.background2)
                holder.groupImageView.background.alpha = 150

            }
            "Friends" -> {
                holder.groupImageView.setImageResource(R.drawable.ic_friends)
                holder.groupImageView.setBackgroundResource(R.drawable.background4)
                holder.groupImageView.background.alpha = 150

            }
            "Other" -> {
                holder.groupImageView.setImageResource(R.drawable.ic_grid)
                holder.groupImageView.setBackgroundResource(R.drawable.background6)
                holder.groupImageView.background.alpha = 150

            }
            else -> {
                Log.e("Type Error","Type not found in list")
                holder.groupImageView.setImageResource(R.drawable.ic_list)
                holder.groupImageView.setBackgroundResource(R.drawable.background3)
                holder.groupImageView.background.alpha = 150

            }
        }
    }

    override fun getItemCount(): Int = groups.size
    fun updateList(newGroups: List<GroupDetail>) {
        groups = newGroups
        notifyDataSetChanged()
    }
}
