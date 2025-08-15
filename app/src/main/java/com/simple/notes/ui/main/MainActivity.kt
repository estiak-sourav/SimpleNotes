package com.simple.notes.ui.main


import com.simple.notes.R

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.simple.notes.databinding.ActivityMainBinding
import com.simple.notes.ui.noteEdit.AddEditNoteActivity
import com.simple.notes.ui.viewmodel.NoteViewModel
import com.simple.notes.ui.viewmodel.NoteViewModelFactory
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val vm: NoteViewModel by viewModels { NoteViewModelFactory(this) }
    private val adapter = NoteListAdapter(
        onClick = { note ->
            val i = Intent(this, AddEditNoteActivity::class.java)
            i.putExtra(AddEditNoteActivity.EXTRA_NOTE_ID, note.id)
            startActivity(i)
        },
        onLongClick = {note ->
            AlertDialog.Builder(binding.root.context)
                .setTitle("Delete Note")
                .setMessage("Are you sure you want to delete this note?")
                .setPositiveButton("Delete") { _, _ ->
                    vm.delete(note)
                    Toast.makeText(this, "Note deleted", Toast.LENGTH_SHORT).show()
                }
                .setNegativeButton("Cancel", null)
                .show()
        }
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)

        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val sysBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(sysBars.left, sysBars.top, sysBars.right, sysBars.bottom)
            insets
        }

        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = adapter

        ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ) = false

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val note = adapter.currentList[viewHolder.bindingAdapterPosition]
                vm.delete(note)
                Snackbar.make(binding.root, R.string.deleted, Snackbar.LENGTH_LONG)
                    .setAction(R.string.undo) {
                        vm.add(note.title, note.description)
                    }.show()
            }
        }).attachToRecyclerView(binding.recyclerView)

        binding.fab.setOnClickListener {
            startActivity(Intent(this, AddEditNoteActivity::class.java))
        }

        // Observe notes
        lifecycle.addObserver(LifecycleLogger("MainActivity"))
        lifecycleScope.launch {
            vm.notes.collect { list ->
                adapter.submitList(list)
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        val searchItem: MenuItem = menu.findItem(R.id.action_search)
        val searchView = searchItem.actionView as SearchView
        searchView.queryHint = getString(R.string.search_hint)
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                vm.setQuery(query.orEmpty())
                return true
            }
            override fun onQueryTextChange(newText: String?): Boolean {
                vm.setQuery(newText.orEmpty())
                return true
            }
        })
        return true
    }
}