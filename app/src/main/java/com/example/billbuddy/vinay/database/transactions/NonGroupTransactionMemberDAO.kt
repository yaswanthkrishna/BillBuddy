package com.example.billbuddy.vinay.database.transactions

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.billbuddy.yaswanth.MemberDetail

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

    @Query("""
        SELECT ng.userId, u.name, ng.Amount_owe as amountOwe
        FROM NonGroup_Transaction_member ng
        INNER JOIN User_table u ON ng.userId = u.user_id
        WHERE ng.transactionId = :transactionId AND ng.userId != :paidByUserId AND ng.Amount_owe > 0
    """)
    suspend fun getNonGroupTransactionMembersWithAmounts(transactionId: Long, paidByUserId: Long): List<MemberDetail>

}
