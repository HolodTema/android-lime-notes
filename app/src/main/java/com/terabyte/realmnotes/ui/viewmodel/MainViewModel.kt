package com.terabyte.realmnotes.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.terabyte.realmnotes.domain.model.Category
import com.terabyte.realmnotes.domain.model.NoteCategoryPair
import com.terabyte.realmnotes.domain.repository.NoteRepository
import com.terabyte.realmnotes.domain.util.NoteCategoryPairCreator
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

class MainViewModel(private val noteRepository: NoteRepository) : ViewModel() {
    private val _stateFlowMainFragment = MutableStateFlow(MainFragmentState.FRAGMENT_NOTE_LIST)
    val stateFlowMainFragment: StateFlow<MainFragmentState> = _stateFlowMainFragment.asStateFlow()


    private val _stateFlowNoteFilterText = MutableStateFlow<String>("")
    val stateFlowNoteFilterText: StateFlow<String> = _stateFlowNoteFilterText.asStateFlow()


    private val _stateFlowCategoryList = MutableStateFlow<List<Category>>(emptyList())
    val stateFlowCategoryList: StateFlow<List<Category>> = _stateFlowCategoryList.asStateFlow()


    private var noteCategoryPairList: List<NoteCategoryPair> = emptyList()
    private val _stateFlowNoteCategoryPairList =
        MutableStateFlow<List<NoteCategoryPair>>(emptyList())
    val stateFlowNoteCategoryPairList: StateFlow<List<NoteCategoryPair>> =
        _stateFlowNoteCategoryPairList.asStateFlow()

    init {
        loadNotesAndCategories()
        configureFilterNotesByText()
    }

    fun loadNotesAndCategories() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                val notes = noteRepository.getAllNotes()
                val categories = noteRepository.getAllCategories()
                val noteCategoryPairs =
                    NoteCategoryPairCreator.createNoteCategoryPairList(notes, categories)

                withContext(Dispatchers.Main) {
                    noteCategoryPairList = noteCategoryPairs
                    _stateFlowCategoryList.value = categories
                    _stateFlowNoteCategoryPairList.value = noteCategoryPairs
                }
            }
        }
    }

    fun setMainFragmentState(mainFragmentState: MainFragmentState) {
        _stateFlowMainFragment.value = mainFragmentState
    }

    fun setNoteFilterText(text: String) {
        _stateFlowNoteFilterText.value = text
    }

    fun deleteAllNotes() {
        viewModelScope.launch {
            val deferred = async(Dispatchers.IO) {
                noteRepository.deleteAllNotes()
            }
            deferred.await()
            loadNotesAndCategories()
        }
    }

    fun deleteAllCategories() {
        viewModelScope.launch {
            val deferred = async(Dispatchers.IO) {
                noteRepository.deleteAllCategories()
            }
            deferred.await()
            loadNotesAndCategories()
        }
    }

    private fun configureFilterNotesByText() {
        viewModelScope.launch {
            stateFlowNoteFilterText.collect { text ->
                if (text.isBlank()) {
                    _stateFlowNoteCategoryPairList.value = noteCategoryPairList
                } else {
                    val deferred = async(Dispatchers.Default) {
                        NoteCategoryPairCreator.filterByNoteText(text, noteCategoryPairList)
                    }
                    _stateFlowNoteCategoryPairList.value = deferred.await()
                }
            }
        }
    }

    @Suppress("UNCHECKED_CAST")
    class Factory(private val noteRepository: NoteRepository) : ViewModelProvider.Factory {

        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return MainViewModel(noteRepository) as T
        }

    }
}