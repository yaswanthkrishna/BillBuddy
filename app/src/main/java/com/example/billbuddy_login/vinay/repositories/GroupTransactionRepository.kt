package com.example.billbuddy_login.vinay.repositories

import androidx.lifecycle.LiveData

import com.example.billbuddy_login.vinay.database.transactions.TransactionDAO
import com.example.billbuddy_login.vinay.database.transactions.TransactionEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class GroupTransactionRepository(val DAO: TransactionDAO) {

    fun addTransaction(entity: TransactionEntity) {
        CoroutineScope(Dispatchers.IO).launch {
            DAO.addTransaction(entity)
        }
    }

    fun getTransactionsList(): LiveData<List<TransactionEntity>> {
        return DAO.getTransactionList()
    }

    fun updateTransaction(entity: TransactionEntity) {
        DAO.updateTransaction(entity)
    }

    fun deleteTransaction(entity: TransactionEntity) {
        DAO.deleteTransaction(entity)
    }
}