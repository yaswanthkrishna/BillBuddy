package com.example.billbuddy.yaswanth

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.commit
import com.example.billbuddy.R

class ExpenditureActivity : AppCompatActivity() {

    private lateinit var expenditureFragment: ExpenditureFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_expenditure)

        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        // Initialize the fragment
        expenditureFragment = if (savedInstanceState == null) {
            val fragment = ExpenditureFragment()
            supportFragmentManager.commit {
                replace(R.id.fragment_container, fragment)
            }
            fragment
        } else {
            supportFragmentManager.findFragmentById(R.id.fragment_container) as ExpenditureFragment
        }

    }
}
