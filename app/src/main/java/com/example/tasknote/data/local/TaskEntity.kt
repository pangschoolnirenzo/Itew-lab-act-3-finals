package com.example.tasknote.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

// The server creates the IDs now, so the primary key is the server's String id.
@Entity(tableName = "tasks")
data class TaskEntity(
    @PrimaryKey val id: String,
    val title: String,
    val note: String,
    val isDone: Boolean,
    val createdAt: Long
)
