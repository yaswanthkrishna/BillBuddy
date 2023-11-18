package com.example.billbuddy.ui.theme

// Import necessary libraries
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.example.billbuddy.R
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager

class FriendsFragment : Fragment() {
    // Declare your UI elements here
    private lateinit var rvFriends: RecyclerView
    private lateinit var tvOverallAmount: TextView
    private lateinit var btnAddFriend: MaterialButton
    private lateinit var searchBar: TextInputEditText
    private lateinit var btnFilter: MaterialButton
    private lateinit var btnRefresh: MaterialButton

    // Assuming you have a ViewModel to fetch and manage data
    private val viewModel: FriendsViewModel by viewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        // Inflate your layout for this fragment
        return inflater.inflate(R.layout.fragment_friends, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // Initialize your UI elements here
        rvFriends = view.findViewById(R.id.rvFriendsList)
        tvOverallAmount = view.findViewById(R.id.tvOverallAmount2)
        btnAddFriend = view.findViewById(R.id.btnAddFriends)
        searchBar = view.findViewById(R.id.searchBar2)
        btnFilter = view.findViewById(R.id.btnFilter2)
        btnRefresh = view.findViewById(R.id.btnRefresh2)

        // Set up RecyclerView with an adapter and a layout manager
        rvFriends.adapter = FriendsAdapter(emptyList()) // Pass in the initial empty list or data from the ViewModel
        rvFriends.layoutManager = LinearLayoutManager(requireContext())

        // Set up listeners and bindings for search, filter, and refresh functionalities
        btnAddFriend.setOnClickListener {
            // Code to navigate to AddFriendActivity
        }
        searchBar.addTextChangedListener {
            // Code to filter the friends list based on the search query
        }
        btnFilter.setOnClickListener {
            // Code to show filter options and apply selected filters
        }
        btnRefresh.setOnClickListener {
            // Code to refresh data from the database
        }

        // Observe data changes from the ViewModel and update UI accordingly
        viewModel.friendsList.observe(viewLifecycleOwner) { friendsList ->
            // Update the adapter with the new list
            (rvFriends.adapter as FriendsAdapter).updateList(friendsList)
            // Calculate and update the overall amount
            tvOverallAmount.text = calculateOverallAmount(friendsList)
        }
    }

    // Method to calculate the overall amount
    private fun calculateOverallAmount(friendsList: List<Friend>): String {
        // Implement your logic to calculate the overall amount
        // Sum up what others owe you and subtract what you owe
        return "$0.00" // Placeholder
    }
}
