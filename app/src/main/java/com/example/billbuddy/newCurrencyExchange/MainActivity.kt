package com.example.billbuddy.newCurrencyExchange

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.example.billbuddy.R
import com.example.billbuddy.databinding.ActivityMainTestBinding
import com.example.billbuddy.newCurrencyExchange.presentation.converter.ConverterFragment
import com.example.billbuddy.newCurrencyExchange.presentation.main.MainFragment

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainTestBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainTestBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.bottomNavigationView.setOnItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.navigation_home -> {
                    replaceFragment(MainFragment())
                    true
                }
                R.id.navigation_dashboard -> {
                    replaceFragment(ConverterFragment())
                    true
                }
                else -> false
            }
        }
    }

    private fun replaceFragment(fragment: Fragment) {
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.fragmentContainerView, fragment)
        fragmentTransaction.commit()
    }
}