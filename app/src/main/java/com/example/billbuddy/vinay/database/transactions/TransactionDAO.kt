package com.example.billbuddy.vinay.database.transactions

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface TransactionDAO {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addTransaction(transactionEntity: TransactionEntity)

    @Query("select * from transaction_table")
    fun getTransactionList(): LiveData<List<TransactionEntity>>

    @Update
    suspend fun updateTransaction(transactionEntity: TransactionEntity)

    @Delete
    fun deleteTransaction(transactionEntity: TransactionEntity)

    @Query("SELECT MAX(transaction_id)+1 FROM Transaction_table")
    suspend fun getNextTransactionId(): Long?


}