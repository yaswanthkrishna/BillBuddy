package com.example.billbuddy.vinay.repositories

import androidx.lifecycle.LiveData
import com.example.billbuddy.vinay.database.users.UserDAO
import com.example.billbuddy.vinay.database.users.UserEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class UserRepository(val DAO: UserDAO) {

    fun addUser(entity: UserEntity) {
        CoroutineScope(Dispatchers.IO).launch {
            DAO.addUser(entity)
        }
    }

    fun getUserList(): LiveData<List<UserEntity>> {
        return DAO.getUserList()
    }

    fun updateUser(entity: UserEntity) {
        CoroutineScope(Dispatchers.IO).launch {
            DAO.updateUser(entity)
        }
    }

    fun deleteUser(entity: UserEntity) {
        DAO.deleteUser(entity)
    }

    fun getUserIdByName(userName: String): Long? {
        return DAO.getUserIdByName(userName)
    }

    suspend fun getNameAndPhoneByUserId(userId: Long): UserDAO.NameAndPhone? {
        return DAO.getNameAndPhoneByUserId(userId)
    }

    fun updateOweById(userId: Long, newOwe: Double) {
        CoroutineScope(Dispatchers.IO).launch {
            DAO.updateOweById(userId, newOwe)
        }
    }
}