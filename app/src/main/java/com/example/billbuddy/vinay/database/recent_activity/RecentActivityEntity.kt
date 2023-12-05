package com.example.billbuddy.vinay.database.recent_activity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
@Entity(tableName = "recentActivity_table")
data class RecentActivityEntity(
    @ColumnInfo(name = "type") var type: String?, //GroupTransaction,Non_group_transaction,Comment, Delete group, create Group,etc
    @ColumnInfo(name = "creator") var creator: Int,
    @ColumnInfo(name = "amount") var amount: Int,
    @ColumnInfo(name = "groupName") var groupName: String?,
    @ColumnInfo(name = "otherUser") var username: String?,
    @ColumnInfo(name = "time") var time: Long, //time in milliseconds
    @ColumnInfo(name = "note") var note: String?,
    @ColumnInfo(name = "amount_owe") var youOwe: Int,
    @ColumnInfo(name = "amount_owes") var otherOwes: Int,
    @ColumnInfo(name = "transactionId") var transactionId: Int
) {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var id: Int? = null
}
