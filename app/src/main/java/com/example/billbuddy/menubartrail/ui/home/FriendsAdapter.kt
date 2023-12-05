package com.example.billbuddy.menubartrail.ui.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.billbuddy.R

class FriendsAdapter(private var friends: List<Friend>) : RecyclerView.Adapter<FriendsAdapter.FriendViewHolder>() {

    class FriendViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val nameTextView: TextView = view.findViewById(R.id.tvFriendName)
        val nameAmountView: TextView = view.findViewById(R.id.tvAmount)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FriendViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_friend, parent, false)
        return FriendViewHolder(view)
    }

    override fun onBindViewHolder(holder: FriendViewHolder, position: Int) {
        if (friends.isEmpty()) {
            holder.nameTextView.text = "No friends available"
            holder.nameAmountView.visibility = View.GONE
            // Other UI adjustments for empty state
        } else {
            val friend = friends[position]
            holder.nameTextView.text = friend.name
            holder.nameAmountView.visibility = View.VISIBLE
            // Bind other data as needed
        }
    }

    override fun getItemCount(): Int {
        return if (friends.isEmpty()) 1 else friends.size // Return 1 for empty state
    }
    fun updateList(newFriends: List<Friend>) {
        friends = newFriends
        notifyDataSetChanged()
    }
}
