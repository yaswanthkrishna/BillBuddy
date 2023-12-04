package com.example.billbuddy

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.billbuddy.vinay.database.friend_non_group.FriendDAO

class FriendsViewModelFactory(private val friendDAO: FriendDAO, private val userId: Long) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(FriendsViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return FriendsViewModel(friendDAO, userId) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}