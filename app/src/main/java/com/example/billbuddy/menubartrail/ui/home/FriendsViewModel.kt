package com.example.billbuddy.menubartrail.ui.home

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.billbuddy.vinay.database.friend_non_group.FriendDAO

class FriendsViewModel(private val friendDAO: FriendDAO, private var userId: Long) : ViewModel() {
    val friendsList = MutableLiveData<List<Friend>>()
    init {
        refreshFriendsList()
    }
    private fun fetchFriends() {
        friendDAO.getFriendsList(userId).observeForever { friendEntities ->
            val friends = friendEntities.map { entity ->
                Friend(entity.id, entity.userId, entity.friendUserId, entity.owe, entity.owes, entity.totalDue, entity.name)
            }
            friendsList.postValue(friends)
        }
        calculateOverallAmount()
    }
    fun updateUserId(newUserId: Long) {
        this.userId = newUserId
    }
    fun refreshFriendsList() {
        fetchFriends()
    }
    fun calculateOverallAmount(): Double {
        return friendsList.value?.sumByDouble { it.owes - it.owe } ?: 0.0
    }
}