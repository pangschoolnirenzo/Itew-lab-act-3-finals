package com.example.tasknote.ui.tasks

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import java.text.DateFormat
import java.util.Date

@Composable
fun TaskDetailScreen(taskId: String, viewModel: TaskViewModel, navController: NavController) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    val task = state.tasks.find { it.id == taskId }

    Column(modifier = Modifier.padding(16.dp)) {
        Text(
            text = task?.title ?: "Task not found",
            style = MaterialTheme.typography.titleLarge
        )

        if (task != null) {
            if (task.note.isNotBlank()) {
                Text(task.note, modifier = Modifier.padding(top = 8.dp))
            }
            Text(
                text = if (task.isDone) "Status: Done" else "Status: Not done",
                modifier = Modifier.padding(top = 8.dp)
            )
            if (task.createdAt > 0L) {
                Text(
                    text = "Created: " + DateFormat.getDateTimeInstance().format(Date(task.createdAt)),
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }
        }

        Button(onClick = { navController.popBackStack() }, modifier = Modifier.padding(top = 8.dp)) {
            Text("Back")
        }
    }
}
