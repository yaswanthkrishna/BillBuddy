package com.example.billbuddy.menubartrail.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.button.MaterialButton
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.billbuddy.GroupDetail
import com.example.billbuddy.R
import com.example.billbuddy.vinay.database.SplitwiseDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class GroupsFragment : Fragment() {
    private lateinit var rvGroups: RecyclerView
    private lateinit var tvOverallAmount: TextView
    private lateinit var viewModel: GroupsViewModel
    companion object {
        fun Double.format(digits: Int) = "%.${digits}f".format(this)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val database = SplitwiseDatabase.getDatabase(requireContext())
        val groupDao = database.getMyGroupListEntries()
        val factory = GroupsViewModelFactory(groupDao, 0L)
        viewModel = ViewModelProvider(this, factory)[GroupsViewModel::class.java]
        viewModel.refreshGroupsList()
        val userEmail = arguments?.getString("email") ?: ""
        getCurrentUserId(userEmail) { userId ->
            viewModel.updateUserId(userId)
        }
    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.fragment_groups, container, false)
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
        rvGroups = view.findViewById(R.id.groupsList)
        tvOverallAmount = view.findViewById(R.id.tvOverallAmount2_group)
        rvGroups.layoutManager = LinearLayoutManager(requireContext())
        rvGroups.adapter = GroupsAdapter(emptyList())
        viewModel.groupDetailsList.observe(viewLifecycleOwner) { groupDetails ->
            if (groupDetails != null && groupDetails.isNotEmpty()) {
                updateList(groupDetails)
            } else {
                // Handle empty or null list, e.g., show a message or hide the list
            }
        }
        viewModel.totalAmount.observe(viewLifecycleOwner) { total ->
            tvOverallAmount.text = when {
                total > 0 -> "Total Amount You are Owed: $${total.format(2)}"
                total < 0 -> "Total Amount You Owe: -\$${total.format(2)}"
                else -> "Total Amount: $0.00"
            }
        }
        viewModel.refreshGroupsList()
    }
    override fun onResume() {
        super.onResume()
        viewModel.refreshGroupsList()
    }
    private fun updateList(groups: List<GroupDetail>) {
        val adapter = rvGroups.adapter as? GroupsAdapter
        adapter?.updateList(groups)
    }
    private fun Double.format(digits: Int) = "%.${digits}f".format(this)
}