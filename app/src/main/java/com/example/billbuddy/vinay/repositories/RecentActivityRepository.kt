package com.example.billbuddy.vinay.repositories

import androidx.lifecycle.LiveData
import com.example.billbuddy.vinay.database.recent_activity.RecentActivityDAO
import com.example.billbuddy.vinay.database.recent_activity.RecentActivityEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class RecentActivityRepository(private val dao: RecentActivityDAO) {

    suspend fun addRecentActivity(recentActivityEntity: RecentActivityEntity) {
        withContext(Dispatchers.IO) {
            dao.addRecentActivity(recentActivityEntity)
        }
    }

    fun getRecentActivityList(): LiveData<List<RecentActivityEntity>>{
        return dao.getRecentActivityList()
    }

    suspend fun updateRecentActivityMember(recentActivityEntity: RecentActivityEntity) {
        withContext(Dispatchers.IO) {
            dao.updateRecentActivityMember(recentActivityEntity)
        }
    }

    suspend fun deleteRecentActivityMember(recentActivityEntity: RecentActivityEntity) {
        withContext(Dispatchers.IO) {
            dao.deleteRecentActivityMember(recentActivityEntity)
        }
    }
}