package com.example.billbuddy_login.vinay.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
<<<<<<< Updated upstream:app/src/main/java/com/example/billbuddy_login/vinay/viewmodels/GroupViewModel.kt
import com.example.billbuddy_login.vinay.database.groups.GroupEntity
import com.example.billbuddy_login.vinay.database.transactions.TransactionEntity
import com.example.billbuddy_login.vinay.repositories.GroupRepository
import com.example.billbuddy_login.vinay.repositories.UserRepository
=======
import com.example.billbuddy.vinay.database.groups.GroupEntity
import com.example.billbuddy.vinay.repositories.GroupRepository
>>>>>>> Stashed changes:app/src/main/java/com/example/billbuddy/vinay/viewmodels/GroupViewModel.kt

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

class GroupViewModelFactory(val repository: GroupRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return GroupViewModel(repository) as T
    }
}