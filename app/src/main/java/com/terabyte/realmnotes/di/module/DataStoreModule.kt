package com.terabyte.realmnotes.di.module

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.terabyte.realmnotes.data.local.datastore.DataStoreRepositoryImpl
import com.terabyte.realmnotes.di.qualifier.ApplicationContext
import com.terabyte.realmnotes.domain.repository.DataStoreRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

private val Context.dataStore
        by preferencesDataStore(DataStoreRepositoryImpl.DATA_STORE_NAME)


@Module
interface DataStoreModule {

    @Binds
    @Singleton
    fun bindDataStoreRepositoryImpl(dataStoreRepositoryImpl: DataStoreRepositoryImpl): DataStoreRepository

    companion object {
        @Provides
        @Singleton
        fun provideDataStore(@ApplicationContext context: Context): DataStore<Preferences> {
            return context.dataStore
        }
    }
}