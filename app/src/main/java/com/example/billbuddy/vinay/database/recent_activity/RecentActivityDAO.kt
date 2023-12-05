package com.example.billbuddy.vinay.database.recent_activity

import androidx.lifecycle.LiveData
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

interface RecentActivityDAO {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addRecentActivity(recentActivityEntity: RecentActivityEntity)

    @Query("SELECT * FROM recentActivity_table")
    fun getRecentActivityList(): LiveData<List<RecentActivityEntity>>

    @Update
    suspend fun updateRecentActivityMember(recentActivityEntity: RecentActivityEntity)

    @Delete
    suspend fun deleteRecentActivityMember(recentActivityEntity: RecentActivityEntity)
}