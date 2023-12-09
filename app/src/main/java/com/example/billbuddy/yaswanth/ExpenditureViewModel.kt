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

                _expenditureStats.postValue(
                    ExpenditureStats(
                        totalSpentOnGroups,
                        totalSpentOnFriends,
                        totalOwedByOthers
                    )
                )
            } catch (e: Exception) {
                Log.e("ExpenditureViewModel", "Error calculating expenditure stats: ${e.message}")
            }
        }
    }

    fun updateExpenditureStats() {
        calculateExpenditureStats()
    }

    fun getExpenditureData(startDate: String, endDate: String): LiveData<ExpenditureData> {
        val expenditureData = MutableLiveData<ExpenditureData>()

        viewModelScope.launch(Dispatchers.IO) {
            try {
                // Fetch transactions paid by the user
                val paidByUserTransactions =
                    transactionDAO.getTransactionsForUserInRangeSuspend(userId, startDate, endDate)

                // Fetch transactions where the user is owed money (or any other relevant transactions)
                val otherUserTransactions =
                    transactionDAO.getOtherUserTransactionsInRange(userId, startDate, endDate)
                Log.d("ExpenditureViewModel", "Value of others is:$otherUserTransactions")

                // Combine both lists
                val allTransactions = paidByUserTransactions + otherUserTransactions

                // Group transactions into categories
                val categories = allTransactions.groupBy { transaction ->
                    when {
                        transaction.totalAmount >= 0 && transaction.groupFlag -> "Income by Groups"
                        transaction.totalAmount >= 0 && !transaction.groupFlag -> "Income by Friends"
                        transaction.totalAmount < 0 && transaction.groupFlag -> "Expenditure by Groups"
                        else -> "Expenditure by Friends"
                    }
                }

                // Sum up the amounts for each category
                val dataValues = categories.map { (category, transactions) ->
                    transactions.sumOf { transaction ->
                        if (category.startsWith("Expenditure")) {
                            -transaction.totalAmount.toDouble() // Negate the amount for expenditures
                        } else {
                            transaction.totalAmount.toDouble()
                        }
                    }
                }
                val dataLabels = categories.keys.toList()

                expenditureData.postValue(
                    ExpenditureData(
                        dataValues,
                        dataLabels
                    )
                )
            } catch (e: Exception) {
                Log.e("ExpenditureViewModel", "Error fetching transactions: ${e.message}")
            }
        }

        return expenditureData
    }

}

// Data classes for the view model
data class ExpenditureData(val values: List<Double>, val labels: List<String>)
data class ExpenditureStats(
    val totalSpentOnGroups: Double,
    val totalSpentOnFriends: Double,
    val totalOwedByOthers: Double
)