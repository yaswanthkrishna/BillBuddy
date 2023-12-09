package com.example.billbuddy.vinay.database.groups

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.example.billbuddy.GroupDetail

@Dao
interface GroupListDAO {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addGroup(groupEntity: GroupListEntity)

    @Query("SELECT * FROM GroupList_table")
    fun getGroupList(): LiveData<List<GroupListEntity>>

    @Query("SELECT * FROM GroupList_table WHERE groupId = :groupId")
    suspend fun getGroupById(groupId: Long): GroupListEntity?

    @Query("DELETE FROM GroupList_table WHERE groupId = :groupId")
    suspend fun deleteGroup(groupId: Long)
    @Query("SELECT groupId FROM grouplist_table WHERE group_name = :groupName")
    suspend fun getGroupIdByName(groupName: String):Long?

    @Transaction
    @Query("""
    SELECT g.groupId as id, g.group_name as name, 
           COALESCE(SUM(gm.User_owe), 0) as totalOwed, 
           COALESCE(SUM(gm.Group_owes), 0) as totalOwes
    FROM GroupList_table g
    LEFT JOIN Group_member_table gm ON g.groupId = gm.Group_id AND gm.User_id = :userId
    GROUP BY g.groupId
""")
    suspend fun getGroupDetailsByUserId(userId: Long): List<GroupDetail>

}
