package com.example.billbuddy.vinayactivity

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

import androidx.recyclerview.widget.LinearLayoutManager
import com.example.billbuddy.R
import com.example.billbuddy.databinding.ActivityCreateGroupBinding

class CreateGroup : AppCompatActivity() {

    private lateinit var binding: ActivityCreateGroupBinding
    private var category: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreateGroupBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val rvHorizontalList = binding.rvHorizontalList

        val horizontalLayoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        rvHorizontalList.layoutManager = horizontalLayoutManager

        val groupCategories = listOf(
            GroupCategory("Home",R.drawable.home),
            GroupCategory("Trip",R.drawable.trip),
            GroupCategory("Couple",R.drawable.couple),
            GroupCategory("Friends",R.drawable.friends),
            GroupCategory("Other",R.drawable.other)
        )

        val categoryAdapter = GroupCategoryAdapter(groupCategories) { selectedCategory ->

            // You can pass the selected category to the AddGroupMembers fragment
            category = selectedCategory.categoryName

        }

        binding.addMember.setOnClickListener {
            val groupName = binding.etGroupName.text.toString().trim()

            if (groupName.isNotEmpty()) {
                val addMemberFragment = AddGroupMembers()
                val bundle = Bundle()
                bundle.putString("groupName", groupName)
                bundle.putString("groupCategory", category)
                addMemberFragment.arguments = bundle

                val transaction = supportFragmentManager.beginTransaction()
                transaction.replace(R.id.fragmentContainer, addMemberFragment)
                transaction.addToBackStack(null)
                transaction.commit()
            } else {
                Toast.makeText(this, "Please enter a group name", Toast.LENGTH_SHORT).show()
            }
        }

        binding.rvHorizontalList.adapter = categoryAdapter

        binding.tvBackArrow.setOnClickListener {
            onBackPressed()
        }
    }

    override fun onBackPressed() {
        // Handle back button press here
        // For example, you can check if any fragments are in the back stack
        // and pop the fragment or finish the activity accordingly
        val fragmentManager = supportFragmentManager
        if (fragmentManager.backStackEntryCount > 0) {
            fragmentManager.popBackStack()
        } else {
            super.onBackPressed()
        }
    }
}
