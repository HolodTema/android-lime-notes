package com.terabyte.realmnotes.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.terabyte.realmnotes.domain.model.Note
import com.terabyte.realmnotes.domain.repository.NoteRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

enum class MainFragmentState {
    FRAGMENT_NOTE_LIST,
    FRAGMENT_CATEGORY_LIST,
    FRAGMENT_SETTINGS
}

class MainViewModel(private val noteRepository: NoteRepository): ViewModel() {
    private val _stateFlowMainFragment = MutableStateFlow(MainFragmentState.FRAGMENT_NOTE_LIST)
    val stateFlowMainFragment: StateFlow<MainFragmentState> = _stateFlowMainFragment.asStateFlow()

    private var noteList: List<Note> = emptyList()
    private val _stateFlowNoteList = MutableStateFlow<List<Note>>(emptyList())
    val stateFlowNoteList: StateFlow<List<Note>> = _stateFlowNoteList.asStateFlow()

    private val _stateFlowNoteFilterText = MutableStateFlow<String>("")
    val stateFlowNoteFilterText: StateFlow<String> = _stateFlowNoteFilterText.asStateFlow()

    init {
        loadNotes()
        configureFilterNotesByText()
    }

    fun setMainFragmentState(mainFragmentState: MainFragmentState) {
        _stateFlowMainFragment.value = mainFragmentState
    }

    fun setNoteFilterText(text: String) {
        _stateFlowNoteFilterText.value = text
    }

    private fun loadNotes() {
        viewModelScope.launch {
            val deferred = async(Dispatchers.IO) {
                noteRepository.getAllNotes().sorted().reversed()
            }
            noteList = deferred.await()
            _stateFlowNoteList.value = noteList
        }
    }

    private fun configureFilterNotesByText() {
        viewModelScope.launch {
            stateFlowNoteFilterText.collect { text ->
                if (text.isBlank()) {
                    _stateFlowNoteList.value = noteList
                }
                else {
                    val deferred = async(Dispatchers.Default) {
                        noteList.filter { note ->
                            note.text.contains(text, ignoreCase = true)
                        }
                    }
                    _stateFlowNoteList.value = deferred.await()
                }
            }
        }
    }

    @Suppress("UNCHECKED_CAST")
    class Factory(private val noteRepository: NoteRepository): ViewModelProvider.Factory {

        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return MainViewModel(noteRepository) as T
        }

    }
}