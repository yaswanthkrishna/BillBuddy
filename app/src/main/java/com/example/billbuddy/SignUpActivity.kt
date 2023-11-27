package com.example.billbuddy

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.example.billbuddy.databinding.ActivitySignUpBinding

class SignUpActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var databaseReference: DatabaseReference
    private lateinit var binding: ActivitySignUpBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initButtons()
    }

    private fun initButtons() {
        binding.btnSaveUser.setOnClickListener(this)
        binding.btnback.setOnClickListener(this)
        binding.byusingsignup.setOnClickListener(this)
        firebaseAuth = FirebaseAuth.getInstance()
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btnback -> {
                val intent = Intent(this@SignUpActivity, LoginSignUp::class.java)
                startActivity(intent)
            }
            R.id.btnSaveUser -> {
                signUp()
            }
            R.id.btnsignup -> {
                val intent = Intent(this@SignUpActivity, SignUpActivity::class.java)
                startActivity(intent)
            }
        }
    }

    private fun signUp() {
        if (binding.mail.text.toString().isEmpty()) {
            binding.mail.error = "Please enter email"
            binding.mail.requestFocus()
            return
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(binding.mail.text.toString()).matches()) {
            binding.mail.error = "Please enter a valid email"
            binding.mail.requestFocus()
            return
        }

        if (binding.pwd.text.toString().isEmpty()) {
            binding.pwd.error = "Please enter Password"
            binding.pwd.requestFocus()
            return
        }

        if (binding.etFullname.text.toString().isEmpty()) {
            binding.etFullname.error = "Please Enter name"
            binding.etFullname.requestFocus()
            return
        }

        if (binding.etPhoneNo.text.toString().isEmpty()) {
            binding.etPhoneNo.error = "Please Enter phone number"
            binding.etPhoneNo.requestFocus()
            return
        }

        register(
            binding.etFullname.text.toString(),
            binding.mail.text.toString(),
            binding.pwd.text.toString(),
            binding.etPhoneNo.text.toString()
        )
    }

    private fun register(mUsername: String, mMail: String, mPwd: String, phoneNo: String) {
        firebaseAuth.createUserWithEmailAndPassword(mMail, mPwd)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val user = firebaseAuth.currentUser
                    val userId: String = user!!.uid
                    databaseReference =
                        FirebaseDatabase.getInstance().getReference("Users").child(userId)

                    val data = Database(
                        userId,
                        email = mMail,
                        phoneNo = phoneNo,
                        name = mUsername
                    )

                    try {
                        databaseReference.setValue(data)
                            .addOnCompleteListener(this) { task ->
                                if (task.isSuccessful) {
                                    val intent = Intent(this@SignUpActivity, LoginActivity::class.java)
                                    startActivity(intent)
                                } else {
                                    Toast.makeText(
                                        baseContext, "Failed.",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            }
                    } catch (e: Exception) {
                        Log.e("Debug", "Exception: ${e.message}")
                    }
                } else {
                    Log.w("Debug", "signInWithEmail:failure", task.exception)
                    Toast.makeText(
                        baseContext, "Authentication failed.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
    }
}
