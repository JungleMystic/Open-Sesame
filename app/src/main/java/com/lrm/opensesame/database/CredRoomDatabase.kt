package com.lrm.opensesame.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [LoginCred::class], version = 1, exportSchema = false)
abstract class CredRoomDatabase: RoomDatabase() {

    abstract fun credDao(): CredDao

    companion object {
        @Volatile
        private var INSTANCE: CredRoomDatabase? = null

        fun getDatabase(context: Context): CredRoomDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    CredRoomDatabase::class.java,
                    "cred_database"
                )
                    .fallbackToDestructiveMigration()
                    .build()

                INSTANCE = instance

                instance
            }
        }
    }
}