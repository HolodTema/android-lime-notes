package com.terabyte.realmnotes.application

import android.app.Application
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.terabyte.realmnotes.data.local.datastore.DataStoreRepositoryImpl
import com.terabyte.realmnotes.domain.repository.DataStoreRepository

class MyApplication: Application() {
    private val dataStore: DataStore<Preferences> by preferencesDataStore(DataStoreRepositoryImpl.DATA_STORE_NAME)

    val dataStoreRepository: DataStoreRepository by lazy {
        DataStoreRepositoryImpl.getInstance(dataStore)
    }
}