package com.example.billbuddy_login

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.billbuddy_login.menubartrail.MenuMainActivity
import com.example.billbuddy_login.databinding.ActivityWelcomeScreenBinding

class WelcomeScreenActivity : AppCompatActivity() {

    private lateinit var binding: ActivityWelcomeScreenBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityWelcomeScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnAddYourApartment.setOnClickListener {
            startActivity(Intent(this, MenuMainActivity::class.java))
        }

        binding.btnAddYourGroupTrip.setOnClickListener {
            startActivity(Intent(this, MenuMainActivity::class.java))
        }

        binding.btnSkipForNow.setOnClickListener {
            startActivity(Intent(this, MenuMainActivity::class.java))
        }
    }
}
