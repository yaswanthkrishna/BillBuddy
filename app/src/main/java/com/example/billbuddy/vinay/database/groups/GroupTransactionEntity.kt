package com.example.billbuddy.vinay.database.groups

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.example.billbuddy.vinay.database.transactions.TransactionEntity

@Entity(
    tableName = "Group_Transaction_table",
    foreignKeys = [
        ForeignKey(
            entity = GroupListEntity::class,
            parentColumns = ["groupId"],
            childColumns = ["Group_id"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = TransactionEntity::class,
            parentColumns = ["Transaction_id"],
            childColumns = ["Transaction_id"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("Group_id"), Index("Transaction_id")]
)
data class GroupTransactionEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,

    @ColumnInfo(name = "Group_id")
    val groupId: Long,

    @ColumnInfo(name = "Transaction_id")
    val transactionId: Long,

    @ColumnInfo(name = "Total_amount")
    val totalAmount: Double
)

