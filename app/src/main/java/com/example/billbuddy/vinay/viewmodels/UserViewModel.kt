package com.example.billbuddy.vinay.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.ViewModelProvider
import com.example.billbuddy.vinay.database.users.UserDAO
import com.example.billbuddy.vinay.database.users.UserEntity
import com.example.billbuddy.vinay.repositories.UserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class UserViewModel(private val repository: UserRepository) : ViewModel() {

    fun addUser(entity: UserEntity) {
        viewModelScope.launch {
            repository.addUser(entity)
        }
    }

    fun getUserList(): LiveData<List<UserEntity>> {
        return repository.getUserList()
    }

    fun updateUser(entity: UserEntity) {
        viewModelScope.launch {
            repository.updateUser(entity)
        }
    }

    fun deleteUser(entity: UserEntity) {
        viewModelScope.launch {
            repository.deleteUser(entity)
        }
    }

    suspend fun getUserIdByName(userName: String): Long? {
        return withContext(Dispatchers.IO) {
            repository.getUserIdByName(userName)
        }
    }
}

class UserViewModelFactory(private val repository: UserRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(UserViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return UserViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
