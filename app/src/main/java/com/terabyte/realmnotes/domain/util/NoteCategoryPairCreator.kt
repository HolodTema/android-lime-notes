package com.terabyte.realmnotes.domain.util

import com.terabyte.realmnotes.domain.model.Category
import com.terabyte.realmnotes.domain.model.Note
import com.terabyte.realmnotes.domain.model.NoteCategoryPair

object NoteCategoryPairCreator {

    suspend fun createNoteCategoryPairList(
        notes: List<Note>,
        categories: List<Category>
    ): List<NoteCategoryPair> {
        return notes.map { note ->
            NoteCategoryPair(
                note = note,
                category = categories.find { it.id == note.categoryId }
            )
        }.sorted().reversed()
    }

    suspend fun filterByNoteText(
        text: String,
        noteCategoryPairs: List<NoteCategoryPair>
    ): List<NoteCategoryPair> {
        return noteCategoryPairs.filter { pair ->
            pair.note.text.contains(text, ignoreCase = true)
        }
    }

}