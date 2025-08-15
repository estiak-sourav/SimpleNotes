package com.simple.notes.ui.main

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.simple.notes.data.local.Note
import com.simple.notes.databinding.ItemNoteBinding
import com.simple.notes.utils.NoteDiffCallback
import java.text.SimpleDateFormat
import java.util.*

class NoteListAdapter(
    private val onClick: (Note) -> Unit,
    private val onLongClick: (Note) -> Unit
) : ListAdapter<Note, NoteListAdapter.VH>(NoteDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val binding = ItemNoteBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return VH(binding)
    }

    override fun onBindViewHolder(holder: VH, position: Int) = holder.bind(getItem(position))

    inner class VH(private val binding: ItemNoteBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(note: Note) {
            binding.tvTitle.text = note.title
            binding.tvDesc.text = note.description
            val sdf = SimpleDateFormat("MMM d, yyyy • HH:mm", Locale.getDefault())
            binding.tvDate.text = sdf.format(Date(note.updatedAt))
            binding.root.setOnClickListener { onClick(note) }
            binding.root.setOnLongClickListener { onLongClick(note); true}
        }
    }
}