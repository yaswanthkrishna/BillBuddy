package com.example.billbuddy.vinay.repositories

import androidx.lifecycle.LiveData
import com.example.billbuddy.vinay.database.groups.GroupListDAO
import com.example.billbuddy.vinay.database.groups.GroupListEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class GroupListRepository(private val dao: GroupListDAO) {

    fun addGroup(groupEntity: GroupListEntity) {
        CoroutineScope(Dispatchers.IO).launch {
            dao.addGroup(groupEntity)
        }
    }

    fun getGroupList(): LiveData<List<GroupListEntity>> {
        return dao.getGroupList()
    }

    suspend fun getGroupById(groupId: Long): GroupListEntity? {
        return dao.getGroupById(groupId)
    }

    suspend fun deleteGroup(groupId: Long) {
        CoroutineScope(Dispatchers.IO).launch {
            dao.deleteGroup(groupId)
        }
    }

    suspend fun getGroupIdByName(groupName: String): Long? {
        return dao.getGroupIdByName(groupName)
    }

}
