package com.example.billbuddy.vinay.database.groups

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.example.billbuddy.vinay.database.transactions.TransactionEntity
import com.example.billbuddy.vinay.database.users.UserEntity

@Entity(
    tableName = "Group_Transaction_member",
    foreignKeys = [
        ForeignKey(
            entity = TransactionEntity::class,
            parentColumns = ["Transaction_id"],
            childColumns = ["Transaction_id"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = UserEntity::class,
            parentColumns = ["user_id"],
            childColumns = ["user_id"]
        )
    ],
    indices = [Index("Transaction_id"), Index("user_id")]
)
data class GroupTransactionMemberEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,

    @ColumnInfo(name = "Group_id")
    val groupId: Long,

    @ColumnInfo(name = "Transaction_id")
    val transactionId: Long,

    @ColumnInfo(name = "user_id")
    val userId: Long,

    @ColumnInfo(name = "Amount_owe")
    val amountOwe: Double,

    @ColumnInfo(name = "Amount_owes")
    val amountOwes: Double
)
