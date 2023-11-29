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
    private lateinit var ivImageTakerSplitWise: ImageView
    private lateinit var ivCameraSplitWise: ImageView
    private val cameraRequestSplitWise = 1222

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ivImageTakerSplitWise = binding.ivImageTakerSplitWise
        ivCameraSplitWise = binding.ivCameraSplitWise

        if (ContextCompat.checkSelfPermission(
                applicationContext,
                Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_DENIED
        )
            ActivityCompat.requestPermissions(
                this, arrayOf(Manifest.permission.CAMERA), cameraRequestSplitWise
            )

        ivCameraSplitWise.setOnClickListener {
            val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            startActivityForResult(cameraIntent, cameraRequestSplitWise)
        }

        binding.btnBackSign.setOnClickListener {
            val intent = Intent(this, LoginSignUp::class.java)
            startActivity(intent)
            finish()
        }

        binding.btnDoneSign.setOnClickListener {
            if (binding.etSignUpFullName.text.isNotEmpty() &&
                binding.etSignUpEmail.editText?.text!!.isNotEmpty() &&
                binding.etSignUpPassword.editText?.text!!.isNotEmpty()
            ) {

                createDatabase()
                val userEntity = UserEntity(
                    binding.etSignUpFullName.text.toString(),
                    binding.etSignUpPhone.text.toString(),
                    binding.etSignUpEmail.editText?.text!!.toString(),
                    binding.etSignUpPassword.editText?.text!!.toString(),
                    "",
                    "0",
                    "0",
                    0
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == cameraRequestSplitWise) {
            val images: Bitmap = data?.extras?.get("data") as Bitmap
            ivImageTakerSplitWise.setImageBitmap(images)
        }
    }
}
