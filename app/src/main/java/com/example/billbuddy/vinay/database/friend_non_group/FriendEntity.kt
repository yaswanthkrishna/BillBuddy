package com.example.billbuddy.vinay.database.friend_non_group

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.example.billbuddy.vinay.database.users.UserEntity

@Entity(
    tableName = "friends_table",
    foreignKeys = [
        ForeignKey(
            entity = UserEntity::class,
            parentColumns = ["user_id"],
            childColumns = ["user_id"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = UserEntity::class,
            parentColumns = ["user_id"],
            childColumns = ["friend_user_id"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("user_id"), Index("friend_user_id")]
)
data class FriendEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,

    @ColumnInfo(name = "user_id")
    val userId: Long,

    @ColumnInfo(name = "friend_user_id")
    val friendUserId: Long,

    @ColumnInfo(name = "owe")
    val owe: Double,

    @ColumnInfo(name = "owes")
    val owes: Double,

    @ColumnInfo(name = "total_due")
    val totalDue: Double
)
