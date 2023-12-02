// GroupTransactionViewModel.kt

package com.example.billbuddy.vinay.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.ViewModelProvider
import com.example.billbuddy.vinay.database.groups.GroupTransactionDAO
import com.example.billbuddy.vinay.database.groups.GroupTransactionEntity
import com.example.billbuddy.vinay.repositories.GroupTransactionRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class GroupTransactionViewModel(private val repository: GroupTransactionRepository) : ViewModel() {

    suspend fun addGroupTransaction(groupTransactionEntity: GroupTransactionEntity) {
        withContext(Dispatchers.IO) {
            repository.addGroupTransaction(groupTransactionEntity)
        }
    }

    fun getGroupTransactions(groupId: Long): LiveData<List<GroupTransactionEntity>> {
        return repository.getGroupTransactions(groupId)
    }

    suspend fun updateGroupTransaction(groupTransactionEntity: GroupTransactionEntity) {
        withContext(Dispatchers.IO) {
            repository.updateGroupTransaction(groupTransactionEntity)
        }
    }

    suspend fun deleteGroupTransaction(groupTransactionEntity: GroupTransactionEntity) {
        withContext(Dispatchers.IO) {
            repository.deleteGroupTransaction(groupTransactionEntity)
        }
    }
}

class GroupTransactionViewModelFactory(private val repository: GroupTransactionRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(GroupTransactionViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return GroupTransactionViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
