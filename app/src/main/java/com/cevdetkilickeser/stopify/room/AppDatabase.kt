package com.cevdetkilickeser.stopify.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.cevdetkilickeser.stopify.data.entity.Download
import com.cevdetkilickeser.stopify.data.entity.History
import com.cevdetkilickeser.stopify.data.entity.Like

@Database(entities = [History::class, Like::class, Download::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    abstract fun getHistoryDao(): HistoryDao
    abstract fun getLikeDao(): LikeDao
    abstract fun getDownloadDao(): DownloadDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "app_database"
                ).build()

                INSTANCE = instance

                instance
            }
        }
    }
}