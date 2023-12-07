package com.example.billbuddy.vinay.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.billbuddy.vinay.database.transactions.TransactionDAO
import com.example.billbuddy.vinay.database.transactions.TransactionEntity

class FriendDetailViewModel(getMyTransactionEntries: TransactionDAO, friendId: Long) : ViewModel() {

    val transactionsList: LiveData<List<TransactionEntity>> = getMyTransactionEntries.getTransactionsForFriend(friendId)
    class Factory(private val transactionDAO: TransactionDAO, private val friendId: Long) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(FriendDetailViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return FriendDetailViewModel(transactionDAO, friendId) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}
