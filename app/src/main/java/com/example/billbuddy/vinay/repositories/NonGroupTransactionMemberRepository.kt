package com.example.billbuddy.vinay.repositories

import androidx.lifecycle.LiveData
import com.example.billbuddy.vinay.database.transactions.NonGroupTransactionMemberDAO
import com.example.billbuddy.vinay.database.transactions.NonGroupTransactionMemberEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class NonGroupTransactionMemberRepository(private val dao: NonGroupTransactionMemberDAO) {

    suspend fun addTransactionMember(memberEntity: NonGroupTransactionMemberEntity) {
        withContext(Dispatchers.IO) {
            dao.addTransactionMember(memberEntity)
        }
    }

    fun getTransactionMembersList(): LiveData<List<NonGroupTransactionMemberEntity>> {
        return dao.getTransactionMembersList()
    }

    suspend fun updateTransactionMember(memberEntity: NonGroupTransactionMemberEntity) {
        withContext(Dispatchers.IO) {
            dao.updateTransactionMember(memberEntity)
        }
    }

    suspend fun deleteTransactionMember(memberEntity: NonGroupTransactionMemberEntity) {
        withContext(Dispatchers.IO) {
            dao.deleteTransactionMember(memberEntity)
        }
    }
}