package com.example.billbuddy.vinay.database.recent_activity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.example.billbuddy.vinay.database.transactions.TransactionEntity

@Entity(tableName = "recentTransactionActivity_table")
data class RecentActivityTransactionEntity(
    //description of the bill
    @ColumnInfo(name = "description") var descriptionL: String?,
    //GroupTransaction,Non_group_transaction
    @ColumnInfo(name = "type") var type: String?,
    //User name for creator of activity
    @ColumnInfo(name = "creator") var creator: Int,
    //User name for who paid the bill
    @ColumnInfo(name = "userPaid") var userPaid: String?,
    //amount for each member of the transaction including the user paid or creator
    //in format (name,amount)
    @ColumnInfo(name = "percentageAmount") var memberAmount: Map<String, Int>,
    //group name if group transaction
    @ColumnInfo(name = "groupName") var groupName: String?,
    @ColumnInfo(name = "time") var time: Long, //time in milliseconds
    @ColumnInfo(name = "note") var note: String?
) {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var id: Int? = null
}
