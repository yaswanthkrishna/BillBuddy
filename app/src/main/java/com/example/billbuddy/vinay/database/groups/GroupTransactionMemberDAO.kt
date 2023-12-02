package com.example.billbuddy.vinay.database.groups

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface GroupTransactionMemberDAO {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addGroupTransactionMember(memberEntity: GroupTransactionMemberEntity)

    @Query("SELECT * FROM Group_Transaction_member")
    fun getGroupTransactionMembersList(): LiveData<List<GroupTransactionMemberEntity>>

    @Update
    suspend fun updateGroupTransactionMember(memberEntity: GroupTransactionMemberEntity)

    @Delete
    suspend fun deleteGroupTransactionMember(memberEntity: GroupTransactionMemberEntity)
}
