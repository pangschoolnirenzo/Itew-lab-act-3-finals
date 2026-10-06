package com.example.tasknote.ui.tasks

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.tasknote.TaskNoteApp
import com.example.tasknote.data.repository.TaskRepository
import com.example.tasknote.domain.model.Task
import com.example.tasknote.util.AppResult
import com.example.tasknote.util.toMessage
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class TaskViewModel(private val repository: TaskRepository) : ViewModel() {

    private val isLoading = MutableStateFlow(false)
    private val errorMessage = MutableStateFlow<String?>(null)

    // Number of requests running right now (viewModelScope runs on the main thread)
    private var runningRequests = 0

    val uiState: StateFlow<TaskUiState> =
        combine(repository.tasks, isLoading, errorMessage) { tasks, loading, error ->
            TaskUiState(tasks, loading, error)
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = TaskUiState(isLoading = true)
        )

    // Runs once per ViewModel, so rotating the device does not start a second request
    init { refresh() }

    fun refresh() {
        if (isLoading.value) return // a request is already running
        launchWithState { repository.refresh() }
    }

    // onSuccess lets the Add screen close itself after the task was saved
    fun addTask(title: String, note: String, onSuccess: () -> Unit = {}) {
        if (title.isBlank()) {
            errorMessage.value = "Title cannot be empty."
            return // blank titles are never sent to the server
        }
        launchWithState(onSuccess) { repository.addTask(title.trim(), note.trim()) }
    }

    fun toggleDone(task: Task) = launchWithState { repository.toggleDone(task) }

    fun deleteTask(id: String) = launchWithState { repository.deleteTask(id) }

    fun errorShown() { errorMessage.value = null }

    private fun launchWithState(
        onSuccess: () -> Unit = {},
        action: suspend () -> AppResult<Unit>
    ) {
        viewModelScope.launch {
            runningRequests++
            isLoading.value = true
            errorMessage.value = null
            when (val result = action()) {
                is AppResult.Success -> onSuccess()
                is AppResult.Failure -> errorMessage.value = result.error.toMessage()
            }
            runningRequests--
            if (runningRequests == 0) isLoading.value = false
        }
    }

    companion object {
        val Factory = viewModelFactory {
            initializer {
                val app = this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as TaskNoteApp
                TaskViewModel(app.container.taskRepository)
            }
        }
    }
}
