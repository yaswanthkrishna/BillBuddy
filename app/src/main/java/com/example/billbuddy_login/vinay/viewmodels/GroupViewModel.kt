package com.example.billbuddy_login.vinay.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.billbuddy_login.vinay.database.groups.GroupEntity
import com.example.billbuddy_login.vinay.database.transactions.TransactionEntity
import com.example.billbuddy_login.vinay.repositories.GroupRepository
import com.example.billbuddy_login.vinay.repositories.UserRepository

class GroupViewModel(val repository: GroupRepository) : ViewModel() {
    fun addGroup(entity: GroupEntity) {
        repository.addGroup(entity)
    }

    fun getGroupList(): LiveData<List<GroupEntity>> {
        return repository.getGroupList()
    }

    fun updateGroup(entity: GroupEntity) {
        repository.updateGroup(entity)
    }

    fun deleteGroup(entity: GroupEntity) {
        repository.deleteGroup(entity)
    }
}

/*class GroupViewModelFactory(val repository: GroupRepository) : ViewModelProvider.Factory {
    fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return GroupViewModel(repository) as T
    }
}*/