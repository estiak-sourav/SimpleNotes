package com.simple.notes.ui.main

import com.simple.notes.data.NoteRepository
import com.simple.notes.data.local.Note
import com.simple.notes.ui.viewmodel.NoteViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

suspend fun NoteViewModel.repoGetById(id: Int): Note? = withContext(Dispatchers.IO) {
    val field = NoteViewModel::class.java.getDeclaredField("repo").apply { isAccessible = true }
    val repo = field.get(this@repoGetById) as NoteRepository
    repo.getById(id)
}