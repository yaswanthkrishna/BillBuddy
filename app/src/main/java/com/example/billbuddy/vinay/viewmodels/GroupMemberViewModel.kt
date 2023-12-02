package com.example.billbuddy.vinay.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.ViewModelProvider
import com.example.billbuddy.vinay.database.groups.GroupMemberDAO
import com.example.billbuddy.vinay.database.groups.GroupMemberEntity
import com.example.billbuddy.vinay.repositories.GroupMemberRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class GroupMemberViewModel(private val repository: GroupMemberRepository) : ViewModel() {

    suspend fun addGroupMember(groupMemberEntity: GroupMemberEntity) {
        withContext(Dispatchers.IO) {
            repository.addGroupMember(groupMemberEntity)
        }
    }

    fun getGroupMembers(groupId: Long): LiveData<List<GroupMemberEntity>> {
        return repository.getGroupMembers(groupId)
    }

    suspend fun updateGroupMember(groupMemberEntity: GroupMemberEntity) {
        withContext(Dispatchers.IO) {
            repository.updateGroupMember(groupMemberEntity)
        }
    }

    suspend fun deleteGroupMember(groupMemberEntity: GroupMemberEntity) {
        withContext(Dispatchers.IO) {
            repository.deleteGroupMember(groupMemberEntity)
        }
    }

}

class GroupMemberViewModelFactory(private val repository: GroupMemberRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(GroupMemberViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return GroupMemberViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
