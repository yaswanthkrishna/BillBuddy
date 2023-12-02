package com.example.billbuddy.vinay.database.friend_non_group

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface FriendDAO {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addFriend(friendEntity: FriendEntity)

    @Query("SELECT * FROM friends_table where user_id=:userId")
    fun getFriendsList(userId: Long): LiveData<List<FriendEntity>>

    @Update
    suspend fun updateFriend(friendEntity: FriendEntity)

    @Delete
    suspend fun deleteFriend(friendEntity: FriendEntity)
}