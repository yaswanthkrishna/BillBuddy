package com.example.billbuddy.vinay.database.friend_non_group

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.billbuddy.vinay.database.friend_non_group.FriendDAO
import com.example.billbuddy.vinay.database.friend_non_group.FriendEntity
import com.example.billbuddy.vinay.database.users.UserDAO
import com.example.billbuddy.vinay.database.users.UserEntity

@Database(entities = [FriendEntity::class, UserEntity::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun friendDao(): FriendDAO
    abstract fun userDao(): UserDAO // Add this line to provide access to UserDAO

    companion object {
        // Singleton prevents multiple instances of database opening at the same time.
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "app_database" // Database name
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
