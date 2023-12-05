// FriendViewModel.kt

package com.example.billbuddy.vinay.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.ViewModelProvider
import com.example.billbuddy.vinay.database.friend_non_group.FriendEntity
import com.example.billbuddy.vinay.repositories.FriendRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class FriendViewModel(private val repository: FriendRepository) : ViewModel() {

    fun addFriend(friendEntity: FriendEntity) {
        viewModelScope.launch {
            repository.addFriend(friendEntity)
        }
    }

    fun getFriendsList(userId: Long): LiveData<List<FriendEntity>> {
        return repository.getFriendsList(userId)
    }

    fun updateFriend(friendEntity: FriendEntity) {
        viewModelScope.launch {
            repository.updateFriend(friendEntity)
        }
    }

    fun deleteFriend(friendEntity: FriendEntity) {
        viewModelScope.launch {
            repository.deleteFriend(friendEntity)
        }
    }

    fun updateOweAmount(userId: Long, friendUserId: Long, amount: Double) {
        viewModelScope.launch {
            repository.updateOweAmount(userId, friendUserId, amount)
        }
    }

    fun updateOwesAmount(userId: Long, friendUserId: Long, amount: Double) {
        viewModelScope.launch {
            repository.updateOwesAmount(userId, friendUserId, amount)
        }
    }

    fun updateTotalDue(userId: Long, friendUserId: Long) {
        viewModelScope.launch {
            repository.updateTotalDue(userId, friendUserId)
        }
    }
}

class FriendViewModelFactory(private val repository: FriendRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(FriendViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return FriendViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
