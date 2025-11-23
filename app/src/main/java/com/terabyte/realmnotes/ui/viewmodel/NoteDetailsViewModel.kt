package com.terabyte.realmnotes.ui.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.terabyte.realmnotes.LOG_TAG_DEBUG
import com.terabyte.realmnotes.domain.model.Category
import com.terabyte.realmnotes.domain.model.Note
import com.terabyte.realmnotes.domain.repository.NoteRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

enum class NoteDetailsState {
    ADD_NOTE,
    UPDATE_NOTE
}

class NoteDetailsViewModel(private val noteRepository: NoteRepository): ViewModel() {
    private val _stateFlowNote = MutableStateFlow(Note())
    val stateFlowNote: StateFlow<Note> = _stateFlowNote.asStateFlow()

    private val _stateFlowNoteDetails = MutableStateFlow(NoteDetailsState.ADD_NOTE)
    val stateFlowNoteDetails: StateFlow<NoteDetailsState> = _stateFlowNoteDetails.asStateFlow()

    private val _stateFlowCategoryList = MutableStateFlow<List<Category>>(emptyList())
    val stateFlowCategoryList: StateFlow<List<Category>> = _stateFlowCategoryList.asStateFlow()

    init {
        loadCategories()
    }

    fun loadCategories() {
        viewModelScope.launch {
            val deferred = async(Dispatchers.IO) {
                val categories = noteRepository.getAllCategories().toMutableList()
                categories.add(0, Category())
                categories.toList()
            }
            _stateFlowCategoryList.value = deferred.await()
        }
    }

    fun setStateUpdate(note: Note) {
        _stateFlowNoteDetails.value = NoteDetailsState.UPDATE_NOTE
        _stateFlowNote.value = note
    }

    fun saveNote(noteSavedListener: ()->Unit) {
        viewModelScope.launch {
            val deferred = async(Dispatchers.IO) {
                when (stateFlowNoteDetails.value) {
                    NoteDetailsState.UPDATE_NOTE -> {
                        noteRepository.updateNote(stateFlowNote.value)

                    }
                    NoteDetailsState.ADD_NOTE -> {
                        noteRepository.addNote(stateFlowNote.value)
                    }
                }
            }

            deferred.await()
            Log.d(LOG_TAG_DEBUG, "note was saved")
            noteSavedListener()
        }
    }

    fun deleteNote(noteDeletedListener: ()->Unit) {
        val noteId = stateFlowNote.value.id
        if (noteId == null) {
            return
        }

        viewModelScope.launch {
            val deferred = async(Dispatchers.IO) {
                noteRepository.deleteNote(noteId)
            }
            deferred.await()
            noteDeletedListener()
        }
    }

    fun updateNoteText(text: String) {
        _stateFlowNote.value.text = text
    }

    fun updateNoteCategoryId(categoryId: String?) {
        _stateFlowNote.value.categoryId = categoryId
    }

    @Suppress("UNCHECKED_CAST")
    class Factory(private val noteRepository: NoteRepository): ViewModelProvider.Factory {

        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return NoteDetailsViewModel(noteRepository) as T
        }

    }
}