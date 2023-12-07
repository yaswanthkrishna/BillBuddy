package com.example.billbuddy.vinay.database.recent_activity

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
@Dao
interface RecentActivityTransactionDAO {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addRecentActivityTransaction(transactionEntity: RecentActivityTransactionEntity)

    @Query("SELECT * FROM recentTransactionActivity_table")
    fun getRecentActivityTransactionList(): LiveData<List<RecentActivityTransactionEntity>>

    @Update
    suspend fun updateRecentActivityTransactionMember(transactionEntity: RecentActivityTransactionEntity)

    @Delete
    suspend fun deleteRecentActivityTransactionMember(transactionEntity: RecentActivityTransactionEntity)
}