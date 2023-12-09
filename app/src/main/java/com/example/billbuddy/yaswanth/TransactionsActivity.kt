package com.example.billbuddy.yaswanth

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.billbuddy.R
import com.example.billbuddy.databinding.ActivityTransactionsBinding
import com.example.billbuddy.menubartrail.MenuMainActivity
import com.example.billbuddy.vinay.database.sharedpreferences.PreferenceHelper

class TransactionsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityTransactionsBinding
    private lateinit var preferenceHelper: PreferenceHelper
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTransactionsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialize preferenceHelper
        preferenceHelper = PreferenceHelper(this)

        val options = arrayOf("Friends", "Groups")
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, options)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerOptions.adapter = adapter
        binding.spinnerOptions.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parentView: AdapterView<*>?, selectedItemView: View?, position: Int, id: Long) {
                when (position) {
                    0 -> loadFragment(FriendTransactionFragment.newInstance())
                    1 -> loadFragment(GroupTransactionFragment.newInstance())
                }
            }
            override fun onNothingSelected(parentView: AdapterView<*>?) {
            }
        }
        binding.btnBack.setOnClickListener {
            onBackPressed()
        }
    }

    private fun loadFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainerTransaction, fragment) // Correct ID
            .commit()
    }
    private fun setToolbarTitle(title: String) {
        supportActionBar?.title = title
    }
    override fun onPause() {
        super.onPause()
        preferenceHelper.writeStringToPreference("LAST_ACTIVITY", this::class.java.name)
    }
}
