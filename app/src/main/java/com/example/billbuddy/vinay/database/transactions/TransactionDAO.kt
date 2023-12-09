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

    @Query("SELECT * FROM Transaction_table WHERE Paid_by_userid = :friendId")
    fun getTransactionsForFriend(friendId: Long): LiveData<List<TransactionEntity>>

    @Query("SELECT * FROM Transaction_table WHERE Group_flag = 1") // Assuming groupFlag = 1 for group transactions
    fun getGroupTransactions(): LiveData<List<TransactionEntity>>

    @Query("SELECT * FROM Transaction_table WHERE Group_flag = 0") // Assuming groupFlag = 0 for friend transactions
    fun getFriendTransactions(): LiveData<List<TransactionEntity>>


    @Transaction
    @Query("""
        SELECT u.name 
        FROM User_table u 
        JOIN NonGroup_Transaction_member ng ON u.user_id = ng.userId 
        WHERE ng.transactionId = :transactionId AND u.user_id != :paidByUserId
    """)
    suspend fun getNonGroupTransactionMembers(transactionId: Long, paidByUserId: Long): List<String>
    @Query("SELECT SUM(Total_amount) FROM Transaction_table WHERE Paid_by_userid = :userId AND Group_flag = 1")
    suspend fun getTotalSpentOnGroups(userId: Long): Double?

    @Query("SELECT SUM(Total_amount) FROM Transaction_table WHERE Paid_by_userid = :userId AND Group_flag = 0")
    suspend fun getTotalSpentOnFriends(userId: Long): Double?
}