package com.terabyte.realmnotes.domain.repository

import com.terabyte.realmnotes.domain.model.Note

interface NoteRepository {

    suspend fun getAllNotes(): List<Note>

    suspend fun addNote(note: Note)

    suspend fun updateNote(note: Note)

    suspend fun deleteNote(noteId: String)

    fun close()

}