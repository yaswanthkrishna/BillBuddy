package com.example.billbuddy

import DashboardActivity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.billbuddy.databinding.ActivityLoginBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class LoginActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initButtons()
    }

    private fun initButtons() {
        binding.btnSave.setOnClickListener(this)
        binding.btnback.setOnClickListener(this)
        binding.forgotpassword.setOnClickListener(this)
        auth = Firebase.auth
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btnback -> {
                val intent = Intent(this@LoginActivity, LoginSignUp::class.java)
                startActivity(intent)
            }
            R.id.btnSave -> {
                signIn()
            }
            R.id.forgotpassword -> {
                val intent = Intent(this@LoginActivity, ResetPasswordActivity::class.java)
                startActivity(intent)
            }
        }
    }

    private fun signIn() {
        binding.email.error = null
        binding.password.error = null

        if (binding.email.text.toString().isEmpty()) {
            binding.email.error = "Please enter email"
            return
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(binding.email.text.toString()).matches()) {
            binding.email.error = "Please enter a valid email"
            return
        }
        if (binding.password.text.toString().isEmpty()) {
            binding.password.error = "Please enter Password"
            return
        }

        auth.signInWithEmailAndPassword(binding.email.text.toString(), binding.password.text.toString())
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    val user = auth.currentUser
                    Log.d("Lakshmi", "signInWithEmail:success")
                    startActivity(Intent(this@LoginActivity, DashboardActivity::class.java))
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w("Lakshmi", "signInWithEmail:failure", task.exception)
                    Toast.makeText(
                        baseContext, "Authentication failed: ${task.exception?.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
    }
}
