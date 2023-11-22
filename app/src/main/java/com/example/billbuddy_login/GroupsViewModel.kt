package com.example.billbuddy_login


import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.billbuddy_login.Group

class GroupsViewModel : ViewModel() {
    // LiveData for friend list
    val groupsList = MutableLiveData<List<Group>>()

    // Function to fetch data from the database and post value to LiveData
    fun fetchGroups() {
        // TODO: Fetch data from the database and update the LiveData
    }

    // Other functionalities related to groups data
}
