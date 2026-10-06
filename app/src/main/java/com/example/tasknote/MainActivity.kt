package com.example.tasknote

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.tasknote.navigation.AppNavigation
import com.example.tasknote.ui.tasks.TaskViewModel
import com.example.tasknote.ui.theme.TaskNoteTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            // Dependencies come from AppContainer through the ViewModel factory
            val viewModel: TaskViewModel = viewModel(factory = TaskViewModel.Factory)
            TaskNoteTheme {
                AppNavigation(viewModel)
            }
        }
    }
}
