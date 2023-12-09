package com.example.billbuddy

import android.content.Intent
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.billbuddy.databinding.ActivityLoginScreenBinding
import com.example.billbuddy.jing.FragmentMainActivity
import com.example.billbuddy.vinay.database.sharedpreferences.PreferenceHelper
import com.example.billbuddy.vinay.viewmodels.UserViewModel
import com.example.billbuddy.vinay.viewmodels.UserViewModelFactory
import com.example.billbuddy.vinay.views.SplitwiseApplication
import com.google.android.play.integrity.internal.i

class Login_Screen_Activity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginScreenBinding
    private lateinit var userViewModel: UserViewModel
    private val preferenceHelper = PreferenceHelper(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_USER_PORTRAIT

        binding = ActivityLoginScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (intent.getStringExtra("email").toString() != "null") {
            binding.etEmail.editText?.setText(intent.getStringExtra("email").toString())
        }

        createDatabase()

        binding.btnBackLogin.setOnClickListener {
            val intent = Intent(this, LoginSignUp::class.java)
            startActivity(intent)
        }

        // Inside your onCreate method
        binding.btnDoneLogin.setOnClickListener {
            val email = binding.etEmail.editText?.text.toString()
            val password = binding.etPassword.editText?.text.toString()

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Email and password are required", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            userViewModel.getUserList().observe(this, Observer { userList ->
                var isUserAuthenticated = false

                for (user in userList) {
                    if (user.email == email && user.password == password) {
                        isUserAuthenticated = true
                        val intent2 = Intent(this, FragmentMainActivity::class.java)
                        intent2.putExtra("name", user.name)
                        preferenceHelper.writeLongToPreference(
                            SplitwiseApplication.PREF_USER_ID,
                            user.user_id!!
                        )
                        preferenceHelper.writeBooleanToPreference(
                            SplitwiseApplication.PREF_IS_USER_LOGIN,
                            true
                        )
                        preferenceHelper.writeBooleanToPreference(SplitwiseApplication.PREF_IS_USER_LOGIN, true);

                        preferenceHelper.writeStringToPreference("USER_NAME", user.name)
                        preferenceHelper.writeStringToPreference("USER_EMAIL", user.email)
                        startActivity(intent2)
                        finish()
                        break
                    }
                }

                if (!isUserAuthenticated) {
                    Toast.makeText(this, "Email or Password is incorrect", Toast.LENGTH_SHORT).show()
                }
            })
        }

    }

    private fun createDatabase() {
        val appClass = application as SplitwiseApplication
        val userRepository = appClass.userRepository
        val userViewModelFactory = UserViewModelFactory(userRepository)

        userViewModel = ViewModelProvider(this, userViewModelFactory)
            .get(UserViewModel::class.java)
    }
}
