package com.simple.notes.ui.noteEdit


import com.simple.notes.R

import android.os.Bundle
import android.view.MenuItem
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.snackbar.Snackbar
import com.simple.notes.data.local.Note
import com.simple.notes.databinding.ActivityAddEditNoteBinding
import com.simple.notes.ui.main.repoGetById
import com.simple.notes.ui.viewmodel.NoteViewModel
import com.simple.notes.ui.viewmodel.NoteViewModelFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AddEditNoteActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddEditNoteBinding
    private val vm: NoteViewModel by viewModels { NoteViewModelFactory(this) }

    private var currentId: Int? = null
    private var loadedNote: Note? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityAddEditNoteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        currentId = intent.getIntExtra(EXTRA_NOTE_ID, -1).takeIf { it != -1 }
        title =
            if (currentId == null) getString(R.string.add_note) else getString(R.string.edit_note)
        binding.btnSave.text =
            if (currentId == null) getString(R.string.save) else getString(R.string.update)

        currentId?.let { id ->
            CoroutineScope(Dispatchers.Main).launch {
                loadedNote = vm.repoGetById(id) // helper via extension below
                loadedNote?.let {
                    binding.etTitle.setText(it.title)
                    binding.etDesc.setText(it.description)
                }
            }
        }

        binding.btnSave.setOnClickListener { saveNote() }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean = when(item.itemId) {
        android.R.id.home -> { finish(); true }
        else -> super.onOptionsItemSelected(item)
    }

    private fun saveNote() {
        val title = binding.etTitle.text.toString().trim()
        val desc = binding.etDesc.text.toString().trim()
        if (title.isEmpty()) {
            Snackbar.make(binding.root, R.string.title_required, Snackbar.LENGTH_SHORT).show()
            return
        }
        currentId?.let { id -> vm.update(id, title, desc) } ?: vm.add(title, desc)
        finish()
    }

    companion object {
        const val EXTRA_NOTE_ID = "extra_note_id"
    }
}