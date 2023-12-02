package com.example.billbuddy.vinay.repositories

import androidx.lifecycle.LiveData
import com.example.billbuddy.vinay.database.groups.GroupTransactionDAO
import com.example.billbuddy.vinay.database.groups.GroupTransactionEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class GroupTransactionRepository(private val dao: GroupTransactionDAO) {
    suspend fun addGroupTransaction(groupTransactionEntity: GroupTransactionEntity) {
        CoroutineScope(Dispatchers.IO).launch {
            dao.addGroupTransaction(groupTransactionEntity)
        }
    }

    fun getGroupTransactions(groupId: Long): LiveData<List<GroupTransactionEntity>> {
        return dao.getGroupTransactions(groupId)
    }

    suspend fun updateGroupTransaction(groupTransactionEntity: GroupTransactionEntity) {
        CoroutineScope(Dispatchers.IO).launch {
            dao.updateGroupTransaction(groupTransactionEntity)
        }
    }

    suspend fun deleteGroupTransaction(groupTransactionEntity: GroupTransactionEntity) {
        CoroutineScope(Dispatchers.IO).launch {
            dao.deleteGroupTransaction(groupTransactionEntity)
        }
    }
}

