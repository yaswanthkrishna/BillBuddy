package com.example.billbuddy.vinay.database.groups

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.billbuddy.Group
import com.example.billbuddy.GroupDetail

@Dao
interface GroupDAO {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addGroup(groupEntity: GroupEntity)

    @Query("select * from group_table")
    fun getGroupList(): LiveData<List<GroupEntity>>

    @Update
    suspend fun updateGroup(groupEntity: GroupEntity)

    @Delete
    fun deleteGroup(groupEntity: GroupEntity)

    @Transaction
    @Query("""
        SELECT g.groupId as id, g.group_name as name, 
               SUM(gm.User_owe) as totalOwed, 
               SUM(gm.Group_owes) as totalOwes
        FROM GroupList_table g
        INNER JOIN Group_member_table gm ON g.groupId = gm.Group_id
        WHERE gm.User_id = :userId
        GROUP BY g.groupId
    """)
    suspend fun getGroupDetailsByUserId(userId: Long): List<GroupDetail>
}