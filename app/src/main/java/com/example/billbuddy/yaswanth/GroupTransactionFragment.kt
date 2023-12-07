package com.example.billbuddy.yaswanth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.billbuddy.R
import com.example.billbuddy.databinding.FragmentTransactionsBinding
import com.example.billbuddy.vinay.database.SplitwiseDatabase
import com.example.billbuddy.vinay.database.transactions.TransactionEntity

class GroupTransactionFragment : Fragment() {
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
        transactionsAdapter = TransactionsAdapter(requireContext()) { transaction ->
            openTransactionDetails(transaction)
        }
        val recyclerView = view.findViewById<RecyclerView>(R.id.recyclerView_trans)
        recyclerView.adapter = transactionsAdapter
        recyclerView.layoutManager = LinearLayoutManager(context)
        loadTransactions()
    }

    private fun loadTransactions() {
        viewModel.getGroupTransactions().observe(viewLifecycleOwner, Observer { transactions ->
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
    private fun loadGroupTransactions() {
        // Implement logic to load group transactions and update the adapter
        // Example: transactionsAdapter.submitList(groupTransactions)
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
    companion object {
        fun newInstance(): GroupTransactionFragment {
            return GroupTransactionFragment()
        }
    }
}
