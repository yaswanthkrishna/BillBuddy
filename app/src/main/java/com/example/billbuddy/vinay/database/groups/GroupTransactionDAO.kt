// GroupTransactionDAO.kt

package com.example.billbuddy.vinay.database.groups

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface GroupTransactionDAO {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addGroupTransaction(groupTransactionEntity: GroupTransactionEntity)

    @Query("SELECT * FROM Group_Transaction_table where Group_id=:groupId")
    fun getGroupTransactions(groupId: Long): LiveData<List<GroupTransactionEntity>>

    @Update
    suspend fun updateGroupTransaction(groupTransactionEntity: GroupTransactionEntity)

    @Delete
    suspend fun deleteGroupTransaction(groupTransactionEntity: GroupTransactionEntity)

}

