package com.example.billbuddy.menubartrail.ui.home

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.billbuddy.R
import com.example.billbuddy.menubartrail.ui.home.GroupsFragment.Companion.format

class FriendsAdapter(private val context: Context, private var friends: List<Friend>) : RecyclerView.Adapter<FriendsAdapter.FriendViewHolder>() {
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
        val randImageView: ImageView = view.findViewById(R.id.rvFriendImage)
        fun bind(friend: Friend) {
            nameTextView.text = friend.name
            if (friend.totalDue >= 0) {
                nameAmountView.text = "${friend.totalDue.format(2)}"
                nameAmountView.setTextColor(ContextCompat.getColor(context,R.color.green_2))
            } else {
                nameAmountView.text = "-${friend.totalDue.format(2)}"
                nameAmountView.setTextColor(Color.RED)
            }
            // function generates random int and to get random background color.
            // The foreground icon remains the same
            val userId = friend.userId
            val randomInt = ((userId + friend.friendUserId + friend.owes + friend.owe) % 5).toInt()
            val imageList = listOf(
                R.drawable.background1, R.drawable.background2,
                R.drawable.background3, R.drawable.background4,
                R.drawable.background5, R.drawable.background6)
            randImageView.setImageResource(R.drawable.ic_person)
            randImageView.setBackgroundResource(imageList[randomInt])
            randImageView.background.alpha = 255
        }
    }
    override fun getItemCount(): Int = friends.size
    fun updateList(newFriends: List<Friend>) {
        friends = newFriends
        notifyDataSetChanged()
    }
}