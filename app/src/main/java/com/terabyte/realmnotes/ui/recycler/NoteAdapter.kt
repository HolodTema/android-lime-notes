package com.terabyte.realmnotes.ui.recycler

import android.content.res.ColorStateList
import android.icu.text.SimpleDateFormat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.terabyte.realmnotes.databinding.ListItemNoteBinding
import com.terabyte.realmnotes.domain.model.Category
import com.terabyte.realmnotes.domain.model.Note
import com.terabyte.realmnotes.domain.model.NoteCategoryPair
import java.util.Locale


class NoteCategoryPairDiffUtil : DiffUtil.ItemCallback<NoteCategoryPair>() {
    override fun areContentsTheSame(
        oldItem: NoteCategoryPair,
        newItem: NoteCategoryPair
    ): Boolean {
        return oldItem == newItem
    }

    override fun areItemsTheSame(
        oldItem: NoteCategoryPair,
        newItem: NoteCategoryPair
    ): Boolean {
        return oldItem.note.id == newItem.note.id
    }
}

class NoteHolder(
    private val binding: ListItemNoteBinding,
    private val noteSelectedListener: (Note) -> Unit
) : RecyclerView.ViewHolder(binding.root) {
    private val dateFormat = SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.getDefault())

    fun bind(noteCategoryPair: NoteCategoryPair) {
        binding.textNote.text = noteCategoryPair.note.text
        binding.textNoteDate.text = dateFormat.format(noteCategoryPair.note.date)
        binding.root.setOnClickListener {
            noteSelectedListener(noteCategoryPair.note)
        }

        if (noteCategoryPair.category == null) {
            binding.imageCategoryIcon.visibility = View.GONE
            binding.textNoteCategoryName.visibility = View.GONE
        }
        else {
            binding.imageCategoryIcon.visibility = View.VISIBLE
            binding.textNoteCategoryName.visibility = View.VISIBLE
            binding.imageCategoryIcon.imageTintList = ColorStateList.valueOf(noteCategoryPair.category.color)
            binding.textNoteCategoryName.text = noteCategoryPair.category.name
        }
    }

}

class NoteAdapter(
    private val inflater: LayoutInflater,
    private val noteSelectedListener: (Note) -> Unit
) :
    ListAdapter<NoteCategoryPair, NoteHolder>(NoteCategoryPairDiffUtil()) {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): NoteHolder {
        val binding = ListItemNoteBinding.inflate(inflater, parent, false)
        return NoteHolder(binding, noteSelectedListener)
    }

    override fun onBindViewHolder(
        holder: NoteHolder,
        position: Int
    ) {
        val noteCategoryPair = getItem(position)
        holder.bind(noteCategoryPair)
    }

}