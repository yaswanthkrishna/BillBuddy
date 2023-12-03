package com.example.billbuddy
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.example.billbuddy.vinay.database.sharedpreferences.PreferenceHelper
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class DashboardActivity : AppCompatActivity() {

    private lateinit var viewPager: ViewPager2
    private lateinit var tabs: TabLayout
    private lateinit var userEmail: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)

        userEmail = getEmailFromPreferences()

        viewPager = findViewById(R.id.dashboardViewPager)
        tabs = findViewById(R.id.tabs)
        viewPager.adapter = DashboardPagerAdapter(this, userEmail)
        // Set up the TabLayout to work with ViewPager2
        TabLayoutMediator(tabs, viewPager) { tab, position ->
            tab.text = if (position == 0) "Friends" else "Groups"
        }.attach()
    }

    private inner class DashboardPagerAdapter(activity: AppCompatActivity, private val userEmail: String) :
        FragmentStateAdapter(activity) {
        override fun getItemCount(): Int = 2 // We have two fragments

        override fun createFragment(position: Int): Fragment {
            return if (position == 0) {
                FriendsFragment().apply {
                    arguments = Bundle().apply {
                        putString("email", userEmail)
                    }
                }
            } else {
                GroupsFragment()
            }
        }
    }

    private fun getEmailFromPreferences(): String {
        val preferenceHelper = PreferenceHelper(this)
        val email = preferenceHelper.readStringFromPreference("USER_EMAIL")
        return email ?: "" // Return an empty string if email is null
    }
}
