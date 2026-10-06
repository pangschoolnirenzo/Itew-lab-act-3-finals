package com.example.tasknote.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

// Version 2: the schema changed (String id, note, createdAt).
@Database(entities = [TaskEntity::class], version = 2, exportSchema = false)
abstract class TaskDatabase : RoomDatabase() {
    abstract fun taskDao(): TaskDao

    companion object {
        @Volatile private var INSTANCE: TaskDatabase? = null

        fun getInstance(context: Context): TaskDatabase =
            INSTANCE ?: synchronized(this) {
                Room.databaseBuilder(
                    context.applicationContext, TaskDatabase::class.java, "task_database"
                )
                    // Lab only: the local data is just a cache that is reloaded from the API.
                    // A real app would write a Migration so users keep their data.
                    .fallbackToDestructiveMigration()
                    .build().also { INSTANCE = it }
            }
    }
}
