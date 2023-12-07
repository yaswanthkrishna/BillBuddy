package com.example.billbuddy.yaswanth

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.billbuddy.R
import com.example.billbuddy.databinding.ActivityTransactionsBinding
import com.example.billbuddy.menubartrail.ui.home.FriendsFragment
import com.example.billbuddy.jing.FragmentA

class TransactionsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityTransactionsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTransactionsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        // Set up the spinner (dropdown menu)
        val options = arrayOf("Friends", "Groups")
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, options)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerOptions.adapter = adapter

        // Handle item selection in the spinner
        binding.spinnerOptions.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parentView: AdapterView<*>?, selectedItemView: View?, position: Int, id: Long) {
                // Load corresponding fragment based on the selected option
                when (position) {
                    0 -> loadFragment(FriendsFragment())
                    1 -> loadFragment(FragmentA())
                }
            }

            override fun onNothingSelected(parentView: AdapterView<*>?) {
                // Do nothing here
            }
        }

        // Set up the back button click listener
        binding.btnBack.setOnClickListener {
            onBackPressed()
        }
    }

    private fun loadFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer, fragment)
            .commit()
    }
    private fun setToolbarTitle(title: String) {
        supportActionBar?.title = title
    }
}
