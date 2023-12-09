package com.example.billbuddy.yaswanth

import android.util.Log
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.billbuddy.vinay.database.friend_non_group.FriendDAO
import com.example.billbuddy.vinay.database.groups.GroupMemberDAO
import com.example.billbuddy.vinay.database.sharedpreferences.PreferenceHelper
import com.example.billbuddy.vinay.database.transactions.TransactionDAO
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
class ExpenditureViewModel(
    private val transactionDAO: TransactionDAO,
    private val friendDAO: FriendDAO,
    private val groupMemberDAO: GroupMemberDAO,
    private val userId: Long
) : ViewModel() {

    private val _expenditureStats = MutableLiveData<ExpenditureStats>()
    val expenditureStats: LiveData<ExpenditureStats> = _expenditureStats

    init {
        calculateExpenditureStats()
    }

    private fun calculateExpenditureStats() {
        if (userId == null) {
            Log.e("ExpenditureViewModel", "userId is null")
            return
        }

        viewModelScope.launch(Dispatchers.IO) {
            try {
                val totalSpentOnGroups = transactionDAO.getTotalSpentOnGroups(userId) ?: 0.0
                val totalSpentOnFriends = transactionDAO.getTotalSpentOnFriends(userId) ?: 0.0
                val totalOwedByOthers = friendDAO.getTotalOwedByOthers(userId) ?: 0.0

                _expenditureStats.postValue(ExpenditureStats(totalSpentOnGroups, totalSpentOnFriends, totalOwedByOthers))
            } catch (e: Exception) {
                Log.e("ExpenditureViewModel", "Error calculating expenditure stats: ${e.message}")
            }
        }
    }

    fun updateExpenditureStats() {
        calculateExpenditureStats()
    }
}

data class ExpenditureStats(
    val totalSpentOnGroups: Double,
    val totalSpentOnFriends: Double,
    val totalOwedByOthers: Double
)
