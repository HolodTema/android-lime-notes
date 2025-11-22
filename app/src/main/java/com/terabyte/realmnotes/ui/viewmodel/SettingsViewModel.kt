package com.terabyte.realmnotes.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.terabyte.realmnotes.domain.repository.DataStoreRepository
import kotlinx.coroutines.launch

class SettingsViewModel(private val dataStoreRepository: DataStoreRepository): ViewModel() {

    val flowIsDarkTheme = dataStoreRepository.isDarkTheme

    fun saveIsDarkTheme(isDarkTheme: Boolean) {
        viewModelScope.launch {
            dataStoreRepository.saveIsDarkTheme(isDarkTheme)
        }
    }


    @Suppress("UNCHECKED_CAST")
    class Factory(private val dataStoreRepository: DataStoreRepository): ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return SettingsViewModel(dataStoreRepository) as T
        }
    }
}