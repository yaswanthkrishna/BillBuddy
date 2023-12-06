package com.example.billbuddy

import android.Manifest
import android.content.Intent
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
        binding = ActivitySignUpScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)


        binding.btnBackSign.setOnClickListener {
            val intent = Intent(this, LoginSignUp::class.java)
            startActivity(intent)
            finish()
        }

        binding.btnDoneSign.setOnClickListener {
            if (binding.etSignUpFullName.editText?.text!!.isNotEmpty() &&
                binding.etSignUpEmail.editText?.text!!.isNotEmpty() &&
                binding.etSignUpPassword.editText?.text!!.isNotEmpty()
            ) {

                createDatabase()
                val userEntity = UserEntity(
                    name=binding.etSignUpFullName.editText?.text!!.toString(),
                    phone=binding.etSignUpPhone.editText?.text!!.toString(),
                    email=binding.etSignUpEmail.editText?.text!!.toString(),
                    password=binding.etSignUpPassword.editText?.text!!.toString(),
                    gender="",
                    owe="0",
                    owes="0"
                )

                userViewModel.addUser(userEntity)

                val intent2 = Intent(this, Login_Screen_Activity::class.java)
                intent2.putExtra("email", userEntity.email)
                startActivity(intent2)
                finish()
            } else Toast.makeText(this, "fill all the fields", Toast.LENGTH_SHORT).show()
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
