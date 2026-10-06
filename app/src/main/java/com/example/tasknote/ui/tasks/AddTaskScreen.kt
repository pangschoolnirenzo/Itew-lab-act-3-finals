package com.example.tasknote.ui.tasks

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController

@Composable
fun AddTaskScreen(viewModel: TaskViewModel, navController: NavController) {
    var title by remember { mutableStateOf("") }
    var note by remember { mutableStateOf("") }

    val state by viewModel.uiState.collectAsStateWithLifecycle()

    // Don't leave an old error behind for the list screen
    DisposableEffect(Unit) { onDispose { viewModel.errorShown() } }

    Column(modifier = Modifier.padding(16.dp)) {
        OutlinedTextField(
            value = title,
            onValueChange = { title = it },
            label = { Text("New task") },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = note,
            onValueChange = { note = it },
            label = { Text("Note (optional)") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp)
        )

        // Disabled while a request runs to prevent double submits
        Button(
            onClick = { viewModel.addTask(title, note) { navController.popBackStack() } },
            enabled = !state.isLoading,
            modifier = Modifier.padding(top = 8.dp)
        ) {
            Text(if (state.isLoading) "Saving..." else "Save")
        }

        state.errorMessage?.let { message ->
            Text(
                text = message,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(top = 8.dp)
            )
        }

        Button(onClick = { navController.popBackStack() }) {
            Text("Back to List")
        }
    }
}
