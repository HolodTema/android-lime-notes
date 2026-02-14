package com.terabyte.realmnotes.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.terabyte.realmnotes.domain.repository.DataStoreRepository
import kotlinx.coroutines.launch
import javax.inject.Inject

class SettingsViewModel @Inject constructor(private val dataStoreRepository: DataStoreRepository): ViewModel() {

    val flowIsDarkTheme = dataStoreRepository.isDarkTheme

    fun saveIsDarkTheme(isDarkTheme: Boolean) {
        viewModelScope.launch {
            dataStoreRepository.saveIsDarkTheme(isDarkTheme)
        }
    }
}