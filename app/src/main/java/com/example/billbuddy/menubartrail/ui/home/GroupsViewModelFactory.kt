package com.example.billbuddy.menubartrail.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.billbuddy.vinay.database.groups.GroupListDAO

class GroupsViewModelFactory(private val groupDAO: GroupListDAO, private val userId: Long) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(GroupsViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return GroupsViewModel(groupDAO, userId) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
