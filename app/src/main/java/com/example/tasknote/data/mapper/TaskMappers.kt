package com.example.tasknote.data.mapper

import com.example.tasknote.data.local.TaskEntity
import com.example.tasknote.data.remote.TaskDto
import com.example.tasknote.domain.model.Task

fun TaskDto.toEntity() = TaskEntity(
    id = requireNotNull(id) { "Server returned a task without an id" },
    title = title, note = note, isDone = isDone, createdAt = createdAt
)

fun TaskEntity.toDomain() = Task(id, title, note, isDone, createdAt)

fun Task.toDto() = TaskDto(id, title, note, isDone, createdAt)
