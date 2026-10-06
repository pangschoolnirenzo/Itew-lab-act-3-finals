package com.example.tasknote.ui.tasks

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.example.tasknote.R
import com.example.tasknote.domain.model.Task
import com.example.tasknote.navigation.Routes

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskScreen(viewModel: TaskViewModel, navController: NavController) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    val snackbar = remember { SnackbarHostState() }

    // Tasks shown + error -> the list stays visible and the message appears in a Snackbar
    LaunchedEffect(state.errorMessage) {
        val msg = state.errorMessage
        if (msg != null && state.tasks.isNotEmpty()) {
            snackbar.showSnackbar(msg)
            viewModel.errorShown()
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbar) },
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.task_list_title)) },
                actions = {
                    IconButton(onClick = viewModel::refresh, enabled = !state.isLoading) {
                        Icon(Icons.Filled.Refresh, contentDescription = "Refresh tasks")
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { navController.navigate(Routes.ADD_TASK) }) {
                Icon(Icons.Filled.Add, contentDescription = "Add task")
            }
        }
    ) { padding ->
        Box(Modifier.padding(padding).fillMaxSize()) {
            when {
                // 1. First load, no cached tasks
                state.isLoading && state.tasks.isEmpty() ->
                    CircularProgressIndicator(Modifier.align(Alignment.Center))

                // 2. No tasks and an error
                state.tasks.isEmpty() && state.errorMessage != null ->
                    ErrorState(state.errorMessage!!, onRetry = viewModel::refresh)

                // 3. No tasks, no error
                state.tasks.isEmpty() -> EmptyState()

                // 4/5. Tasks shown (thin progress bar while a request runs)
                else -> Column {
                    if (state.isLoading) LinearProgressIndicator(Modifier.fillMaxWidth())
                    TaskList(
                        tasks = state.tasks,
                        onToggle = viewModel::toggleDone,
                        onDelete = viewModel::deleteTask,
                        onOpen = { navController.navigate("task_detail/${it.id}") }
                    )
                }
            }
        }
    }
}

@Composable
fun TaskList(
    tasks: List<Task>,
    onToggle: (Task) -> Unit,
    onDelete: (String) -> Unit,
    onOpen: (Task) -> Unit
) {
    LazyColumn(modifier = Modifier.fillMaxWidth()) {
        items(tasks, key = { it.id }) { task ->
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    // Week 10 hands-on task: tap a row to open its detail screen
                    .clickable { onOpen(task) }
                    .padding(dimensionResource(id = R.dimen.padding_medium))
            ) {
                Checkbox(
                    checked = task.isDone,
                    onCheckedChange = { onToggle(task) }
                )
                Text(task.title, modifier = Modifier.weight(1f))
                IconButton(onClick = { onDelete(task.id) }) {
                    Icon(Icons.Default.Delete, contentDescription = "Delete task")
                }
            }
        }
    }
}

@Composable
fun EmptyState() {
    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text(
            text = "No tasks yet. Tap + to add one.",
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(dimensionResource(id = R.dimen.padding_large))
        )
    }
}

@Composable
fun ErrorState(message: String, onRetry: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(dimensionResource(id = R.dimen.padding_large)),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            Icons.Filled.Warning,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.error
        )
        Text(
            text = message,
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(vertical = 12.dp)
        )
        Button(onClick = onRetry) { Text("Retry") }
    }
}
