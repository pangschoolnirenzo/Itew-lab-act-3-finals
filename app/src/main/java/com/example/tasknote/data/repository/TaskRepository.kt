package com.example.tasknote.data.repository

import com.example.tasknote.data.local.TaskDao
import com.example.tasknote.data.mapper.toDomain
import com.example.tasknote.data.mapper.toDto
import com.example.tasknote.data.mapper.toEntity
import com.example.tasknote.data.remote.TaskApiService
import com.example.tasknote.data.remote.TaskDto
import com.example.tasknote.domain.model.Task
import com.example.tasknote.util.AppResult
import com.example.tasknote.util.safeApiCall
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import retrofit2.HttpException

// The ONLY class that talks to Room or the API.
// The UI reads from Room; the API only writes into Room.
class TaskRepository(
    private val api: TaskApiService,
    private val dao: TaskDao
) {
    val tasks: Flow<List<Task>> =
        dao.observeAll().map { list -> list.map { it.toDomain() } }

    suspend fun refresh(): AppResult<Unit> = safeApiCall {
        val remote = api.getTasks()
        dao.replaceAll(remote.map { it.toEntity() })
    }

    suspend fun addTask(title: String, note: String): AppResult<Unit> = safeApiCall {
        val created = api.createTask(
            TaskDto(title = title, note = note, createdAt = System.currentTimeMillis())
        )
        dao.upsert(created.toEntity())
    }

    suspend fun toggleDone(task: Task): AppResult<Unit> = safeApiCall {
        val updated = api.updateTask(task.id, task.copy(isDone = !task.isDone).toDto())
        dao.upsert(updated.toEntity())
    }

    suspend fun deleteTask(id: String): AppResult<Unit> = safeApiCall {
        val response = api.deleteTask(id)
        // Response<Unit> does not throw on 4xx/5xx, so check it yourself
        if (!response.isSuccessful && response.code() != 404) throw HttpException(response)
        dao.deleteById(id) // 404 = already gone on server, remove locally too
    }
}
