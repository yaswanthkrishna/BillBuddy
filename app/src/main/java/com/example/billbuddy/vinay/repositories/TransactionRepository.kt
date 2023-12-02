package com.example.billbuddy.vinay.repositories

import androidx.lifecycle.LiveData
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import com.example.billbuddy.vinay.database.transactions.TransactionDAO
import com.example.billbuddy.vinay.database.transactions.TransactionEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class TransactionRepository(private val dao: TransactionDAO) {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addTransaction(transactionEntity: TransactionEntity) {
        CoroutineScope(Dispatchers.IO).launch {
            dao.addTransaction(transactionEntity)
        }
    }

    fun getTransactionList(): LiveData<List<TransactionEntity>> {
        return dao.getTransactionList()
    }

    suspend fun updateTransaction(transactionEntity: TransactionEntity) {
        CoroutineScope(Dispatchers.IO).launch {
            dao.updateTransaction(transactionEntity)
        }
    }

    suspend fun deleteTransaction(transactionEntity: TransactionEntity) {
            dao.deleteTransaction(transactionEntity)
    }

    suspend fun getNextTransactionId(): Long? {
        return dao.getNextTransactionId()
    }

}
