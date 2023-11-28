package com.example.billbuddy

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class DashboardActivity : AppCompatActivity() {

    private lateinit var viewPager: ViewPager2
    private lateinit var tabs: TabLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)

        viewPager = findViewById(R.id.dashboardViewPager)
        tabs = findViewById(R.id.tabs)
        viewPager.adapter = DashboardPagerAdapter(this)

        // Set up the TabLayout to work with ViewPager2
        TabLayoutMediator(tabs, viewPager) { tab, position ->
            tab.text = if (position == 0) "Friends" else "Groups"
        }.attach()
    }

    private inner class DashboardPagerAdapter(activity: AppCompatActivity) : FragmentStateAdapter(activity) {
        override fun getItemCount(): Int = 2 // We have two fragments

        override fun createFragment(position: Int) = if (position == 0) {
            FriendsFragment() // The fragment that shows friends
        } else {
            GroupsFragment()  // The fragment that shows groups
        }
    }
}
