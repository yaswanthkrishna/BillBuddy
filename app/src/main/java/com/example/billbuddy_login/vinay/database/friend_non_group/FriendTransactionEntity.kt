package com.example.billbuddy_login.vinay.database.friend_non_group

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "friend_transaction_table")
data class FriendTransactionEntity(
    @ColumnInfo(name = "user_id") var user_id: Int,
    @ColumnInfo(name = "name") var name: String,
    @ColumnInfo(name = "number") var number: String,
    @ColumnInfo(name = "amount") var amount: Int,
    @ColumnInfo(name = "time") var time: String,
    @ColumnInfo(name = "description") var description: String,
    @ColumnInfo(name = "paidBy") var paidBy: String,        // New column for PaidBy
    @ColumnInfo(name = "splitType") var splitType: String  // New column for SplitType
) {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var id: Int? = null
}
