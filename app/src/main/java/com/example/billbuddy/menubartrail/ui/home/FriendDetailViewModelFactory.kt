package com.example.billbuddy.menubartrail.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.billbuddy.vinay.database.transactions.TransactionDAO
import com.example.billbuddy.vinay.viewmodels.FriendDetailViewModel

class FriendDetailViewModelFactory(
    private val transactionDAO: TransactionDAO,
    private val friendId: Long
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(FriendDetailViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return FriendDetailViewModel(transactionDAO, friendId) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
