package com.example.billbuddy

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.billbuddy.vinay.database.sharedpreferences.PreferenceHelper
import com.example.billbuddy.vinay.views.SplitwiseApplication

class LauncherActivity : AppCompatActivity() {
    private lateinit var preferenceHelper: PreferenceHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        preferenceHelper = PreferenceHelper(this)

        Log.d("LauncherActivity", "onCreate called")

        val userId = preferenceHelper.readLongFromPreference(SplitwiseApplication.PREF_USER_ID) ?: 0L
        if (userId != 0L) {
            Log.d("LauncherActivity", "User ID is not 0, redirecting to MenuMainActivity")
            val intent = Intent().setClassName(this, "com.example.billbuddy.menubartrail.MenuMainActivity")
            startActivity(intent)
        } else {
            Log.d("LauncherActivity", "User ID is 0, redirecting to Login_Screen_Activity")
            val intent = Intent(this, Login_Screen_Activity::class.java)
            startActivity(intent)
        }
        finish() // Make sure this activity is removed from the back stack
    }
}
