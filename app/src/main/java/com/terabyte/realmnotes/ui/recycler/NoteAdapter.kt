package com.terabyte.realmnotes.ui.recycler

import android.icu.text.SimpleDateFormat
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.terabyte.realmnotes.databinding.ListItemNoteBinding
import com.terabyte.realmnotes.domain.model.Note
import java.util.Locale


class NoteDiffUtilItemCallback : DiffUtil.ItemCallback<Note>() {
    override fun areContentsTheSame(
        oldItem: Note,
        newItem: Note
    ): Boolean {
        return oldItem == newItem
    }

    override fun areItemsTheSame(
        oldItem: Note,
        newItem: Note
    ): Boolean {
        return oldItem.id == newItem.id
    }
}

class Holder(
    private val binding: ListItemNoteBinding,
    private val noteSelectedListener: (Note) -> Unit
) : RecyclerView.ViewHolder(binding.root) {
    private val dateFormat = SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.getDefault())

    fun bind(note: Note) {
        binding.textNote.text = note.text
        binding.textNoteDate.text = dateFormat.format(note.date)
        binding.root.setOnClickListener {
            noteSelectedListener(note)
        }
    }

}

class NoteAdapter(
    private val inflater: LayoutInflater,
    private val noteSelectedListener: (Note) -> Unit
) :
    ListAdapter<Note, Holder>(NoteDiffUtilItemCallback()) {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): Holder {
        val binding = ListItemNoteBinding.inflate(inflater, parent, false)
        return Holder(binding, noteSelectedListener)
    }

    override fun onBindViewHolder(
        holder: Holder,
        position: Int
    ) {
        holder.bind(getItem(position))
    }

}