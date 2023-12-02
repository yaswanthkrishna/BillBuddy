package com.example.billbuddy

import android.content.Intent
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

class Login_Screen_Activity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginScreenBinding
    private lateinit var userViewModel: UserViewModel
    private val preferenceHelper = PreferenceHelper(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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

        binding.btnDoneLogin.setOnClickListener {
            val email = binding.etEmail.editText?.text.toString()
            val password = binding.etPassword.editText?.text.toString()

            userViewModel.getUserList().observe(this, Observer {
                for (i in it) {
                    if (i.email == email && i.password == password) {
                        val intent2 = Intent(this, FragmentMainActivity::class.java)
                        intent2.putExtra("name", i.name)
                        preferenceHelper.writeIntToPreference(
                            SplitwiseApplication.PREF_USER_ID,
                            i.user_id!!
                        )
                        preferenceHelper.writeBooleanToPreference(
                            SplitwiseApplication.PREF_IS_USER_LOGIN,
                            true
                        )
                        preferenceHelper.writeStringToPreference("USER_NAME", i.name)
                        preferenceHelper.writeStringToPreference("USER_EMAIL", i.email)
                        startActivity(intent2)
                        finish()
                        break
                    } else {
                        Toast.makeText(this, "Email or Password is wrong", Toast.LENGTH_SHORT)
                            .show()
                    }
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
