package com.example.billbuddy.yaswanth

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.preference.PreferenceManager
import com.example.billbuddy.Login_Screen_Activity
import com.example.billbuddy.menubartrail.MenuMainActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        handleRedirection()
    }

    private fun handleRedirection() {
        val prefs = getSharedPreferences("login_pref", MODE_PRIVATE)
        val userEmail = prefs.getString("USER_EMAIL", "")

        if (userEmail.isNullOrEmpty()) {
            // User not logged in, redirect to Login Screen
            startActivity(Intent(this, Login_Screen_Activity::class.java))
        } else {
            // User is logged in, redirect to last activity or default activity
            val lastActivity = prefs.getString("LAST_ACTIVITY", MenuMainActivity::class.java.name)
            try {
                val intent = Intent(this, Class.forName(lastActivity))
                startActivity(intent)
            } catch (e: ClassNotFoundException) {
                // If last activity class not found, go to default
                startActivity(Intent(this, MenuMainActivity::class.java))
            }
        }
        finish()
    }
}
