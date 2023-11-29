package com.example.billbuddy_login.vinay.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
<<<<<<< Updated upstream:app/src/main/java/com/example/billbuddy_login/vinay/viewmodels/FriendTransactionViewModel.kt
import com.example.billbuddy_login.vinay.database.friend_non_group.FriendTransactionEntity
import com.example.billbuddy_login.vinay.repositories.FriendTransactionRepository
=======
import com.example.billbuddy.vinay.database.friend_non_group.FriendTransactionEntity
import com.example.billbuddy.vinay.repositories.FriendTransactionRepository
>>>>>>> Stashed changes:app/src/main/java/com/example/billbuddy/vinay/viewmodels/FriendTransactionViewModel.kt

class FriendTransactionViewModel(val repository: FriendTransactionRepository) : ViewModel() {
    fun addFriendTransaction(entity: FriendTransactionEntity) {
        repository.addFriendTransaction(entity)
    }

    fun getFriendTransactionsList(): LiveData<List<FriendTransactionEntity>> {
        return repository.getFriendTransactionsList()
    }

    fun updateFriendTransaction(entity: FriendTransactionEntity) {
        repository.updateFriendTransaction(entity)
    }

    fun deleteFriendTransaction(entity: FriendTransactionEntity) {
        repository.deleteFriendTransaction(entity)
    }
}

class FriendTransactionViewModelFactory(val repository: FriendTransactionRepository) :
    ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(FriendTransactionViewModel::class.java)) {
            return FriendTransactionViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
    }
}
