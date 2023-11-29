package com.example.billbuddy_login

import DashboardActivity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.RelativeLayout
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
<<<<<<< Updated upstream:app/src/main/java/com/example/billbuddy_login/LoginSignUp.kt
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.example.billbuddy_login.databinding.ActivityLogInAndSignUpBinding
=======
import com.example.billbuddy.databinding.ActivityLogInAndSignUpBinding
import com.example.billbuddy.menubartrail.MenuMainActivity
>>>>>>> Stashed changes:app/src/main/java/com/example/billbuddy/LoginSignUp.kt
import com.google.android.gms.common.api.ApiException

class LoginSignUp : AppCompatActivity(), View.OnClickListener {
    private lateinit var binding: ActivityLogInAndSignUpBinding
    private lateinit var googleSignInOptions: GoogleSignInOptions
    private lateinit var googleSignInClient: GoogleSignInClient
    private val SIGNIN_REQ_CODE = 40
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialize Firebase
        FirebaseApp.initializeApp(this)

        binding = ActivityLogInAndSignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initButtons()
        initData()
    }

    private fun initButtons() {
        binding.btnlogin.setOnClickListener(this)
        binding.btnsignup.setOnClickListener(this)
        binding.btnsignwithgoogle.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btnlogin -> {
                val intent = Intent(this@LoginSignUp, Login_Screen_Activity::class.java)
                startActivity(intent)
            }
            R.id.btnsignup -> {
                val intent = Intent(this@LoginSignUp, Sign_up_Screen_Activity::class.java)
                startActivity(intent)
            }
            R.id.btnsignwithgoogle -> {
                val signInIntent = googleSignInClient.signInIntent
                startActivityForResult(signInIntent, SIGNIN_REQ_CODE)
            }
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == SIGNIN_REQ_CODE) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val account = task.getResult(ApiException::class.java)
                Log.d("TAG", "firebaseAuthWithGoogle:" + account?.id)
                firebaseAuthWithGoogle(account?.idToken)
            } catch (e: ApiException) {
                Log.e("TAG", "Google sign-in failed: ${e.statusCode}")
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
                    Log.e("TAG", "Firebase authentication failed: ${task.exception?.message}")
                    showSnackbar("Authentication Failed. Please try again.")
                }
            }
    }

    private fun updateUI(user: FirebaseUser?) {
        val intent = Intent(this@LoginSignUp, MenuMainActivity::class.java)
        startActivity(intent)
    }

    private fun showSnackbar(message: String) {
        Snackbar.make(binding.root, message, Snackbar.LENGTH_SHORT).show()
    }
}
