package com.example.billbuddy.vinay.repositories

import androidx.lifecycle.LiveData
import com.example.billbuddy.vinay.database.recent_activity.RecentActivityTransactionDAO
import com.example.billbuddy.vinay.database.recent_activity.RecentActivityTransactionEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class RecentActivityTransactionRepository(private val dao: RecentActivityTransactionDAO) {

    suspend fun addRecentActivityTransaction(transactionEntity: RecentActivityTransactionEntity) {
        withContext(Dispatchers.IO) {
            dao.addRecentActivityTransaction(transactionEntity)
        }
    }

    fun getRecentActivityTransactionList(): LiveData<List<RecentActivityTransactionEntity>>{
        return dao.getRecentActivityTransactionList()
    }

    suspend fun updateRecentActivityTransactionMember(transactionEntity: RecentActivityTransactionEntity) {
        withContext(Dispatchers.IO) {
            dao.updateRecentActivityTransactionMember(transactionEntity)
        }
    }

    suspend fun deleteRecentActivityTransactionMember(transactionEntity: RecentActivityTransactionEntity) {
        withContext(Dispatchers.IO) {
            dao.deleteRecentActivityTransactionMember(transactionEntity)
        }
    }
}