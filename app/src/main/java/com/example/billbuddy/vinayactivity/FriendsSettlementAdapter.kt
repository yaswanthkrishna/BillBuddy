package com.example.billbuddy.vinayactivity

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.billbuddy.R

class FriendsSettlementAdapter(
    private val friends: List<String>,
    private val amountsOwed: MutableList<String>,
    private val friendUserid: MutableList<Long>,
    private val onFriendItemClickListener: OnFriendItemClickListener
) : RecyclerView.Adapter<FriendsSettlementAdapter.FriendViewHolder>() {

    interface OnFriendItemClickListener {
        fun onFriendItemClick(position: Int)
    }

    class FriendViewHolder(itemView: View, private val listener: OnFriendItemClickListener) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
        val textFriendName: TextView = itemView.findViewById(R.id.textFriendName)
        val textAmountOwed: TextView = itemView.findViewById(R.id.textAmountOwed)

        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            listener.onFriendItemClick(adapterPosition)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FriendViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_friend_settleup, parent, false)

        return FriendViewHolder(itemView, onFriendItemClickListener)
    }

    override fun onBindViewHolder(holder: FriendViewHolder, position: Int) {
        val friend = friends[position]
        val amountOwed = amountsOwed[position]

        holder.textFriendName.text = friend
        holder.textAmountOwed.text = "Amount Owed: $amountOwed"
    }

    override fun getItemCount(): Int {
        return friends.size
    }

    fun getFriendName(position: Int): String {
        return friends[position]
    }

    fun getAmountOwed(position: Int): String {
        return amountsOwed[position]
    }

    fun getFriendUserId(position: Int): Long {
        return friendUserid[position]
    }
}
