package com.example.billbuddy.yaswanth

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.billbuddy.R
import com.example.billbuddy.vinay.database.sharedpreferences.PreferenceHelper
import com.example.billbuddy.vinay.database.transactions.TransactionEntity
import com.example.billbuddy.vinay.views.SplitwiseApplication

class TransactionsAdapter(
    private val context: Context,
    private val onTransactionClick: (TransactionEntity) -> Unit
) : RecyclerView.Adapter<TransactionsAdapter.ViewHolder>() {
    private lateinit var preferenceHelper: PreferenceHelper
    private var transactions: List<TransactionEntity> = listOf()
    fun submitList(transactions: List<TransactionEntity>) {
        Log.d("TransactionsAdapter", "Submitting list: ${transactions.size}")
        this.transactions = transactions
        notifyDataSetChanged()
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_transaction, parent, false)
        return ViewHolder(view)
    }
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val transaction = transactions[position]
        holder.bind(transaction)
    }
    override fun getItemCount(): Int = transactions.size
    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val textViewDescription: TextView = view.findViewById(R.id.textViewTransactionDescription)
        private val textViewAmount: TextView = view.findViewById(R.id.textViewTransactionAmount)
        private val textViewDate: TextView = view.findViewById(R.id.textViewTransactionDate)
        private val transactionImage : ImageView = view.findViewById(R.id.transactionImage)
        fun bind(transaction: TransactionEntity) {
            textViewDescription.text = transaction.description ?: "No Description"
            textViewAmount.text = "${transaction.totalAmount}"
            textViewDate.text = transaction.transactionDateTime // Format this date as needed
            itemView.setOnClickListener { onTransactionClick(transaction) }
            transactionImage.setImageResource(R.drawable.ic_scroll)
            val paidBy = transaction.paidByUserId ?: -1
            preferenceHelper = PreferenceHelper(context)
            val userId = preferenceHelper.readLongFromPreference(SplitwiseApplication.PREF_USER_ID)
            if (userId == paidBy){
                transactionImage.setBackgroundResource(R.color.Dark_green)
            }else{
                transactionImage.setBackgroundResource(R.color.orange_splitwise)
            }
        }
    }
}
