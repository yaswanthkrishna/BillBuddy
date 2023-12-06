package com.example.billbuddy.menubartrail

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.billbuddy.R
import com.example.billbuddy.menubartrail.ui.home.HomeFragment
import com.example.billbuddy.vinayactivity.AddExpenseActivity
import com.example.billbuddy.vinay.database.sharedpreferences.PreferenceHelper
import com.example.billbuddy.vinayactivity.AddFriendActivity
import com.example.billbuddy.vinayactivity.CreateGroup
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.navigation.NavigationView

class MenuMainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private lateinit var drawerLayout: DrawerLayout
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var preferenceHelper: PreferenceHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)

        preferenceHelper = PreferenceHelper(this)

        setSupportActionBar(findViewById(R.id.toolbar))

        findViewById<FloatingActionButton>(R.id.fab).setOnClickListener { view ->
            val intent = Intent(this, AddExpenseActivity::class.java)
            startActivity(intent)
        }

        drawerLayout = findViewById(R.id.drawer_layout)
        val navView: NavigationView = findViewById(R.id.nav_view)
        val navController = findNavController(R.id.nav_host_fragment)

        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_home, R.id.nav_settings, R.id.nav_ScaneCode, R.id.nav_Contact
            ), drawerLayout
        )

        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        navView.setNavigationItemSelectedListener(this)
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

        }
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_addfriend -> {
                // Handle Add Friends on Splitwise click
                // Example: open a new activity or fragment
                val intent = Intent(this, AddFriendActivity::class.java)
                startActivity(intent)
                return true
            }
            R.id.action_creategroupe -> {
                // Handle Create Group click
                // Example: open a new activity or fragment
                val intent = Intent(this, CreateGroup::class.java)
                startActivity(intent)
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }
}
