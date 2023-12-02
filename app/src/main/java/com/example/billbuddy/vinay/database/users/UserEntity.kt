package com.example.billbuddy.vinay.database.users

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_table")
data class UserEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "user_id") var user_id: Int=0,
    @ColumnInfo(name = "name") var name: String,
    @ColumnInfo(name = "phone") var phone: String?,
    @ColumnInfo(name = "email") var email: String,
    @ColumnInfo(name = "password") var password: String,
    @ColumnInfo(name = "gender") var gender: String,
    @ColumnInfo(name = "owe") var owe: String,
    @ColumnInfo(name = "owes") var owes: String
)