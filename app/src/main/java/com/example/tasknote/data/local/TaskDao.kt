package com.example.tasknote.data.local

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface TaskDao {
    // Emits a new list every time the table changes (single source of truth)
    @Query("SELECT * FROM tasks ORDER BY createdAt DESC")
    fun observeAll(): Flow<List<TaskEntity>>

    @Upsert
    suspend fun upsert(task: TaskEntity)

    @Upsert
    suspend fun upsertAll(tasks: List<TaskEntity>)

    @Query("DELETE FROM tasks WHERE id = :id")
    suspend fun deleteById(id: String)

    @Query("DELETE FROM tasks")
    suspend fun clear()

    // clear + insert in one transaction so the UI never sees an empty in-between list
    @Transaction
    suspend fun replaceAll(tasks: List<TaskEntity>) {
        clear()
        upsertAll(tasks)
    }
}
