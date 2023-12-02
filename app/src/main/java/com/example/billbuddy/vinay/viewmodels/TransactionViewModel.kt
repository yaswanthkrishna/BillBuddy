package com.example.billbuddy.vinay.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.billbuddy.vinay.database.transactions.TransactionEntity
import com.example.billbuddy.vinay.repositories.TransactionRepository
import kotlinx.coroutines.launch

class TransactionViewModel(private val repository: TransactionRepository) :
    ViewModel() {

    fun addTransaction(transactionEntity: TransactionEntity) {
        viewModelScope.launch {
            repository.addTransaction(transactionEntity)
        }
    }

    fun getTransactionList(): LiveData<List<TransactionEntity>> {
        return repository.getTransactionList()
    }

    fun updateTransaction(transactionEntity: TransactionEntity) {
        viewModelScope.launch {
            repository.updateTransaction(transactionEntity)
        }
    }

    fun deleteTransaction(transactionEntity: TransactionEntity) {
        viewModelScope.launch {
            repository.deleteTransaction(transactionEntity)
        }
    }
}

class TransactionViewModelFactory(private val repository: TransactionRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TransactionViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return TransactionViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
