package com.example.billbuddy.vinayactivity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.billbuddy.R
import com.example.billbuddy.databinding.ActivitySettleUpMainBinding

class SettleUpActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySettleUpMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettleUpMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.tvBackArrow.setOnClickListener {
            onBackPressed()
        }

        // Begin the transaction
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer_settleup, SettleUpListFragment())
            .commit()
    }

    override fun onBackPressed() {
        // Handle back button press here
        // For example, you can check if any fragments are in the back stack
        // and pop the fragment or finish the activity accordingly
        val fragmentManager = supportFragmentManager
        if (fragmentManager.backStackEntryCount > 0) {
            fragmentManager.popBackStack()
        } else {
            super.onBackPressed()
        }
    }
}
