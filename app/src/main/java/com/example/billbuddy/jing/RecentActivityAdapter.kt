package com.example.billbuddy.jing

import android.content.Context
import android.os.Build
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.example.billbuddy.databinding.ActivityItemBinding
import com.example.billbuddy.vinay.database.recent_activity.RecentActivityEntity
import com.example.billbuddy.vinay.database.recent_activity.RecentActivityTransactionEntity
import com.example.billbuddy.vinay.viewmodels.UserViewModel
import com.example.billbuddy.vinay.viewmodels.UserViewModelFactory
import com.example.billbuddy.vinay.views.SplitwiseApplication
import java.sql.Timestamp
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import com.example.billbuddy.jing.RecentActivity
import com.example.billbuddy.vinay.database.users.UserEntity

class RecentActivityAdapter(val activities : List<Any>,val currentUser : UserEntity, val userList: List<UserEntity>,val context: Context) :
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

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        // bind each item in the list to a view
        with(holder){
            val currentActivity: Any = activities[position]
            // solve transaction activities display
            if (currentActivity is RecentActivityTransactionEntity) {
                val textList = getTextTransactionActivity(currentActivity)
                binding.activityDescription.text = textList[0]
                binding.activityExpectedAction.text = textList[1]
                binding.acticityTimeStamp.text = timeDisplay(currentActivity.time)
            }else if(currentActivity is RecentActivityEntity){
                val description = ""
                binding.activityDescription.text = description
                binding.activityExpectedAction.text = ""
                binding.acticityTimeStamp.text = timeDisplay(currentActivity.time)
            }
        }

    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun timeDisplay(timestamp: Long) :String{
        val currentTime = System.currentTimeMillis()
        val difference = currentTime - timestamp
        val current = LocalDateTime.ofInstant(Instant.ofEpochMilli(timestamp), ZoneId.systemDefault())
        return if (difference / 1000 < 60){
            (difference / 1000).toString() + "seconds ago"
        }else if(difference / 60000 < 60){
            (difference /60000).toInt().toString() + "minutes ago"
        }else if(difference / 3600000 < 4){
            (difference / 3600000).toInt().toString() + "hours ago"
        }else{
            DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss").format(current)
        }


    }
    // static inner ViewHolder class

    private fun getDescriptionActivity(activity : RecentActivityEntity) : String{
        return activity.comment!!
    }

    private fun getTextTransactionActivity(activity : RecentActivityTransactionEntity) : List<String>{
        var description: String = ""
        var action: String = ""
        val creator = userList.firstOrNull { it.user_id == activity.creator.toLong() }
        if (activity.groupName == ""){
            //set description
            description = if (currentUser.name == creator!!.name) {
                "You added ${activity.description}."
            }else{ "${creator.name} added ${activity.description}." }
            //set Action
            if (activity.userPaid == currentUser.name){
                var totalAmount :Double = 0.0
                for (item in activity.memberAmount){
                    if (item.key != activity.userPaid){ totalAmount += item.value }
                }
                action = "You get back \$$totalAmount "
            }else{
                action = if (activity.memberAmount[currentUser.name] == 0.0){
                    "You do not owe anything"
                }else {
                    "You owe \$${activity.memberAmount[currentUser.name]}"
                }
            }
        }else{
            description = if (currentUser.name == creator!!.name) {
                "You added ${activity.description} to ${activity.groupName}."
            }else{ "${creator.name} added ${activity.description} to ${activity.groupName}." }
        }
        return listOf(description,action)
    }

    private fun getExpectedActionTransactionActivity(activity: RecentActivityTransactionEntity){}
        inner class ViewHolder(val binding: ActivityItemBinding): RecyclerView.ViewHolder(binding.root)


}
