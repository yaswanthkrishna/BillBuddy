package com.example.billbuddy

import android.content.Intent
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.example.billbuddy.databinding.ActivityLogInAndSignUpBinding
import com.example.billbuddy.menubartrail.MenuMainActivity
import com.example.billbuddy.vinay.database.sharedpreferences.PreferenceHelper
import com.google.android.gms.common.api.ApiException

class LoginSignUp : AppCompatActivity() {
    private lateinit var binding: ActivityLogInAndSignUpBinding
    private lateinit var googleSignInOptions: GoogleSignInOptions
    private lateinit var googleSignInClient: GoogleSignInClient
    private lateinit var auth: FirebaseAuth
    private val SIGNIN_REQ_CODE = 40
    private lateinit var preferenceHelper: PreferenceHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialize Firebase
        FirebaseApp.initializeApp(this)

        binding = ActivityLogInAndSignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)
        preferenceHelper = PreferenceHelper(this)

        initButtons()
        initData()
        checkLoginAndRedirect()
    }

    private fun initButtons() {
        binding.btnlogin.setOnClickListener {
            val intent = Intent(this@LoginSignUp, Login_Screen_Activity::class.java)
            startActivity(intent)
        }
        binding.btnsignup.setOnClickListener {
            val intent = Intent(this@LoginSignUp, Sign_up_Screen_Activity::class.java)
            startActivity(intent)
        }
        binding.btnsignwithgoogle.setOnClickListener {
            val signInIntent = googleSignInClient.signInIntent
            startActivityForResult(signInIntent, SIGNIN_REQ_CODE)
        }
    }

    private fun initData() {
        googleSignInOptions = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        auth = FirebaseAuth.getInstance()
        googleSignInClient = GoogleSignIn.getClient(this, googleSignInOptions)
    }

    private fun checkLoginAndRedirect() {
        val userEmail = preferenceHelper.readStringFromPreference("USER_EMAIL")
        if (!userEmail.isNullOrEmpty()) {
            val lastActivityName = preferenceHelper.readActivityFromPreference()
            if (lastActivityName.isNotEmpty()) {
                try {
                    val intent = Intent(this, Class.forName(lastActivityName))
                    startActivity(intent)
                    finish()
                } catch (e: ClassNotFoundException) {
                    e.printStackTrace()
                    // Handle the error or redirect to a default activity
                }
            } else {
                val intent = Intent(this, MenuMainActivity::class.java)
                startActivity(intent)
                finish()
            }
        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == SIGNIN_REQ_CODE) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val account = task.getResult(ApiException::class.java)
                firebaseAuthWithGoogle(account?.idToken)
            } catch (e: ApiException) {
                showSnackbar("Google Sign-In Failed. Please try again.")
            }
        }
    }

    private fun firebaseAuthWithGoogle(idToken: String?) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val user = auth.currentUser
                    showSnackbar("SignIn successful")
                    updateUI(user)
                } else {
                    showSnackbar("Authentication Failed. Please try again.")
                }
            }
    }

    private fun updateUI(user: FirebaseUser?) {
        // Update UI after SignIn, example: Redirect to MenuMainActivity
        val intent = Intent(this@LoginSignUp, MenuMainActivity::class.java)
        startActivity(intent)
    }

    private fun showSnackbar(message: String) {
        Snackbar.make(binding.root, message, Snackbar.LENGTH_SHORT).show()
    }
}