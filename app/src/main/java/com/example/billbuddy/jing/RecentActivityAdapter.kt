package com.example.billbuddy.jing

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.billbuddy.databinding.ActivityItemBinding
import java.sql.Timestamp
import java.time.Instant

class RecentActivityAdapter(val activities : List<Any>,val context: Context) :
    RecyclerView.Adapter<RecentActivityAdapter.ViewHolder>(){
    // temporary list mimicking database list
    var data = listOf(mapOf("description" to "jing record payment from nowhere", "expected action" to "You paid $20", "timeStamp" to Timestamp(System.currentTimeMillis())))

    override fun onCreateViewHolder(parent: ViewGroup, position: Int): ViewHolder {
        // inflate binding
        val binding = ActivityItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    // function for recyclerview to know how many items needs to display
    override fun getItemCount() = data.size

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

    private fun timeDisplay(timestamp: Timestamp){
        val currentTime = System.currentTimeMillis()
        val difference = currentTime - timestamp.time
    }
    // static inner ViewHolder class
    inner class ViewHolder(val binding: ActivityItemBinding): RecyclerView.ViewHolder(binding.root)
}
