package com.example.billbuddy.vinay.database.groups

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

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

}
