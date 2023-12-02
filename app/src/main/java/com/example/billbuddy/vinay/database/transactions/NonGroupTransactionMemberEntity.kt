package com.example.billbuddy.vinay.database.transactions

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.Companion.CASCADE
import androidx.room.Index
import androidx.room.PrimaryKey
import com.example.billbuddy.vinay.database.users.UserEntity

@Entity(
    tableName = "NonGroup_Transaction_member",
    foreignKeys = [
        ForeignKey(
            entity = TransactionEntity::class,
            parentColumns = ["Transaction_id"],
            childColumns = ["transactionId"],
            onDelete = CASCADE
        )
    ],
    indices = [Index("transactionId")]
)
data class NonGroupTransactionMemberEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,

    @ColumnInfo(name = "transactionId")
    val transactionId: Long,

    @ColumnInfo(name = "userId")
    val userId: Long,

    @ColumnInfo(name = "Amount_owe")
    val amountOwe: Double,

    @ColumnInfo(name = "Amount_owes")
    val amountOwes: Double
)
