package com.terabyte.realmnotes.application

import android.app.Application
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.terabyte.realmnotes.data.local.datastore.DataStoreRepositoryImpl
import com.terabyte.realmnotes.data.local.realm.NoteRepositoryImpl
import com.terabyte.realmnotes.di.component.AppComponent
import com.terabyte.realmnotes.di.component.DaggerAppComponent
import com.terabyte.realmnotes.domain.repository.DataStoreRepository
import com.terabyte.realmnotes.domain.repository.NoteRepository
import javax.inject.Inject

class MyApplication: Application() {

    lateinit var appComponent: AppComponent


    override fun onCreate() {
        super.onCreate()
        appComponent = DaggerAppComponent.factory().create(this)
    }


    override fun onTerminate() {
        super.onTerminate()
        appComponent.noteRepository().close()
    }
}