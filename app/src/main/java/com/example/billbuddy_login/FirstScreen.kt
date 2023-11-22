package com.example.billbuddy_login

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.billbuddy_login.vinay.database.sharedpreferences.PreferenceHelper
import com.example.billbuddy_login.vinay.views.SplitwiseApplication
import com.example.billbuddy_login.databinding.ActivityFirstScreenBinding
import java.util.*
import kotlin.concurrent.timerTask

class FirstScreen : AppCompatActivity() {

    private lateinit var binding: ActivityFirstScreenBinding
    private val preferenceHelper by lazy { PreferenceHelper(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFirstScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        Glide.with(binding.screen).load(R.drawable.billbuddy_logo).into(binding.screen)

        val timer = Timer()
        timer.schedule(timerTask {
            if (!preferenceHelper.readBooleanFromPreference(SplitwiseApplication.PREF_IS_USER_LOGIN)) {
                val intent = Intent(this@FirstScreen, LoginSignUp::class.java)
                startActivity(intent)
            } else {
                val intent = Intent(this@FirstScreen, WelcomeScreenActivity::class.java)
                startActivity(intent)
            }
            finish()
        }, 2000)
    }
}
