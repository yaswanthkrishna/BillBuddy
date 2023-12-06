package com.example.billbuddy.vinay.database.recent_activity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
@Entity(tableName = "recentActivity_table")
data class RecentActivityEntity(
    @ColumnInfo(name = "time") var time: Long, //time in milliseconds
    //Comment,createGroup,deleteGroup,addToGroup,recordPayment...
    @ColumnInfo(name = "type") var type: String?,
    //name for creator of activity
    @ColumnInfo(name = "creator") var creator: String?,
    //Total amount, -1 if not involved in money
    @ColumnInfo(name = "amount") var amount: Int,
    //empty if it's not a group transaction
    @ColumnInfo(name = "targetGroupName") var targetGroupName: String?,
    @ColumnInfo(name = "targetUser") var targetUser: String?,

    @ColumnInfo(name = "comment") var comment: String?,
    //Name of non_group transaction if applicable
    @ColumnInfo(name = "transactionInfo") var transactionname: String?
) {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var id: Int? = null
}
