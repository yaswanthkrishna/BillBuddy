package com.example.billbuddy

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class FriendsAdapter(private var friends: List<Friend>) : RecyclerView.Adapter<FriendsAdapter.FriendViewHolder>() {

    class FriendViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        // Initialize your views here, for example:
        // val nameTextView: TextView = view.findViewById(R.id.nameTextView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FriendViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_friend, parent, false)
        return FriendViewHolder(view)
    }

    override fun onBindViewHolder(holder: FriendViewHolder, position: Int) {
        val friend = friends[position]
        // Bind data to the views here, for example:
        // holder.nameTextView.text = friend.name
    }

    override fun getItemCount(): Int = friends.size

    fun updateList(newFriends: List<Friend>) {
        friends = newFriends
        notifyDataSetChanged()
    }
}
