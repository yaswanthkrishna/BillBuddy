package com.example.billbuddy.vinay.database.recent_activity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import com.example.billbuddy.vinay.database.transactions.TransactionEntity
import kotlin.math.log

@Entity(tableName = "recentTransactionActivity_table")
data class RecentActivityTransactionEntity(
    //description of the bill
    @ColumnInfo(name = "description") var description: String?,
    //GroupTransaction,Non_group_transaction
    @ColumnInfo(name = "type") var type: String?,
    //User id for creator of activity
    @ColumnInfo(name = "creator") var creator: Int,
    //User name for who paid the bill
    @ColumnInfo(name = "userPaid") var userPaid: String?,
    //amount for each member of the transaction including the user paid or creator
    //in format (name,amount)
    @ColumnInfo(name = "memberAmount") var memberAmount: Map<String, Double>,
    //group name if group transaction
    @ColumnInfo(name = "groupName") var groupName: String?,
    @ColumnInfo(name = "time") var time: Long, //time in milliseconds
    @ColumnInfo(name = "note") var note: String?
) {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var id: Int? = null
}
