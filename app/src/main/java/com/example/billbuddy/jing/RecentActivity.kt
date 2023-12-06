package com.example.billbuddy.jing

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.billbuddy.databinding.RecentActivityBinding
import com.example.billbuddy.R
import com.example.billbuddy.jing.RecentActivityAdapter

class RecentActivity : AppCompatActivity(){

    private lateinit var binding: RecentActivityBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.recent_activity)
        // establish binding with recent_activity.xml
        binding = RecentActivityBinding.inflate(layoutInflater)
        binding.rvActivityList.layoutManager = androidx.recyclerview.widget.LinearLayoutManager(this)
        //activity list from database
        val activityList : List<Any> = getSortActivity()
        binding.rvActivityList.adapter = RecentActivityAdapter(activityList,this) //pass list to adapter

    }

    // refresh page when data changed
    private fun refresh(): Int {
        return 1
    }
    
    //fetch and sort activities related to the user
    private fun getSortActivity():List<Any>{
        return emptyList()
    }
}