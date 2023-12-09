package com.example.billbuddy.yaswanth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.billbuddy.vinay.database.friend_non_group.FriendDAO
import com.example.billbuddy.vinay.database.groups.GroupMemberDAO
import com.example.billbuddy.vinay.database.transactions.TransactionDAO

class ExpenditureViewModelFactory(
    private val transactionDAO: TransactionDAO,
    private val friendDAO: FriendDAO,
    private val groupMemberDAO: GroupMemberDAO,
    private val userId: Long
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(ExpenditureViewModel::class.java) ->
                ExpenditureViewModel(transactionDAO, friendDAO, groupMemberDAO, userId) as T
            else -> throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
        }
    }
}
