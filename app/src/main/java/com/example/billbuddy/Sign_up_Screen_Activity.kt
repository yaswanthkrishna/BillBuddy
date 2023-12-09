package com.example.billbuddy

import android.Manifest
import android.content.Intent
import android.content.pm.ActivityInfo
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.example.billbuddy.databinding.ActivitySignUpScreenBinding
import com.example.billbuddy.vinay.database.users.UserEntity
import com.example.billbuddy.vinay.viewmodels.UserViewModel
import com.example.billbuddy.vinay.viewmodels.UserViewModelFactory
import com.example.billbuddy.vinay.views.SplitwiseApplication

class Sign_up_Screen_Activity : AppCompatActivity() {

    private lateinit var binding: ActivitySignUpScreenBinding
    private lateinit var userViewModel: UserViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_USER_PORTRAIT

        binding = ActivitySignUpScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)


        binding.btnBackSign.setOnClickListener {
            val intent = Intent(this, LoginSignUp::class.java)
            startActivity(intent)
            finish()
        }

        // Inside your onCreate method
        binding.btnDoneSign.setOnClickListener {
            val fullName = binding.etSignUpFullName.editText?.text.toString()
            val phone = binding.etSignUpPhone.editText?.text.toString()
            val email = binding.etSignUpEmail.editText?.text.toString()
            val password = binding.etSignUpPassword.editText?.text.toString()

            if (fullName.isEmpty() || phone.isEmpty() || email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Fill in all the fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Check if the email follows a valid pattern
            if (!isValidEmail(email)) {
                Toast.makeText(this, "Enter a valid email address", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            createDatabase()

            val userEntity = UserEntity(
                name = fullName,
                phone = phone,
                email = email,
                password = password,
                gender = "",
                owe = "0",
                owes = "0"
            )

            userViewModel.addUser(userEntity)

            val intent2 = Intent(this, Login_Screen_Activity::class.java)
            intent2.putExtra("email", userEntity.email)
            startActivity(intent2)
            finish()
        }

    }

    private fun isValidEmail(email: String): Boolean {
        val emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+"
        return email.matches(emailPattern.toRegex())
    }

    private fun createDatabase() {
        val appClass = application as SplitwiseApplication
        val userRepository = appClass.userRepository
        val userViewModelFactory = UserViewModelFactory(userRepository)

        userViewModel = ViewModelProvider(this, userViewModelFactory)
            .get(UserViewModel::class.java)
    }

}
