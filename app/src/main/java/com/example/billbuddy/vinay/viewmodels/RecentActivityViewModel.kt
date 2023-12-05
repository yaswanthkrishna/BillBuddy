package com.example.billbuddy.vinay.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.billbuddy.vinay.database.recent_activity.RecentActivityEntity
import com.example.billbuddy.vinay.repositories.RecentActivityRepository
import kotlinx.coroutines.launch

class RecentActivityViewModel(private val repository: RecentActivityRepository) :
    ViewModel() {

    fun addRecentActivity(recentActivityEntity: RecentActivityEntity) {
        viewModelScope.launch {
            repository.addRecentActivity(recentActivityEntity)
        }
    }

    fun getRecentActivityList(): LiveData<List<RecentActivityEntity>>{
        return repository.getRecentActivityList()
    }

    fun updateGroupMember(recentActivityEntity: RecentActivityEntity) {
        viewModelScope.launch {
            repository.updateRecentActivityMember(recentActivityEntity)
        }
    }

    fun deleteGroupMember(recentActivityEntity: RecentActivityEntity){
        viewModelScope.launch {
            repository.deleteRecentActivityMember(recentActivityEntity)
        }
    }
}


class RecentActivityViewModelFactory(private val repository: RecentActivityRepository) :
    ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(RecentActivityViewModel::class.java)) {
            return RecentActivityViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}