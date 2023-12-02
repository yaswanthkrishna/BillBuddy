package com.example.billbuddy.vinay.database.transactions

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface NonGroupTransactionMemberDAO {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addTransactionMember(memberEntity: NonGroupTransactionMemberEntity)

    @Query("SELECT * FROM NonGroup_Transaction_member")
    fun getTransactionMembersList(): LiveData<List<NonGroupTransactionMemberEntity>>

    @Update
    suspend fun updateTransactionMember(memberEntity: NonGroupTransactionMemberEntity)

    @Delete
    suspend fun deleteTransactionMember(memberEntity: NonGroupTransactionMemberEntity)
}
