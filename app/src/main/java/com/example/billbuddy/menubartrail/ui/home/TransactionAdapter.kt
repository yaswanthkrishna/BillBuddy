package com.example.billbuddy.menubartrail.ui.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.billbuddy.R
import com.example.billbuddy.Transaction
import com.example.billbuddy.vinay.database.transactions.TransactionEntity

class TransactionAdapter(private var transactions: List<TransactionEntity>) :
    RecyclerView.Adapter<TransactionAdapter.TransactionViewHolder>() {
    inner class TransactionViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val transactionDescription: TextView = view.findViewById(R.id.textViewTransactionDescription)
        val transactionAmount: TextView = view.findViewById(R.id.textViewTransactionAmount)
        val transactionDate: TextView = view.findViewById(R.id.textViewTransactionDate)
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TransactionViewHolder {
        return TransactionViewHolder(
            LayoutInflater.from(parent.context)
            .inflate(R.layout.item_transaction, parent, false))
    }
    override fun onBindViewHolder(holder: TransactionViewHolder, position: Int) {
        val transaction = transactions[position]
        holder.transactionDescription.text = transaction.description
        holder.transactionAmount.text = transaction.totalAmount.toString()
        holder.transactionDate.text = transaction.transactionDateTime
    }
    override fun getItemCount() = transactions.size
    fun updateData(newTransactions: List<TransactionEntity>) {
        transactions = newTransactions
        notifyDataSetChanged()
    }
}