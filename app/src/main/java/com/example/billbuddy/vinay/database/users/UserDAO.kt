package com.example.billbuddy.vinay.database.users

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface UserDAO {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addUser(userEntity: UserEntity)

    @Query("select * from user_table")
    fun getUserList(): LiveData<List<UserEntity>>

    @Update
    suspend fun updateUser(userEntity: UserEntity)

    @Delete
    fun deleteUser(userEntity: UserEntity)

    @Query("SELECT user_id FROM user_table WHERE name = :userName")
    fun getUserIdByName(userName: String): Long?

    @Query("SELECT user_id FROM user_table WHERE email = :userEmail")
    fun getUserIdByEmail(userEmail: String): Long?

    @Query("SELECT name, phone FROM user_table WHERE user_id = :userId")
    suspend fun getNameAndPhoneByUserId(userId: Long): NameAndPhone?

    data class NameAndPhone(val name: String, val phone: String)

}

