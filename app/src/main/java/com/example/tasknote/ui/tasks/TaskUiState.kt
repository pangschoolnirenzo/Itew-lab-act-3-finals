package com.example.tasknote.ui.tasks

import com.example.tasknote.domain.model.Task

// One data class (not Loading/Success/Error) so cached tasks and an error can show together.
data class TaskUiState(
    val tasks: List<Task> = emptyList(),
    val isLoading: Boolean = false, // a request is running
    val errorMessage: String? = null // shown once, then dismissed
)
