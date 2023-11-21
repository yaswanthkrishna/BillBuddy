package com.example.billbuddy_login.vinay.database.transactions

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface TransactionDAO {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addTransaction(transactionEntity: TransactionEntity)

    @Query("select * from transaction_table")
    fun getTransactionList(): LiveData<List<TransactionEntity>>

    @Update
    fun updateTransaction(transactionEntity: TransactionEntity)

    @Delete
    fun deleteTransaction(transactionEntity: TransactionEntity)
}