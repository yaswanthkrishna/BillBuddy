package com.example.billbuddy.vinay.database.groups

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface GroupMemberDAO {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addGroupMember(groupMemberEntity: GroupMemberEntity)

    @Query("SELECT * FROM Group_member_table where Group_id=:groupId")
    fun getGroupMembers(groupId: Long): LiveData<List<GroupMemberEntity>>

    @Update
    suspend fun updateGroupMember(groupMemberEntity: GroupMemberEntity)

    @Delete
    suspend fun deleteGroupMember(groupMemberEntity: GroupMemberEntity)

}

