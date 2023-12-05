package com.example.billbuddy.vinay.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.billbuddy.vinay.database.recent_activity.RecentActivityTransactionEntity
import com.example.billbuddy.vinay.repositories.RecentActivityTransactionRepository
import kotlinx.coroutines.launch

class RecentActivityTransactionViewModel(private val repository: RecentActivityTransactionRepository) :
    ViewModel() {

    fun addRecentActivityTransaction(transactionEntity: RecentActivityTransactionEntity) {
        viewModelScope.launch {
            repository.addRecentActivityTransaction(transactionEntity)
        }
    }

    fun getRecentActivityTransactionList(): LiveData<List<RecentActivityTransactionEntity>>{
        return repository.getRecentActivityTransactionList()
    }

    fun updateGroupTransactionMember(transactionEntity: RecentActivityTransactionEntity) {
        viewModelScope.launch {
            repository.updateRecentActivityTransactionMember(transactionEntity)
        }
    }

    fun deleteGroupTransactionMember(transactionEntity: RecentActivityTransactionEntity){
        viewModelScope.launch {
            repository.deleteRecentActivityTransactionMember(transactionEntity)
        }
    }
}


class RecentActivityTransactionViewModelFactory(private val repository: RecentActivityTransactionRepository) :
    ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(RecentActivityTransactionViewModel::class.java)) {
            return RecentActivityTransactionViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}