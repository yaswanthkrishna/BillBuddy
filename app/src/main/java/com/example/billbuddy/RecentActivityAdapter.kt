package com.example.billbuddy_login

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.billbuddy_login.databinding.ActivityItemBinding
import com.example.billbuddy_login.vinay.viewmodels.FriendTransactionViewModel
import com.example.billbuddy_login.vinay.viewmodels.GroupTransactionViewModel
import com.example.billbuddy_login.vinay.viewmodels.UserViewModel
import java.sql.Timestamp

class RecentActivityAdapter(val activities : List<Int>,val context: Context) :
    androidx.recyclerview.widget.RecyclerView.Adapter<RecentActivityAdapter.ViewHolder>(){
    // delete if database involved
    var data = listOf(mapOf("description" to "jing record payment from nowhere", "expected action" to "You paid $20", "timeStamp" to Timestamp(System.currentTimeMillis())))
    lateinit var friendTransactionViewmodel : FriendTransactionViewModel
    lateinit var groupTransactionViewModel: GroupTransactionViewModel
    lateinit var userViewModel: UserViewModel

    override fun onCreateViewHolder(parent: ViewGroup, position: Int): ViewHolder {
        // inflate item view
        val binding = ActivityItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    // MUST
    override fun getItemCount(): Int {
        // number of items in the list
        return data.size //change if database acquired
    }

    // MUST
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        // bind each item in the list to a view
        with(holder){
            with(data[position]) {
                binding.activityDescription.text = data[position]["description"].toString()
                binding.activityExpectedAction.text = data[position]["expected action"].toString()
                binding.acticityTimeStamp.text = data[position]["timeStamp"].toString()
            }
        }

    }

    // static inner ViewHolder class
    inner class ViewHolder(val binding: ActivityItemBinding): RecyclerView.ViewHolder(binding.root)
}