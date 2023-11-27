package com.example.billbuddy

// Import necessary libraries
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager

class GroupsFragment : Fragment() {
    // Declare your UI elements here
    private lateinit var rvGroups: RecyclerView
    private lateinit var tvOverallAmount: TextView
    private lateinit var btnAddGroup: MaterialButton
    private lateinit var searchBar: TextInputEditText
    private lateinit var btnFilter: MaterialButton
    private lateinit var btnRefresh: MaterialButton

    // Assuming you have a ViewModel to fetch and manage data
    private val viewModel: GroupsViewModel by viewModels()
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        // Inflate your layout for this fragment
        return inflater.inflate(R.layout.fragment_groups, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // Initialize your UI elements here
        rvGroups = view.findViewById(R.id.rvGroupsList)
        tvOverallAmount = view.findViewById(R.id.tvOverallAmount)
        btnAddGroup = view.findViewById(R.id.btnAddGroup)
        searchBar = view.findViewById(R.id.searchBar)
        btnFilter = view.findViewById(R.id.btnFilter)
        btnRefresh = view.findViewById(R.id.btnRefresh)

        // Initialize the adapter with an empty list or data from the ViewModel
        val adapter = GroupsAdapter(emptyList())
        rvGroups.adapter = adapter
        rvGroups.layoutManager = LinearLayoutManager(requireContext())

        // Set up listeners and bindings for search, filter, and refresh functionalities
        btnAddGroup.setOnClickListener {
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
        viewModel.groupsList.observe(viewLifecycleOwner) { groupsList ->
            // Update the adapter with the new list
// This should reference rvGroups, not rvFriends
            (rvGroups.adapter as GroupsAdapter).updateList(groupsList)
            // Calculate and update the overall amount
            tvOverallAmount.text = calculateOverallAmount(groupsList)
        }
    }

    // Method to calculate the overall amount
    private fun calculateOverallAmount(groupsList: List<Group>): String {
        // Implement your logic to calculate the overall amount
        // Sum up what others owe you and subtract what you owe
        return "$0.00" // Placeholder
    }
}
