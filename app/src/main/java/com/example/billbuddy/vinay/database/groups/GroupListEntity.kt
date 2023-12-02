// GroupListEntity.kt

package com.example.billbuddy.vinay.database.groups

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "GroupList_table")
data class GroupListEntity(
    @PrimaryKey(autoGenerate = true)
    val groupId: Long = 0,

    @ColumnInfo(name = "group_name")
    val groupName: String,

    @ColumnInfo(name = "group_category")
    val groupCategory: String
)

