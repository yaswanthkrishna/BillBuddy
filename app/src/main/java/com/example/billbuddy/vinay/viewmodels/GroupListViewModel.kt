package com.example.billbuddy.vinay.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.ViewModelProvider
import com.example.billbuddy.vinay.database.groups.GroupListDAO
import com.example.billbuddy.vinay.database.groups.GroupListEntity
import com.example.billbuddy.vinay.repositories.GroupListRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class GroupListViewModel(private val repository: GroupListRepository) : ViewModel() {

    fun addGroup(groupEntity: GroupListEntity) {
        viewModelScope.launch {
            repository.addGroup(groupEntity)
        }
    }

    fun getGroupList(): LiveData<List<GroupListEntity>> {
        return repository.getGroupList()
    }

    suspend fun getGroupById(groupId: Long): GroupListEntity? {
        return withContext(Dispatchers.IO) {
            repository.getGroupById(groupId)
        }
    }

    fun deleteGroup(groupId: Long) {
        viewModelScope.launch {
            repository.deleteGroup(groupId)
        }
    }
    suspend fun getUserIdByName(groupName: String): Long? {
        return withContext(Dispatchers.IO) {
            repository.getGroupIdByName(groupName)
        }
    }

}

class GroupListViewModelFactory(private val repository: GroupListRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(GroupListViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return GroupListViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
