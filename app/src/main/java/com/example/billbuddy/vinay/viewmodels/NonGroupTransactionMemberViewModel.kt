package com.example.billbuddy.vinay.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.billbuddy.vinay.database.transactions.NonGroupTransactionMemberEntity
import com.example.billbuddy.vinay.repositories.NonGroupTransactionMemberRepository
import kotlinx.coroutines.launch

class NonGroupTransactionMemberViewModel(private val repository: NonGroupTransactionMemberRepository) :
    ViewModel() {

    fun addTransactionMember(memberEntity: NonGroupTransactionMemberEntity) {
        viewModelScope.launch {
            repository.addTransactionMember(memberEntity)
        }
    }

    fun getTransactionMembersList(): LiveData<List<NonGroupTransactionMemberEntity>> {
        return repository.getTransactionMembersList()
    }

    fun updateTransactionMember(memberEntity: NonGroupTransactionMemberEntity) {
        viewModelScope.launch {
            repository.updateTransactionMember(memberEntity)
        }
    }

    fun deleteTransactionMember(memberEntity: NonGroupTransactionMemberEntity) {
        viewModelScope.launch {
            repository.deleteTransactionMember(memberEntity)
        }
    }
}


class NonGroupTransactionMemberViewModelFactory(private val repository: NonGroupTransactionMemberRepository) :
    ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(NonGroupTransactionMemberViewModel::class.java)) {
            return NonGroupTransactionMemberViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}