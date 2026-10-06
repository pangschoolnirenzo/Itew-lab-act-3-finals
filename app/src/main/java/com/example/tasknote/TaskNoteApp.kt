package com.example.tasknote

import android.app.Application
import com.example.tasknote.di.AppContainer

class TaskNoteApp : Application() {
    lateinit var container: AppContainer

    override fun onCreate() {
        super.onCreate()
        container = AppContainer(this)
    }
}
