package com.example.tasknote.domain.model

// Domain model: what the UI and ViewModel use. No library annotations here.
data class Task(
    val id: String,
    val title: String,
    val note: String,
    val isDone: Boolean,
    val createdAt: Long
)
