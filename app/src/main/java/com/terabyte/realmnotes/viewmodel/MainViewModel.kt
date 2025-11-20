package com.terabyte.realmnotes.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.terabyte.realmnotes.R

class MainViewModel: ViewModel() {

    private val _liveDataFragmentMenuItemId = MutableLiveData<Int>(R.id.menu_item_note_list)
    val liveDataFragmentMenuItemId: LiveData<Int> = _liveDataFragmentMenuItemId

    private val _liveDataNavViewExpanded = MutableLiveData<Boolean>(false)
    val liveDataNavViewExpanded: LiveData<Boolean> = _liveDataNavViewExpanded


    fun setNavViewExpanded(isExpanded: Boolean) {
        _liveDataNavViewExpanded.value = isExpanded
    }
}