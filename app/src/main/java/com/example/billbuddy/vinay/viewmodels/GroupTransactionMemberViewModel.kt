package com.example.billbuddy.vinay.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.billbuddy.vinay.database.groups.GroupTransactionMemberEntity
import com.example.billbuddy.vinay.repositories.GroupTransactionMemberRepository
import kotlinx.coroutines.launch

class GroupTransactionMemberViewModel(private val repository: GroupTransactionMemberRepository) :
    ViewModel() {

    fun addGroupTransactionMember(memberEntity: GroupTransactionMemberEntity) {
        viewModelScope.launch {
            repository.addGroupTransactionMember(memberEntity)
        }
    }

    fun getGroupTransactionMembersList(): LiveData<List<GroupTransactionMemberEntity>> {
        return repository.getGroupTransactionMembersList()
    }

    fun updateGroupTransactionMember(memberEntity: GroupTransactionMemberEntity) {
        viewModelScope.launch {
            repository.updateGroupTransactionMember(memberEntity)
        }
    }

    fun deleteGroupTransactionMember(memberEntity: GroupTransactionMemberEntity) {
        viewModelScope.launch {
            repository.deleteGroupTransactionMember(memberEntity)
        }
    }
}

class GroupTransactionMemberViewModelFactory(private val repository: GroupTransactionMemberRepository) :
    ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(GroupTransactionMemberViewModel::class.java)) {
            return GroupTransactionMemberViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
