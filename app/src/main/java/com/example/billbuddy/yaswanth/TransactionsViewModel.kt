package com.example.billbuddy.yaswanth

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.billbuddy.Friend
import com.example.billbuddy.vinay.database.transactions.TransactionDAO
import com.example.billbuddy.vinay.database.transactions.TransactionEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class TransactionsViewModel(private val transactionDao: TransactionDAO) : ViewModel() {
    private val transactions = MutableLiveData<List<TransactionEntity>>()
    fun getFriendTransactions(): LiveData<List<TransactionEntity>> {
        Log.d("TransactionsViewModel", "Fetching friend transactions")
        return transactionDao.getFriendTransactions()
    }
    fun getGroupTransactions(): LiveData<List<TransactionEntity>> {
        return transactionDao.getGroupTransactions()
    }
}
