package com.example.billbuddy.menubartrail.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.billbuddy.R
import com.example.billbuddy.vinay.database.SplitwiseDatabase
import com.google.android.material.button.MaterialButton
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class FriendsFragment : Fragment() {
    private lateinit var rvFriends: RecyclerView
    private lateinit var tvOverallAmount: TextView
    private lateinit var viewModel: FriendsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val database = SplitwiseDatabase.getDatabase(requireContext())
        val friendDao = database.getMyFriendEntries()
        val factory = FriendsViewModelFactory(friendDao, 0L)
        viewModel = ViewModelProvider(this, factory)[FriendsViewModel::class.java]
        viewModel.refreshFriendsList()
        val userEmail = arguments?.getString("email") ?: ""
        getCurrentUserId(userEmail) { userId ->
            viewModel.updateUserId(userId)
        }
        viewModel.refreshFriendsList()
    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.fragment_friends, container, false)
    }
    override fun onResume() {
        super.onResume()
        viewModel.refreshFriendsList()
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        rvFriends = view.findViewById(R.id.rvFriendsList)
        tvOverallAmount = view.findViewById(R.id.tvOverallAmount2)
        // Set up RecyclerView
        rvFriends.layoutManager = LinearLayoutManager(requireContext())
        rvFriends.adapter = FriendsAdapter(emptyList())

        // Observe LiveData
        viewModel.friendsList.observe(viewLifecycleOwner) { friendsList ->
            if (friendsList.isEmpty()) {
                rvFriends.visibility = View.GONE
            } else {
                rvFriends.visibility = View.VISIBLE
                (rvFriends.adapter as FriendsAdapter).updateList(friendsList)
                updateOverallAmountDisplay()
            }
        }

        // Initiate data fetch
        getCurrentUserId(arguments?.getString("email") ?: "") { userId ->
            viewModel.updateUserId(userId)
            viewModel.refreshFriendsList()
        }

    }

    private fun getCurrentUserId(email: String, callback: (Long) -> Unit) {
        CoroutineScope(Dispatchers.IO).launch {
            val userId = SplitwiseDatabase.getDatabase(requireContext()).getMyUserEntries().getUserIdByEmail(email) ?: 0L
            withContext(Dispatchers.Main) {
                callback(userId)
            }
        }
    }
    private fun updateOverallAmountDisplay() {
        val overallAmount = viewModel.calculateOverallAmount()
        tvOverallAmount.text = if (overallAmount == 0.0) "Total Amount: $0.00" else if(overallAmount > 0.0) "Total Owed: $${overallAmount.format(2)}" else "Total Owe: \$${overallAmount.format(2)}"
        val color = if (overallAmount >= 0) R.color.Dark_green else R.color.orange_splitwise
        tvOverallAmount.setTextColor(ContextCompat.getColor(requireContext(), color))
        viewModel.refreshFriendsList()
    }
    private fun Double.format(digits: Int) = "%.${digits}f".format(this)
}