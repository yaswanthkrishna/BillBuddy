package com.example.billbuddy

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.example.billbuddy.databinding.ActivityResetPasswordBinding

class ResetPasswordActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var auth: FirebaseAuth
    private lateinit var binding: ActivityResetPasswordBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityResetPasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()

        binding.btnreset.setOnClickListener(this)
        binding.btnback.setOnClickListener(this)
    }

    private fun resetPassword(email: String) {
        if (email.isEmpty()) {
            binding.etEmailAddress.error = "Please enter your email"
            return
        }

        auth.sendPasswordResetEmail(email).addOnCompleteListener(this) { task ->
            if (task.isSuccessful) {
                // Provide feedback to the user that the reset email has been sent
                Toast.makeText(
                    this@ResetPasswordActivity,
                    "Check email to reset your password!",
                    Toast.LENGTH_SHORT
                ).show()

                // Optionally, navigate the user to another screen or show a success message
                // Example: startActivity(Intent(this@ResetPasswordActivity, SuccessActivity::class.java))
            } else {
                Toast.makeText(
                    this@ResetPasswordActivity,
                    "Failed to send reset password email. ${task.exception?.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btnreset -> {
                resetPassword(binding.etEmailAddress.text.toString())
            }
            R.id.btnback -> {
                val intent = Intent(this@ResetPasswordActivity, LoginSignUp::class.java)
                startActivity(intent)
            }
        }
    }
}
