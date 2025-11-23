package com.terabyte.realmnotes.domain.repository

import com.terabyte.realmnotes.domain.model.Category
import com.terabyte.realmnotes.domain.model.Note

interface NoteRepository {

    suspend fun getAllNotes(): List<Note>

    suspend fun addNote(note: Note)

    suspend fun updateNote(note: Note)

    suspend fun deleteNote(noteId: String)

    suspend fun deleteAllNotes()

    suspend fun getAllCategories(): List<Category>

    suspend fun addCategory(category: Category)

    suspend fun updateCategory(category: Category)

    suspend fun deleteCategory(categoryId: String)

    suspend fun deleteAllCategories()

    suspend fun removeCategoryIdFromAllNotes(categoryId: String)

    fun close()

}