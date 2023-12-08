package com.example.billbuddy.vinay.database.groups

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.example.billbuddy.vinay.database.users.UserEntity

@Entity(
    tableName = "Group_member_table",
    foreignKeys = [
        ForeignKey(
            entity = GroupListEntity::class,
            parentColumns = ["groupId"],
            childColumns = ["Group_id"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = UserEntity::class,
            parentColumns = ["user_id"],
            childColumns = ["User_id"]
        )
    ],
    indices = [Index("Group_id"), Index("User_id")]
)
data class GroupMemberEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,

    @ColumnInfo(name = "Group_id")
    val groupId: Long,

    @ColumnInfo(name = "User_id")
    val userId: Long,

    @ColumnInfo(name = "User_name")
    val userName: String,

    @ColumnInfo(name = "User_phone")
    val userPhone: String,

    @ColumnInfo(name = "User_owe")
    val userOwe: Double,

    @ColumnInfo(name = "Group_owes")
    val groupOwes: Double,

    @ColumnInfo(name = "Total_due")
    val totalDue: Double
)

