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

    @Query("UPDATE friends_table SET owe = owe + :amount WHERE user_id = :userId AND friend_user_id = :friendUserId")
    suspend fun updateOweAmount(userId: Long, friendUserId: Long, amount: Double)

    @Query("UPDATE friends_table SET owes = owes + :amount WHERE user_id = :userId AND friend_user_id = :friendUserId")
    suspend fun updateOwesAmount(userId: Long, friendUserId: Long, amount: Double)

    @Query("UPDATE friends_table SET total_due = owes - owe WHERE user_id = :userId AND friend_user_id = :friendUserId")
    suspend fun updateTotalDue(userId: Long, friendUserId: Long)

    @Query("SELECT * FROM friends_table WHERE name LIKE :name")
    suspend fun getFriendsByName(name: String): List<FriendEntity>

    @Query("SELECT SUM(total_due) FROM friends_table WHERE user_id = :userId")
    suspend fun getTotalOwedByOthers(userId: Long): Double?

}