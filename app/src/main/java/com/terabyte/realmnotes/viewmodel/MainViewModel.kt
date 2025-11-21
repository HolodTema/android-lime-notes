package com.terabyte.realmnotes.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.terabyte.realmnotes.R
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

enum class MainFragmentState {
    FRAGMENT_NOTE_LIST,
    FRAGMENT_CATEGORY_LIST,
    FRAGMENT_SETTINGS
}

class MainViewModel: ViewModel() {

    private val _stateFlowMainFragment = MutableStateFlow(MainFragmentState.FRAGMENT_NOTE_LIST)
    val stateFlowMainFragment: StateFlow<MainFragmentState> = _stateFlowMainFragment.asStateFlow()

    private val _stateFlowNavViewExpanded = MutableStateFlow<Boolean>(false)
    val stateFlowNavViewExpanded: StateFlow<Boolean> = _stateFlowNavViewExpanded.asStateFlow()

    fun inverseNavViewExpanded() {
        val isExpanded = _stateFlowNavViewExpanded.value
        _stateFlowNavViewExpanded.value = !isExpanded
    }

    fun setMainFragmentState(mainFragmentState: MainFragmentState) {
        _stateFlowMainFragment.value = mainFragmentState
    }
}