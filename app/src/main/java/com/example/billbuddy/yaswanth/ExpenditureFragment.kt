package com.example.billbuddy.yaswanth

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AdapterView
import android.widget.Spinner
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.billbuddy.R
import com.example.billbuddy.vinay.database.SplitwiseDatabase
import com.example.billbuddy.vinay.database.sharedpreferences.PreferenceHelper
class ExpenditureFragment : Fragment() {
    private lateinit var viewModel: ExpenditureViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_expenditure, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val preferenceHelper = PreferenceHelper(requireContext())
        val userId = preferenceHelper.readLongFromPreference("USER_ID", -1L) // Use the same key "USER_ID"
        if (userId == -1L) {
            Log.e("ExpenditureFragment", "User ID is not found in preferences")
            // Handle the case when user ID is not found
            return
        }

        val database = SplitwiseDatabase.getDatabase(requireContext())
        val transactionDAO = database.getMyTransactionEntries()
        val friendDAO = database.getMyFriendEntries()
        val groupMemberDAO = database.getMyGroupMemberEntries()

        val viewModelFactory = ExpenditureViewModelFactory(transactionDAO, friendDAO, groupMemberDAO, userId)
        viewModel = ViewModelProvider(this, viewModelFactory)[ExpenditureViewModel::class.java]
        viewModel.expenditureStats.observe(viewLifecycleOwner, Observer { stats ->
            view.findViewById<TextView>(R.id.totalSpentOnGroups).text = "Total Spent on Groups: ${stats.totalSpentOnGroups}"
            view.findViewById<TextView>(R.id.totalSpentOnFriends).text = "Total Spent on Friends: ${stats.totalSpentOnFriends}"
            view.findViewById<TextView>(R.id.totalOwedByOthers).text = "Total Owed by Others: ${stats.totalOwedByOthers}"
        })

        setupSpinner(view)
    }

    private fun setupSpinner(view: View) {
        val spinner: Spinner = view.findViewById(R.id.spinnerTimeRange)
        ArrayAdapter.createFromResource(
            requireContext(),
            R.array.time_range_options,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinner.adapter = adapter
        }

        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, pos: Int, id: Long) {
                val timeRange = parent.getItemAtPosition(pos).toString()
                updatePieChart(timeRange)
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // Optional: Handle the case when nothing is selected
            }
        }
    }

    private fun updatePieChart(timeRange: String) {
        viewModel.updateExpenditureStats()
        // Potentially handle timeRange specific logic here
    }
}
