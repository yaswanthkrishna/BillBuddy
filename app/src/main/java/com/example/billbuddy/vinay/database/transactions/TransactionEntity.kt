package com.example.billbuddy.vinay.database.transactions

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Transaction_table")
data class TransactionEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "Transaction_id")
    val transactionId: Long = 0,

    @ColumnInfo(name = "Total_amount")
    val totalAmount: Double,

    @ColumnInfo(name = "Paid_by_userid")
    val paidByUserId: Long,

    @ColumnInfo(name = "Split_type")
    val splitType: String,

    @ColumnInfo(name = "Group_flag")
    val groupFlag: Boolean,

    @ColumnInfo(name = "Receipt_image")
    val receiptImage: String?, // Assuming a path or URL to the image

    @ColumnInfo(name = "Comments")
    val comments: String?,

    @ColumnInfo(name = "Description")
    val description: String?,

    @ColumnInfo(name = "Transaction_datetime")
    val transactionDateTime: String
)
