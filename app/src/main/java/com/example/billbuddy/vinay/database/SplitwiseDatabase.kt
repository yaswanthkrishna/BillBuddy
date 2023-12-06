package com.example.billbuddy.vinay.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.billbuddy.vinay.database.friend_non_group.FriendDAO
import com.example.billbuddy.vinay.database.friend_non_group.FriendEntity
import com.example.billbuddy.vinay.database.friend_non_group.FriendTransactionDAO
import com.example.billbuddy.vinay.database.friend_non_group.FriendTransactionEntity
import com.example.billbuddy.vinay.database.groups.GroupListDAO
import com.example.billbuddy.vinay.database.groups.GroupListEntity
import com.example.billbuddy.vinay.database.groups.GroupMemberDAO
import com.example.billbuddy.vinay.database.groups.GroupMemberEntity
import com.example.billbuddy.vinay.database.groups.GroupTransactionDAO
import com.example.billbuddy.vinay.database.groups.GroupTransactionEntity
import com.example.billbuddy.vinay.database.groups.GroupTransactionMemberDAO
import com.example.billbuddy.vinay.database.groups.GroupTransactionMemberEntity
import com.example.billbuddy.vinay.database.recent_activity.Converters
import com.example.billbuddy.vinay.database.recent_activity.RecentActivityDAO
import com.example.billbuddy.vinay.database.recent_activity.RecentActivityEntity
import com.example.billbuddy.vinay.database.recent_activity.RecentActivityTransactionDAO
import com.example.billbuddy.vinay.database.recent_activity.RecentActivityTransactionEntity
import com.example.billbuddy.vinay.database.transactions.NonGroupTransactionMemberDAO
import com.example.billbuddy.vinay.database.transactions.NonGroupTransactionMemberEntity
import com.example.billbuddy.vinay.database.transactions.TransactionDAO
import com.example.billbuddy.vinay.database.transactions.TransactionEntity
import com.example.billbuddy.vinay.database.users.UserDAO
import com.example.billbuddy.vinay.database.users.UserEntity



@Database(entities = arrayOf(UserEntity::class,
    TransactionEntity::class, FriendTransactionEntity::class,GroupTransactionMemberEntity::class,NonGroupTransactionMemberEntity::class,
    FriendEntity::class,GroupListEntity::class,GroupMemberEntity::class,GroupTransactionEntity::class,RecentActivityEntity::class,RecentActivityTransactionEntity::class)
    , version = 2)
@TypeConverters(Converters::class)
abstract class SplitwiseDatabase() : RoomDatabase() {
    abstract fun getMyUserEntries(): UserDAO
    abstract fun getMyTransactionEntries(): TransactionDAO
    abstract fun getMyFriendTransactionEntries(): FriendTransactionDAO
    abstract fun getMyGroupTransactionMemberEntries(): GroupTransactionMemberDAO
    abstract fun getMyNonGroupTransactionMemberEntries(): NonGroupTransactionMemberDAO
    abstract fun getMyFriendEntries(): FriendDAO
    abstract fun getMyGroupListEntries(): GroupListDAO
    abstract fun getMyGroupMemberEntries(): GroupMemberDAO
    abstract fun getMyGroupTransactionEntries(): GroupTransactionDAO
    abstract fun getMyRecentActivityEntries(): RecentActivityDAO
    abstract fun getMyRecentActivityTransactionEntries(): RecentActivityTransactionDAO

    companion object {
        private var INSTANCE: SplitwiseDatabase? = null
        fun getDatabase(context: Context): SplitwiseDatabase {
            if (INSTANCE == null) {
                val builder = Room.databaseBuilder(
                    context.applicationContext,
                    SplitwiseDatabase::class.java,
                    "splitwise_db"
                )
                builder.fallbackToDestructiveMigration()
                return builder.build()
            } else {
                return INSTANCE!!
            }
        }
    }
}