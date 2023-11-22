package com.example.billbuddy_login

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.billbuddy_login.Friend

class FriendsViewModel : ViewModel() {
    // LiveData for friend list
    val friendsList = MutableLiveData<List<Friend>>()

    // Function to fetch data from the database and post value to LiveData
    fun fetchFriends() {
        // TODO: Fetch data from the database and update the LiveData
    }

    // Other functionalities related to friends data
}
