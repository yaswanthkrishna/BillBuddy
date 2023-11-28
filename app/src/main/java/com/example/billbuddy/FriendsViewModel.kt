package com.example.billbuddy

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class FriendsViewModel : ViewModel() {
    // LiveData for friend list
    val friendsList = MutableLiveData<List<Friend>>()

    // Function to fetch data from the database and post value to LiveData
    fun fetchFriends() {
        // TODO: Fetch data from the database and update the LiveData
    }

    // Other functionalities related to friends data
}
