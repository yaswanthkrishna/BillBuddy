package com.example.billbuddy.menubartrail.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.example.billbuddy.R
import com.example.billbuddy.menubartrail.ui.home.FriendsFragment
import com.example.billbuddy.vinay.database.sharedpreferences.PreferenceHelper
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class HomeFragment : Fragment() {

    private lateinit var viewPager: ViewPager2
    private lateinit var tabs: TabLayout
    private lateinit var userEmail: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_home, container, false)
        viewPager = view.findViewById(R.id.dashboardViewPager)
        tabs = view.findViewById(R.id.tabs)
        userEmail = getEmailFromPreferences()
        viewPager.adapter = DashboardPagerAdapter(this, userEmail)
        TabLayoutMediator(tabs, viewPager) { tab, position ->
            tab.text = if (position == 0) "Friends" else "Groups"
        }.attach()
        return view
    }

    private inner class DashboardPagerAdapter(fragment: Fragment, private val userEmail: String) :
        FragmentStateAdapter(fragment) {
        override fun getItemCount(): Int = 2 // We have two fragments
        override fun createFragment(position: Int): Fragment {
            return if (position == 0) {
                FriendsFragment().apply {
                    arguments = Bundle().apply {
                        putString("email", userEmail)
                    }
                }
            } else {
                GroupsFragment().apply {
                    arguments = Bundle().apply {
                        putString("email", userEmail)
                    }
                }
            }
        }
    }

    private fun getEmailFromPreferences(): String {
        val preferenceHelper = PreferenceHelper(requireContext())
        val email = preferenceHelper.readStringFromPreference("USER_EMAIL")
        return email ?: "" // Return an empty string if email is null
    }
}
