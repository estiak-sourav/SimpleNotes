package com.simple.notes.ui.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.simple.notes.data.NoteRepository
import com.simple.notes.data.local.AppDatabase

class NoteViewModelFactory(private val context: Context) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val repo = NoteRepository(AppDatabase.get(context))
        return NoteViewModel(repo) as T
    }
}