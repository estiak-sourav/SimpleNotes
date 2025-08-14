package com.simple.notes.data

import com.simple.notes.data.local.AppDatabase
import com.simple.notes.data.local.Note
import kotlinx.coroutines.flow.Flow

class NoteRepository(private val db: AppDatabase) {
    private val dao = db.noteDao()

    fun notes(query: String?): Flow<List<Note>> =
        if (query.isNullOrBlank()) dao.getAll() else dao.search(query)

    suspend fun getById(id: Int) = dao.getById(id)

    suspend fun add(title: String, description: String) {
        val now = System.currentTimeMillis()
        dao.insert(Note(title = title, description = description, createdAt = now, updatedAt = now))
    }

    suspend fun update(id: Int, title: String, description: String) {
        val existing = dao.getById(id) ?: return
        dao.update(existing.copy(title = title, description = description, updatedAt = System.currentTimeMillis()))
    }

    suspend fun delete(note: Note) = dao.delete(note)
}