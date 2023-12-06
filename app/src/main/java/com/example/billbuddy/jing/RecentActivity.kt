package com.example.billbuddy.jing

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.billbuddy.databinding.RecentActivityBinding
import com.example.billbuddy.R
import com.example.billbuddy.jing.RecentActivityAdapter
import com.example.billbuddy.vinay.database.recent_activity.RecentActivityEntity
import com.example.billbuddy.vinay.database.recent_activity.RecentActivityTransactionEntity
import com.example.billbuddy.vinay.viewmodels.RecentActivityTransactionViewModel
import com.example.billbuddy.vinay.viewmodels.RecentActivityTransactionViewModelFactory
import com.example.billbuddy.vinay.viewmodels.RecentActivityViewModel
import com.example.billbuddy.vinay.viewmodels.RecentActivityViewModelFactory
import com.example.billbuddy.vinay.views.SplitwiseApplication

class RecentActivity : AppCompatActivity(){

    private lateinit var binding: RecentActivityBinding
    private lateinit var recentActivityViewModel: RecentActivityViewModel
    private lateinit var recentActivityTransactionViewModel: RecentActivityTransactionViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.recent_activity)
        createDatabase()
        // establish binding with recent_activity.xml
        binding = RecentActivityBinding.inflate(layoutInflater)
        binding.rvActivityList.layoutManager = androidx.recyclerview.widget.LinearLayoutManager(this)
        //activity list from database
        val activityList : List<Any> = getSortActivity()
        binding.rvActivityList.adapter = RecentActivityAdapter(activityList,this) //pass list to adapter

    }

    override fun onResume() {
        super.onResume()
        binding.rvActivityList.adapter = RecentActivityAdapter(getSortActivity(),this) //pass list to adapter
        getSortActivity()
    }
    // refresh page when data changed
    private fun refresh(): Int {
        return 1
    }
    
    //fetch and sort activities related to the user
    private fun getSortActivity():List<Any>{
        var recentActivityList :List<RecentActivityEntity> = emptyList()
        recentActivityViewModel.getRecentActivityList().observe(this, Observer{
            recentActivityList = it
        })

        var recentActivityTransactionList :List<RecentActivityTransactionEntity> = emptyList()
        recentActivityTransactionViewModel.getRecentActivityTransactionList().observe(this, Observer{
            recentActivityTransactionList = it
        })

        var sortedList :MutableList<Any> = mutableListOf(1)
        sortedList.clear()
        recentActivityList.sortedBy { it.time }
        for (i in recentActivityList){
            sortedList.add(i)
        }
        for (i in recentActivityTransactionList){
            sortedList.add(i)
        }
        sortedList.sortedBy {
            if (it is RecentActivityEntity){
                it.time
            }else if (it is RecentActivityTransactionEntity){
                it.time
            }else{
                -1
            }
        }

        return sortedList
    }

    private fun createDatabase() {
        val appClass = application as SplitwiseApplication
        val recentActivityRepository = appClass.recentActivityRepository
        val recentActivityTransactionRepository = appClass.recentActivityTransactionRepository
        val recentActivityViewModelFactory = RecentActivityViewModelFactory(recentActivityRepository)
        val recentActivityTransactionViewModelFactory =
            RecentActivityTransactionViewModelFactory(recentActivityTransactionRepository)

        recentActivityViewModel = ViewModelProvider(this, recentActivityViewModelFactory)
            .get(RecentActivityViewModel::class.java)

        recentActivityTransactionViewModel = ViewModelProvider(this, recentActivityTransactionViewModelFactory)
            .get(RecentActivityTransactionViewModel::class.java)

    }
}