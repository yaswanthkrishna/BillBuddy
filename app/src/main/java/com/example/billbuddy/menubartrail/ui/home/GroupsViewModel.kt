package com.example.billbuddy.menubartrail.ui.home

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.billbuddy.GroupDetail
import com.example.billbuddy.vinay.database.groups.GroupListDAO
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class GroupsViewModel(private val groupDAO: GroupListDAO, private var userId: Long) : ViewModel() {
    val groupDetailsList = MutableLiveData<List<GroupDetail>>()
    val totalAmount = MutableLiveData<Double>()

    init {
        refreshGroupsList()
    }

    private fun fetchGroups() {
        viewModelScope.launch(Dispatchers.IO) {
            val groupDetails = groupDAO.getGroupDetailsByUserId(userId)
            groupDetailsList.postValue(groupDetails)
            calculateOverallAmount(groupDetails)
        }
    }


    private fun calculateOverallAmount(groupDetails: List<GroupDetail>) {
        val overallAmount = groupDetails.sumOf { it.totalOwes - it.totalOwed }
        totalAmount.postValue(overallAmount)
    }

    fun refreshGroupsList() {
        fetchGroups()
    }
    fun updateUserId(newUserId: Long) {
        this.userId = newUserId
    }
    fun searchGroups(query: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val filteredGroups = groupDetailsList.value?.filter { it.name.contains(query, ignoreCase = true) } ?: emptyList()
            groupDetailsList.postValue(filteredGroups)
        }
    }
}
