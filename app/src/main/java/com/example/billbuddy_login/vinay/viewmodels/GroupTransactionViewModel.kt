package com.example.billbuddy_login.vinay.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.billbuddy_login.vinay.database.transactions.TransactionEntity
import com.example.billbuddy_login.vinay.repositories.GroupTransactionRepository

class GroupTransactionViewModel(val repository: GroupTransactionRepository) : ViewModel() {
    fun addTransaction(entity: TransactionEntity) {
        repository.addTransaction(entity)
    }

    fun getTransactionsList(): LiveData<List<TransactionEntity>> {
        return repository.getTransactionsList()
    }

    fun updateTransaction(entity: TransactionEntity) {
        repository.updateTransaction(entity)
    }

    fun deleteTransaction(entity: TransactionEntity) {
        repository.deleteTransaction(entity)
    }
}

/*class GroupTransactionViewModelFactory(val repository: GroupTransactionRepository) :
    ViewModelProvider.Factory {
    fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return GroupTransactionViewModel(repository) as T
    }
}*/