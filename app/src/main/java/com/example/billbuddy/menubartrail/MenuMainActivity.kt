package com.example.billbuddy.menubartrail

import android.content.Intent
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.preference.PreferenceManager
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.billbuddy.LoginActivity
import com.example.billbuddy.Login_Screen_Activity
import com.example.billbuddy.R
import com.example.billbuddy.vinayactivity.AddExpenseActivity
import com.example.billbuddy.vinay.database.sharedpreferences.PreferenceHelper
import com.example.billbuddy.vinayactivity.AddFriendActivity
import com.example.billbuddy.vinayactivity.CreateGroup
import com.example.billbuddy.vinayactivity.GroupExpenseActivity
import com.example.billbuddy.vinayactivity.SettleUpActivity
import com.example.billbuddy.yaswanth.ExpenditureFragment
import com.example.billbuddy.yaswanth.TransactionsActivity
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.navigation.NavigationView

class MenuMainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private lateinit var drawerLayout: DrawerLayout
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var preferenceHelper: PreferenceHelper
    private val rotateOpen: Animation by lazy { AnimationUtils.loadAnimation(this, R.anim.rotate_open_anim) }
    private val rotateClose: Animation by lazy { AnimationUtils.loadAnimation(this, R.anim.rotate_close_anim) }
    private val fromBottom: Animation by lazy { AnimationUtils.loadAnimation(this, R.anim.from_bottom_anim) }
    private val toBottom: Animation by lazy { AnimationUtils.loadAnimation(this, R.anim.to_bottom_anim) }
    private lateinit var floatingActionButton: FloatingActionButton
    private lateinit var floatingActionButton2: FloatingActionButton
    private lateinit var floatingActionButton3: FloatingActionButton
    private var clicked = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_USER_PORTRAIT
        setContentView(R.layout.activity_main2)
        preferenceHelper = PreferenceHelper(this)
        setSupportActionBar(findViewById(R.id.toolbar))
        drawerLayout = findViewById(R.id.drawer_layout)
        val navView: NavigationView = findViewById(R.id.nav_view)
        val navController = findNavController(R.id.nav_host_fragment)
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_home, R.id.nav_transaction, R.id.nav_settleUp, R.id.nav_Contact
            ), drawerLayout
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
        navView.setNavigationItemSelectedListener(this)
        floatingActionButton = findViewById(R.id.floatingActionButton)
        floatingActionButton2 = findViewById(R.id.floatingActionButton2)
        floatingActionButton3 = findViewById(R.id.floatingActionButton3)
        floatingActionButton.setOnClickListener {
            onAddButtonClicked()
        }
        floatingActionButton2.setOnClickListener {
            val intent = Intent(this, AddExpenseActivity::class.java)
            startActivity(intent)
        }
        floatingActionButton3.setOnClickListener {
            val intent = Intent(this, GroupExpenseActivity::class.java)
            startActivity(intent)
        }
    }
    override fun onResume() {
        super.onResume()
        preferenceHelper.writeStringToPreference("LAST_ACTIVITY", this::class.java.name)
    }
    override fun onPause() {
        super.onPause()
        preferenceHelper.writeStringToPreference("LAST_ACTIVITY", this::class.java.name)
    }
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main, menu)
        return true
    }
    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment)
        val headerView = findViewById<NavigationView>(R.id.nav_view).getHeaderView(0)
        headerView.findViewById<TextView>(R.id.tvUserName).text =
            "Hey, " + preferenceHelper.readStringFromPreference("USER_NAME") + "!"
        headerView.findViewById<TextView>(R.id.textView).text =
            preferenceHelper.readStringFromPreference("USER_EMAIL").toString()
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }
    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        drawerLayout.closeDrawer(GravityCompat.START)
        when (item.itemId) {
            R.id.nav_home -> {
                findNavController(R.id.nav_host_fragment).navigate(R.id.nav_home)
            }
            R.id.nav_settleUp-> {
                val intent = Intent(this, SettleUpActivity::class.java)
                startActivity(intent)
            }
            R.id.nav_transaction -> {
                val intent = Intent(this, TransactionsActivity::class.java)
                startActivity(intent)
            }
            R.id.nav_expenditure -> {
                supportFragmentManager.beginTransaction()
                    .replace(R.id.nav_host_fragment, ExpenditureFragment())
                    .addToBackStack(null)
                    .commit()
            }
            R.id.nav_Contact -> {
                logoutUser()
                return true
            }
        }
        return true
    }
    private fun logoutUser() {
        // Clear user session data
        preferenceHelper.clearUserSession()

        // Redirect to the login screen
        val loginIntent = Intent(this, Login_Screen_Activity::class.java)
        startActivity(loginIntent)
        finish() // Close the current activity
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_addfriend -> {
                val intent = Intent(this, AddFriendActivity::class.java)
                startActivity(intent)
                return true
            }
            R.id.action_creategroupe -> {
                val intent = Intent(this, CreateGroup::class.java)
                startActivity(intent)
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }
    private fun onAddButtonClicked(){
        setVisibility(clicked)
        setAnimation(clicked)
        clicked = !clicked
    }
    private fun setAnimation(clicked:Boolean) {
        if(!clicked){
            floatingActionButton2.startAnimation(fromBottom)
            floatingActionButton3.startAnimation(fromBottom)
            floatingActionButton.startAnimation(rotateOpen)
        }
        else{
            floatingActionButton2.startAnimation(toBottom)
            floatingActionButton3.startAnimation(toBottom)
            floatingActionButton.startAnimation(rotateClose)
        }
    }
    private fun setVisibility(clicked: Boolean) {
        if(!clicked){
            floatingActionButton2.visibility = View.VISIBLE
            floatingActionButton3.visibility = View.VISIBLE
        }
        else{
            floatingActionButton2.visibility = View.INVISIBLE
            floatingActionButton3.visibility = View.INVISIBLE
        }
    }
}
