package com.simple.notes.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.simple.notes.data.NoteRepository
import com.simple.notes.data.local.Note
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class NoteViewModel(private val repo: NoteRepository) : ViewModel() {

    private val _query = MutableStateFlow("")
    val query: StateFlow<String> = _query.asStateFlow()

    val notes: StateFlow<List<Note>> = _query
        .debounce(200)
        .flatMapLatest { q -> repo.notes(q.ifBlank { null }) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    fun setQuery(q: String) { _query.value = q }

    fun add(title: String, description: String) = viewModelScope.launch {
        repo.add(title, description)
    }

    fun update(id: Int, title: String, description: String) = viewModelScope.launch {
        repo.update(id, title, description)
    }

    fun delete(note: Note) = viewModelScope.launch { repo.delete(note) }
}