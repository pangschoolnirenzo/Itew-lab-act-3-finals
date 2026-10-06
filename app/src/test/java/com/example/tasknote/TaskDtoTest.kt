package com.example.tasknote

import com.example.tasknote.data.remote.TaskDto
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class TaskDtoTest {
    // Remove ignoreUnknownKeys = true to see the exception for the report, then put it back.
    private val json = Json { ignoreUnknownKeys = true }

    @Test
    fun decodesSnakeCaseAndDefaults() {
        val raw = """{"id":"7","title":"Buy milk","is_done":true,"extra":1}"""
        val dto = json.decodeFromString<TaskDto>(raw)
        assertEquals("7", dto.id)
        assertTrue(dto.isDone)
        assertEquals("", dto.note) // default used
    }
}
