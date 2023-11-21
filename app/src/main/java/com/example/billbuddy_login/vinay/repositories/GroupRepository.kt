package com.example.billbuddy_login.vinay.repositories

import androidx.lifecycle.LiveData
import com.example.billbuddy_login.vinay.database.groups.GroupDAO
import com.example.billbuddy_login.vinay.database.groups.GroupEntity
import com.example.billbuddy_login.vinay.database.users.UserDAO
import com.example.billbuddy_login.vinay.database.users.UserEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class GroupRepository(val DAO: GroupDAO) {

    fun addGroup(entity: GroupEntity) {
        CoroutineScope(Dispatchers.IO).launch {
            DAO.addGroup(entity)
        }
    }

    fun getGroupList(): LiveData<List<GroupEntity>> {
        return DAO.getGroupList()
    }

    fun updateGroup(entity: GroupEntity) {
        CoroutineScope(Dispatchers.IO).launch {
            DAO.updateGroup(entity)
        }
    }

    fun deleteGroup(entity: GroupEntity) {
        DAO.deleteGroup(entity)
    }
}