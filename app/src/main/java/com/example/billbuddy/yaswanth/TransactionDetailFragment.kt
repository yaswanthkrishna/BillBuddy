package com.example.billbuddy.yaswanth

// Necessary imports
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.billbuddy.R
import com.example.billbuddy.databinding.FragmentTransactionDetailBinding
import com.example.billbuddy.vinay.database.transactions.TransactionEntity
import androidx.fragment.app.Fragment

class TransactionDetailFragment : Fragment() {

    private var _binding: FragmentTransactionDetailBinding? = null
    private val binding get() = _binding!!
    private lateinit var transaction: TransactionEntity
    companion object {
        private const val TRANSACTION_KEY = "transaction"
        fun newInstance(transaction: TransactionEntity): TransactionDetailFragment {
            val fragment = TransactionDetailFragment()
            val args = Bundle()
            args.putSerializable(TRANSACTION_KEY, transaction)
            fragment.arguments = args
            return fragment
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        transaction = arguments?.getSerializable(TRANSACTION_KEY) as TransactionEntity
    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentTransactionDetailBinding.inflate(inflater, container, false)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.textViewTransactionId.text = "Transaction ID: ${transaction.transactionId}"
        binding.textViewPayer.text = "Paid by: ${transaction.paidByUserId}" // Replace with user's name if available
        binding.textViewAmount.text = "Amount: ${transaction.totalAmount}"
        binding.textViewDate.text = "Date: ${transaction.transactionDateTime}"
        binding.textViewOwedAmount.text = "Amount Owed: ${calculateOwedAmount(transaction)}" // Implement calculateOwedAmount method
        // ... populate other views ...
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
    private fun calculateOwedAmount(transaction: TransactionEntity): String {
        // Implement the logic to calculate the amount owed
        return "Some Amount"
    }
}
