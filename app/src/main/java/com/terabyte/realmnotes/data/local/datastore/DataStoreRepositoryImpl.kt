package com.terabyte.realmnotes.data.local.datastore

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import com.terabyte.realmnotes.domain.repository.DataStoreRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class DataStoreRepositoryImpl private constructor(private val dataStore: DataStore<Preferences>) :
    DataStoreRepository {

    override val isDarkTheme: Flow<Boolean> = dataStore.data.map { preferences ->
        preferences[KEY_IS_DARK_THEME] ?: false
    }

    override suspend fun saveIsDarkTheme(isDarkTheme: Boolean) {
        dataStore.edit { preferences ->
            preferences[KEY_IS_DARK_THEME] = isDarkTheme
        }
    }

    companion object {
        const val DATA_STORE_NAME = "PreferencesDataStore"

        private val KEY_IS_DARK_THEME = booleanPreferencesKey("keyIsDarkTheme")

        private lateinit var instance: DataStoreRepositoryImpl

        fun getInstance(dataStore: DataStore<Preferences>): DataStoreRepositoryImpl {
            if (::instance.isInitialized) {
                return instance
            }
            instance = DataStoreRepositoryImpl(dataStore)
            return instance
        }
    }
}