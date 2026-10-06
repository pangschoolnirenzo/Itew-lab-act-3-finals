package com.example.tasknote.data.remote

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

// Matches the MockAPI JSON exactly (snake_case, nullable id)
@Serializable
data class TaskDto(
    val id: String? = null, // null when we POST a new task
    val title: String,
    val note: String = "", // default = safe if the API omits it
    @SerialName("is_done") val isDone: Boolean = false,
    @SerialName("created_at") val createdAt: Long = 0L
)
