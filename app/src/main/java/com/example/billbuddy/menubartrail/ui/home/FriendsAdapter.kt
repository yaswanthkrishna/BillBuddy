package com.example.billbuddy.menubartrail.ui.home

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.billbuddy.R

class FriendsAdapter(private var friends: List<Friend>) : RecyclerView.Adapter<FriendsAdapter.FriendViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FriendViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_friend, parent, false)
        return FriendViewHolder(view)
    }
    override fun onBindViewHolder(holder: FriendViewHolder, position: Int) {
        val friend = friends[position]
        holder.bind(friend)
    }
    inner class FriendViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val nameTextView: TextView = view.findViewById(R.id.tvFriendName)
        private val nameAmountView: TextView = view.findViewById(R.id.tvAmount)
        fun bind(friend: Friend) {
            nameTextView.text = friend.name
            nameAmountView.text = friend.totalDue.toString()
        }
    }

    override fun getItemCount(): Int = friends.size

    fun updateList(newFriends: List<Friend>) {
        friends = newFriends
        notifyDataSetChanged()
    }
}