package com.example.tasknote.di

import android.content.Context
import com.example.tasknote.data.local.TaskDatabase
import com.example.tasknote.data.remote.NetworkModule
import com.example.tasknote.data.repository.TaskRepository

// Creates dependencies in one place
class AppContainer(context: Context) {
    private val database = TaskDatabase.getInstance(context)
    val taskRepository = TaskRepository(NetworkModule.api, database.taskDao())
}
