package com.example.billbuddy.menubartrail.ui.home

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.map
import androidx.lifecycle.viewModelScope
import com.example.billbuddy.menubartrail.ui.home.Friend
import com.example.billbuddy.vinay.database.friend_non_group.FriendDAO
import com.example.billbuddy.vinay.database.users.UserDAO
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class FriendsViewModel(private val friendDAO: FriendDAO, private var userId: Long) : ViewModel() {
    val friendsList = MutableLiveData<List<Friend>>()

    init {
        fetchFriends()
    }
    private fun fetchFriends() {
        friendDAO.getFriendsList(userId).observeForever { friendEntities ->
            val friends = friendEntities.map { entity ->
                Friend(entity.id, entity.userId, entity.friendUserId, entity.owe, entity.owes, entity.totalDue, entity.name)
            }
            friendsList.postValue(friends)
        }
    }
    fun updateUserId(newUserId: Long) {
        this.userId = newUserId
    }
    fun refreshFriendsList() {
        fetchFriends()
    }
    fun filterFriendsList(filter: String) {
        viewModelScope.launch(Dispatchers.IO) {
            friendDAO.getFriendsList(userId).observeForever { friendEntities ->
                val filterFriends = friendEntities.map { entity ->
                    Friend(
                        entity.id,
                        entity.userId,
                        entity.friendUserId,
                        entity.owe,
                        entity.owes,
                        entity.totalDue,
                        entity.name
                    )
                }
                val filteredFriends = when (filter) {
                    "credit" -> filterFriends.filter { it.owes > 0 }
                    "debit" -> filterFriends.filter { it.owe > 0 }
                    else -> filterFriends
                }
                friendsList.postValue(filteredFriends)
            }
        }
    }
    fun sortFriendsList(byAmountOwed: Boolean) {
        val sortedList = if (byAmountOwed) {
            friendsList.value?.sortedByDescending { it.owes - it.owe }
        } else {
            friendsList.value?.sortedBy { it.name }
        }
        friendsList.postValue(sortedList ?: emptyList())
    }
    fun calculateOverallAmount(): Double {
        return friendsList.value?.sumByDouble { it.owes - it.owe } ?: 0.0
    }
    fun searchFriends(query: String) {
        viewModelScope.launch(Dispatchers.IO) {
            friendDAO.getFriendsList(userId).observeForever { friendEntities ->
                val allFriends = friendEntities.map { entity ->
                    Friend(
                        entity.id,
                        entity.userId,
                        entity.friendUserId,
                        entity.owe,
                        entity.owes,
                        entity.totalDue,
                        entity.name
                    )
                }
                val filteredFriends = if (query.isNotEmpty()) {
                    allFriends.filter { it.name.contains(query, ignoreCase = true) }
                } else {
                    allFriends
                }
                friendsList.postValue(filteredFriends)
            }
        }
    }
}