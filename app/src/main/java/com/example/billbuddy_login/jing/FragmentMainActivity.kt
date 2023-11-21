package com.example.billbuddy_login.jing

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.billbuddy_login.databinding.ActivityFragmentMainBinding
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class FragmentMainActivity : AppCompatActivity(), TabLayout.OnTabSelectedListener {

    private lateinit var binding: ActivityFragmentMainBinding
    companion object {
        var name = ""
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFragmentMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        name = intent.getStringExtra("name").toString()
        setViewPager(name)
    }

    private fun setViewPager(name: String?) {
        val fragmentAdapter = FragmentAdapter(this, name!!)
        binding.viewPager.adapter = fragmentAdapter

        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            Log.d("pravin", "onConfigureTab called")
        }.attach()
    }

    override fun onTabSelected(tab: TabLayout.Tab?) {
        // Handle tab selection
    }

    override fun onTabUnselected(tab: TabLayout.Tab?) {
        // Handle tab unselection
    }

    override fun onTabReselected(tab: TabLayout.Tab?) {
        // Handle tab reselection
    }
}
