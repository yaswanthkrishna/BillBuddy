package com.example.billbuddy.vinay.repositories

import androidx.lifecycle.LiveData
import com.example.billbuddy.vinay.database.groups.GroupMemberDAO
import com.example.billbuddy.vinay.database.groups.GroupMemberEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class GroupMemberRepository(private val dao: GroupMemberDAO) {
    suspend fun addGroupMember(groupMemberEntity: GroupMemberEntity) {
        CoroutineScope(Dispatchers.IO).launch {
            dao.addGroupMember(groupMemberEntity)
        }
    }

    fun getGroupMembers(groupId: Long): LiveData<List<GroupMemberEntity>> {
        return dao.getGroupMembers(groupId)
    }

    suspend fun updateGroupMember(groupMemberEntity: GroupMemberEntity) {
        CoroutineScope(Dispatchers.IO).launch {
            dao.updateGroupMember(groupMemberEntity)
        }
    }

    suspend fun deleteGroupMember(groupMemberEntity: GroupMemberEntity) {
        CoroutineScope(Dispatchers.IO).launch {
            dao.deleteGroupMember(groupMemberEntity)
        }
    }
}

