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

    @Query("UPDATE Group_member_table SET User_owe = User_owe + :newUserOwe WHERE Group_id = :groupId AND User_id = :userId")
    suspend fun updateUserOwe(groupId: Long, userId: Long, newUserOwe: Double)

    @Query("UPDATE Group_member_table SET Group_owes = Group_owes +:newGroupOwes WHERE Group_id = :groupId AND User_id = :userId")
    suspend fun updateGroupOwes(groupId: Long, userId: Long, newGroupOwes: Double)

    @Query("UPDATE Group_member_table SET Total_due = (Group_owes - User_owe) WHERE Group_id = :groupId AND User_id = :userId")
    suspend fun updateTotalDue(groupId: Long, userId: Long)
}

