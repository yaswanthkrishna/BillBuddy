package com.example.billbuddy.vinay.repositories

import androidx.lifecycle.LiveData
import com.example.billbuddy.vinay.database.groups.GroupTransactionMemberDAO
import com.example.billbuddy.vinay.database.groups.GroupTransactionMemberEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class GroupTransactionMemberRepository(private val dao: GroupTransactionMemberDAO) {

    suspend fun addGroupTransactionMember(memberEntity: GroupTransactionMemberEntity) {
        withContext(Dispatchers.IO) {
            dao.addGroupTransactionMember(memberEntity)
        }
    }

    fun getGroupTransactionMembersList(): LiveData<List<GroupTransactionMemberEntity>> {
        return dao.getGroupTransactionMembersList()
    }

    suspend fun updateGroupTransactionMember(memberEntity: GroupTransactionMemberEntity) {
        withContext(Dispatchers.IO) {
            dao.updateGroupTransactionMember(memberEntity)
        }
    }

    suspend fun deleteGroupTransactionMember(memberEntity: GroupTransactionMemberEntity) {
        withContext(Dispatchers.IO) {
            dao.deleteGroupTransactionMember(memberEntity)
        }
    }
}
