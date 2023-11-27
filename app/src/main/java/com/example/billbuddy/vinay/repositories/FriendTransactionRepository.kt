package com.example.billbuddy.vinay.repositories

import androidx.lifecycle.LiveData
import com.example.billbuddy.vinay.database.friend_non_group.FriendTransactionDAO
import com.example.billbuddy.vinay.database.friend_non_group.FriendTransactionEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class FriendTransactionRepository(val DAO: FriendTransactionDAO) {

    fun addFriendTransaction(entity: FriendTransactionEntity) {
        CoroutineScope(Dispatchers.IO).launch {
            DAO.addFriendTransaction(entity)
        }
    }

    fun getFriendTransactionsList(): LiveData<List<FriendTransactionEntity>> {
        return DAO.getFriendTransactionList()
    }

    fun updateFriendTransaction(entity: FriendTransactionEntity) {
       CoroutineScope(Dispatchers.IO).launch {
        DAO.updateFriendTransaction(entity)
       }
    }

    fun deleteFriendTransaction(entity: FriendTransactionEntity) {
        DAO.deleteFriendTransaction(entity)
    }
}