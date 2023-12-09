package com.example.billbuddy

import android.content.Intent
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.example.billbuddy.databinding.ActivityLogInAndSignUpBinding
import com.example.billbuddy.jing.FragmentMainActivity
import com.example.billbuddy.menubartrail.MenuMainActivity
import com.example.billbuddy.vinay.database.sharedpreferences.PreferenceHelper
import com.example.billbuddy.vinay.database.users.UserEntity
import com.example.billbuddy.vinay.viewmodels.UserViewModel
import com.example.billbuddy.vinay.viewmodels.UserViewModelFactory
import com.example.billbuddy.vinay.views.SplitwiseApplication
import com.google.android.gms.common.api.ApiException

class LoginSignUp : AppCompatActivity(), View.OnClickListener {
    private lateinit var binding: ActivityLogInAndSignUpBinding
    private lateinit var googleSignInOptions: GoogleSignInOptions
    private lateinit var googleSignInClient: GoogleSignInClient
    private val SIGNIN_REQ_CODE = 40
    private lateinit var auth: FirebaseAuth
    private lateinit var userViewModel: UserViewModel
    private val preferenceHelper = PreferenceHelper(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialize Firebase
        FirebaseApp.initializeApp(this)

        binding = ActivityLogInAndSignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        createDatabase()
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
                    if (user != null) {
                        user.getDisplayName()?.let { Log.d("LoginActivity", it) }
                        user.getEmail()?.let { Log.d("LoginActivity", it) }

                        val userEmail = user.getEmail()
                        val userName = user.getDisplayName()

                        userViewModel.getUserList().observe(this, Observer { userList ->
                            var userExists = false
                            for (i in userList) {
                                if (i.email == userEmail && i.name == userName) {
                                    userExists = true
                                    val intent2 = Intent(this, FragmentMainActivity::class.java)
                                    intent2.putExtra("name", i.name)
                                    preferenceHelper.writeLongToPreference(
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
                                }
                            }

                            if (!userExists) {
                                val userEntity = UserEntity(
                                    name = userName!!,
                                    phone = "",
                                    email = userEmail!!,
                                    password = "",
                                    gender = "",
                                    owe = "0",
                                    owes = "0"
                                )
                                userViewModel.addUser(userEntity)
                            }
                        })

                        showSnackbar("SignIn successful")
                        updateUI(user)
                    } else {
                        Log.e("TAG", "Firebase authentication failed: ${task.exception?.message}")
                        showSnackbar("Authentication Failed. Please try again.")
                    }
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

    private fun createDatabase() {
        val appClass = application as SplitwiseApplication
        val userRepository = appClass.userRepository
        val userViewModelFactory = UserViewModelFactory(userRepository)

        userViewModel = ViewModelProvider(this, userViewModelFactory)
            .get(UserViewModel::class.java)
    }
}
