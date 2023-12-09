package com.example.billbuddy.vinayactivity

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.billbuddy.R
import com.example.billbuddy.databinding.ActivitySettleUpMainBinding
import com.example.billbuddy.menubartrail.MenuMainActivity
import com.example.billbuddy.vinay.database.sharedpreferences.PreferenceHelper

class SettleUpActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySettleUpMainBinding
    private lateinit var preferenceHelper: PreferenceHelper
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
}
