package com.example.billbuddy.menubartrail.ui.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.TextView
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.content.ContextCompat
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.button.MaterialButton
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.billbuddy.R
import com.example.billbuddy.vinay.database.SplitwiseDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.math.log

class FriendsFragment : Fragment() {
    private lateinit var rvFriends: RecyclerView
    private lateinit var tvOverallAmount: TextView
    private lateinit var searchBar: AppCompatEditText
    private lateinit var btnRefresh: MaterialButton
    private var currentFilter = "all"
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
    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.fragment_friends, container, false)
        viewModel.refreshFriendsList()
    }
    private fun getCurrentUserId(email: String, callback: (Long) -> Unit) {
        CoroutineScope(Dispatchers.IO).launch {
            val userDAO = SplitwiseDatabase.getDatabase(requireContext()).getMyUserEntries()
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
        searchBar = view.findViewById(R.id.searchBar2) as AppCompatEditText
        btnRefresh = view.findViewById(R.id.btnRefresh2)
        rvFriends.adapter = FriendsAdapter(viewModel.friendsList.value ?: emptyList())
        rvFriends.layoutManager = LinearLayoutManager(requireContext())
        searchBar.addTextChangedListener { text ->
            viewModel.searchFriends(text.toString())
        }
        btnRefresh.setOnClickListener {
            viewModel.refreshFriendsList()
        }
        viewModel.friendsList.observe(viewLifecycleOwner) { friendsList ->
            if (friendsList.isEmpty()) {
                rvFriends.visibility = View.GONE
            } else {
                rvFriends.visibility = View.VISIBLE
                val adapter = FriendsAdapter(friendsList)
                rvFriends.adapter = adapter
                updateOverallAmountDisplay()
            }
        }
        viewModel.refreshFriendsList()
    }
    private fun updateOverallAmountDisplay() {
        val overallAmount = viewModel.calculateOverallAmount()
        tvOverallAmount.text = if (overallAmount == 0.0) "Total Amount: $0.00" else if(overallAmount > 0.0) "Total Amount You are Owed: $${overallAmount.format(2)}" else "Total Amount You Owe: \$${overallAmount.format(2)}"
        val color = if (overallAmount >= 0) R.color.Dark_green else R.color.colorRed
        tvOverallAmount.setTextColor(ContextCompat.getColor(requireContext(), color))
        viewModel.refreshFriendsList()
    }
    private fun Double.format(digits: Int) = "%.${digits}f".format(this)
    /*private fun showFilterOptions() {
        if (currentFilter == "all") {
            viewModel.filterFriendsList("credit")
            currentFilter = "credit"
        }
        else {
            viewModel.filterFriendsList("all")
            currentFilter = "all"
        }
    }*/
}