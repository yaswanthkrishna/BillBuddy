package com.example.billbuddy.vinay.repositories

import androidx.lifecycle.LiveData
import com.example.billbuddy.vinay.database.friend_non_group.FriendDAO
import com.example.billbuddy.vinay.database.friend_non_group.FriendEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class FriendRepository(private val dao: FriendDAO) {

    suspend fun addFriend(friendEntity: FriendEntity) {
        withContext(Dispatchers.IO) {
            dao.addFriend(friendEntity)
        }
    }

    fun getFriendsList(userId: Long): LiveData<List<FriendEntity>> {
        return dao.getFriendsList(userId)
    }

    suspend fun updateFriend(friendEntity: FriendEntity) {
        withContext(Dispatchers.IO) {
            dao.updateFriend(friendEntity)
        }
    }

    suspend fun deleteFriend(friendEntity: FriendEntity) {
        withContext(Dispatchers.IO) {
            dao.deleteFriend(friendEntity)
        }
    }

    suspend fun updateOweAmount(userId: Long, friendUserId: Long, amount: Double) {
        withContext(Dispatchers.IO) {
            dao.updateOweAmount(userId, friendUserId, amount)
        }
    }

    suspend fun updateOwesAmount(userId: Long, friendUserId: Long, amount: Double) {
        withContext(Dispatchers.IO) {
            dao.updateOwesAmount(userId, friendUserId, amount)
        }
    }

    suspend fun updateTotalDue(userId: Long, friendUserId: Long) {
        withContext(Dispatchers.IO) {
            dao.updateTotalDue(userId, friendUserId)
        }
    }
}