package com.example.billbuddy.vinay.views

import android.app.Application
import com.example.billbuddy.vinay.database.SplitwiseDatabase
import com.example.billbuddy.vinay.repositories.FriendTransactionRepository
import com.example.billbuddy.vinay.repositories.GroupRepository
import com.example.billbuddy.vinay.repositories.GroupTransactionRepository
import com.example.billbuddy.vinay.repositories.UserRepository

class SplitwiseApplication : Application() {

    companion object {
         val PREF_IS_USER_LOGIN = "PREF_LOGIN_BOOLEAN_KEY"
         val PREF_USER_ID = "PREF_LOGIN_USER_ID"
    }

    val userDAO by lazy {
        val roomDatabase = SplitwiseDatabase.getDatabase(this)
        roomDatabase.getMyUserEntries()
    }
    val groupDAO by lazy {
        val roomDatabase = SplitwiseDatabase.getDatabase(this)
        roomDatabase.getMyGroupEntries()
    }
    val friendsDAO by lazy {
        val roomDatabase = SplitwiseDatabase.getDatabase(this)
        roomDatabase.getMyFriendTransactionEntries()
    }
    val transactionDAO by lazy {
        val roomDatabase = SplitwiseDatabase.getDatabase(this)
        roomDatabase.getMyTransactionEntries()
    }

    val userRepository by lazy {
        UserRepository(userDAO)
    }
    val groupRepository by lazy {
        GroupRepository(groupDAO)
    }
    val transactionRepository by lazy {
        GroupTransactionRepository(transactionDAO)
    }
    val friendsRepository by lazy {
        FriendTransactionRepository(friendsDAO)
    }


}