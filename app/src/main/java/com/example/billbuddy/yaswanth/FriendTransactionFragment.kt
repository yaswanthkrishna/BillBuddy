package com.example.billbuddy.yaswanth

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.billbuddy.R
import com.example.billbuddy.databinding.FragmentFriendsBinding
import com.example.billbuddy.databinding.FragmentTransactionsBinding
import com.example.billbuddy.vinay.database.SplitwiseDatabase
import com.example.billbuddy.vinay.database.transactions.TransactionEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class FriendTransactionFragment : Fragment() {
    private var _binding: FragmentTransactionsBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: TransactionsViewModel
    private lateinit var transactionsAdapter: TransactionsAdapter
    val transactionDao = context?.let { SplitwiseDatabase.getDatabase(it).getMyTransactionEntries() }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_transactions, container, false)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val transactionDao = SplitwiseDatabase.getDatabase(requireContext()).getMyTransactionEntries()
        viewModel = ViewModelProvider(this, TransactionsViewModelFactory(transactionDao))[TransactionsViewModel::class.java]
        transactionsAdapter = TransactionsAdapter(requireContext(),getCurrentUserId()) { transaction ->
            openTransactionDetails(transaction)
        }
        val recyclerView = view.findViewById<RecyclerView>(R.id.recyclerView_trans)
        recyclerView.adapter = transactionsAdapter
        recyclerView.layoutManager = LinearLayoutManager(context)
        loadTransactions()
    }

    private fun loadTransactions() {
        viewModel.getFriendTransactions().observe(viewLifecycleOwner, Observer { transactions ->
            Log.d("FriendTransactionFragment", "Transactions received: ${transactions.size}")
            transactionsAdapter.submitList(transactions)
        })
    }
    private fun openTransactionDetails(transaction: TransactionEntity) {
        val transactionDetailFragment = TransactionDetailFragment.newInstance(transaction)
        fragmentManager?.beginTransaction()
            ?.replace(R.id.fragmentContainerTransaction, transactionDetailFragment) // Corrected container ID
            ?.addToBackStack(null)
            ?.commit()
    }
    private fun getCurrentUserId(): Long {
        val sharedPreferences = requireActivity().getSharedPreferences("YourPreferenceName", Context.MODE_PRIVATE)
        return sharedPreferences.getLong("currentUserId", -1) // -1 or any default value indicating no ID was found
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
    companion object {
        fun newInstance(): FriendTransactionFragment {
            return FriendTransactionFragment()
        }
    }
}
