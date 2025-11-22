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

    private val _stateFlowNoteList = MutableStateFlow<List<Note>>(emptyList())
    val stateFlowNoteList: StateFlow<List<Note>> = _stateFlowNoteList

    init {
        loadNotes()
    }

    fun setMainFragmentState(mainFragmentState: MainFragmentState) {
        _stateFlowMainFragment.value = mainFragmentState
    }

    private fun loadNotes() {
        viewModelScope.launch {
            val deferred = async(Dispatchers.IO) {
                noteRepository.getAllNotes()
            }
            _stateFlowNoteList.value = deferred.await()
        }
    }

    @Suppress("UNCHECKED_CAST")
    class Factory(private val noteRepository: NoteRepository): ViewModelProvider.Factory {

        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return MainViewModel(noteRepository) as T
        }

    }
}