package com.example.billbuddy

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.content.ContextCompat
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.billbuddy.vinay.database.friend_non_group.AppDatabase
import com.example.billbuddy.vinay.database.friend_non_group.FriendDAO
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class FriendsFragment : Fragment() {
    private lateinit var rvFriends: RecyclerView
    private lateinit var tvOverallAmount: TextView
    private lateinit var btnAddFriend: MaterialButton
    private lateinit var searchBar: AppCompatEditText
    private lateinit var btnFilter: MaterialButton
    private lateinit var btnRefresh: MaterialButton
    private var currentFilter = "all"
    private lateinit var viewModel: FriendsViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val database = AppDatabase.getDatabase(requireContext())
        val friendDao = database.friendDao()
        val factory = FriendsViewModelFactory(friendDao, 0L) // Temporary initialization
        viewModel = ViewModelProvider(this, factory)[FriendsViewModel::class.java]

        val userEmail = arguments?.getString("email") ?: ""
        getCurrentUserId(userEmail) { userId ->
            // Update ViewModel here
            viewModel.updateUserId(userId)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.fragment_friends, container, false)
    }
    private fun getCurrentUserId(email: String, callback: (Long) -> Unit) {
        CoroutineScope(Dispatchers.IO).launch {
            val userDAO = AppDatabase.getDatabase(requireContext()).userDao()
            val userId = userDAO.getUserIdByEmail(email) ?: 0L
            withContext(Dispatchers.Main) {
                callback(userId)
            }
        }
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        rvFriends = view.findViewById(R.id.rvFriendsList)
        tvOverallAmount = view.findViewById(R.id.tvOverallAmount2)
        btnAddFriend = view.findViewById(R.id.btnAddFriends)
        searchBar = view.findViewById(R.id.searchBar2) as AppCompatEditText
        btnFilter = view.findViewById(R.id.btnFilter2)
        btnRefresh = view.findViewById(R.id.btnRefresh2)
        rvFriends.adapter = FriendsAdapter(emptyList())
        rvFriends.layoutManager = LinearLayoutManager(requireContext())
        btnAddFriend.setOnClickListener {
            navigateToAddFriend()
        }
        searchBar.addTextChangedListener { text ->
            viewModel.searchFriends(text.toString())
        }
        viewModel.friendsList.observe(viewLifecycleOwner) { friendsList ->
            (rvFriends.adapter as FriendsAdapter).updateList(friendsList)
            updateOverallAmountDisplay()
        }
        btnFilter.setOnClickListener {
            showFilterOptions() // Implement this method to show filter options
        }
        btnRefresh.setOnClickListener {
            viewModel.refreshFriendsList()
        }
        viewModel.friendsList.observe(viewLifecycleOwner) { friendsList ->
            if (friendsList.isEmpty()) {
                // Handle empty list: You can show a message or hide certain views
                rvFriends.visibility = View.GONE
                // Set a message like "No friends available" to a TextView if needed
            } else {
                rvFriends.visibility = View.VISIBLE
                (rvFriends.adapter as FriendsAdapter).updateList(friendsList)
                updateOverallAmountDisplay()
            }
        }
    }
    private fun updateOverallAmountDisplay() {
        val overallAmount = viewModel.calculateOverallAmount()
        tvOverallAmount.text = if (overallAmount == 0.0) "Total Amount: $0.00" else "$${overallAmount.format(2)}"
        // Change color based on whether the user owes or is owed money
        val color = if (overallAmount >= 0) R.color.colorGreen else R.color.colorRed
        tvOverallAmount.setTextColor(ContextCompat.getColor(requireContext(), color))
    }
    private fun Double.format(digits: Int) = "%.${digits}f".format(this)
    private fun navigateToAddFriend() {
        // findNavController().navigate(R.id.action_friendsFragment_to_addFriendFragment)
    }
    private fun showFilterOptions() {
        if (currentFilter == "all") {
            viewModel.filterFriendsList("credit")
            currentFilter = "credit"
        }
        else {
            viewModel.filterFriendsList("all")
            currentFilter = "all"
        }
    }
}
