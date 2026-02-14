package com.terabyte.realmnotes.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.terabyte.realmnotes.domain.model.Category
import com.terabyte.realmnotes.domain.repository.NoteRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


enum class CategoryDetailsState {
    ADD_CATEGORY,
    UPDATE_CATEGORY
}

class CategoryDetailsViewModel @Inject constructor(private val noteRepository: NoteRepository) : ViewModel() {

    private val _stateFlowCategory = MutableStateFlow(Category())
    val stateFlowCategory: StateFlow<Category> = _stateFlowCategory.asStateFlow()

    private val _stateFlowCategoryDetails = MutableStateFlow(CategoryDetailsState.ADD_CATEGORY)
    val stateFlowCategoryDetails: StateFlow<CategoryDetailsState> =
        _stateFlowCategoryDetails.asStateFlow()

    fun setStateUpdate(category: Category) {
        _stateFlowCategory.value = category
        _stateFlowCategoryDetails.value = CategoryDetailsState.UPDATE_CATEGORY
    }

    fun saveCategory(categorySavedListener: () -> Unit) {
        viewModelScope.launch {
            val deferred = async(Dispatchers.IO) {
                when (stateFlowCategoryDetails.value) {
                    CategoryDetailsState.ADD_CATEGORY -> {
                        noteRepository.addCategory(stateFlowCategory.value)
                    }

                    CategoryDetailsState.UPDATE_CATEGORY -> {
                        noteRepository.updateCategory(stateFlowCategory.value)
                    }
                }
            }

            deferred.await()
            categorySavedListener()
        }
    }

    fun deleteCategory(categoryDeletedListener: () -> Unit) {
        val categoryId = stateFlowCategory.value.id
        if (categoryId == null) {
            return
        }

        viewModelScope.launch {
            val deferred = async(Dispatchers.IO) {
                noteRepository.deleteCategory(categoryId)
                noteRepository.removeCategoryIdFromAllNotes(categoryId)
            }

            deferred.await()
            categoryDeletedListener()
        }
    }

    fun updateCategoryName(name: String) {
        _stateFlowCategory.value.name = name
    }

    fun updateCategoryColor(color: Int) {
        _stateFlowCategory.value.color = color
    }
}