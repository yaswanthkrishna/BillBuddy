package com.example.billbuddy_login

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import com.google.android.material.tabs.TabLayoutMediator
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.example.billbuddy_login.R
import com.example.billbuddy_login.databinding.RecentActivityBinding
import com.example.billbuddy_login.jing.FragmentMainActivity
import com.example.billbuddy_login.vinay.viewmodels.UserViewModel
import com.example.billbuddy_login.vinay.views.SplitwiseApplication
import com.google.android.material.tabs.TabLayout
import com.example.billbuddy_login.vinay.database.sharedpreferences.PreferenceHelper
import com.example.billbuddy_login.vinay.repositories.FriendTransactionRepository
import com.example.billbuddy_login.vinay.viewmodels.FriendTransactionViewModel
import com.example.billbuddy_login.vinay.viewmodels.GroupTransactionViewModel


class RecentActivity : AppCompatActivity(){

    private lateinit var binding: RecentActivityBinding
    lateinit var friendTransactionViewmodel : FriendTransactionViewModel
    lateinit var groupTransactionViewModel: GroupTransactionViewModel
    lateinit var userViewModel: UserViewModel
    private val preferenceHelper = PreferenceHelper(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.recent_activity)
        binding = RecentActivityBinding.inflate(layoutInflater)
        binding.activityMainPage.layoutManager = androidx.recyclerview.widget.LinearLayoutManager(this)
        val activityList : List<>
        friendTransactionViewmodel.getFriendTransactionsList().observe(this, Observer {
            for (i in it) {
                if (i.user_id == 11345){

                }
            }
        })
    }
}