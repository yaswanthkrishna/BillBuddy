package com.example.billbuddy.vinay.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.billbuddy.vinay.database.users.UserEntity
import com.example.billbuddy.vinay.repositories.UserRepository

class UserViewModel(val repository: UserRepository) : ViewModel() {
    fun addUser(entity: UserEntity) {
        repository.addUser(entity)
    }

    fun getUserList(): LiveData<List<UserEntity>> {
        return repository.getUserList()
    }

    fun updateUser(entity: UserEntity) {
        repository.updateUser(entity)
    }

    fun deleteUser(entity: UserEntity) {
        repository.deleteUser(entity)
    }
}

class UserViewModelFactory(val repository: UserRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return UserViewModel(repository) as T
    }
}