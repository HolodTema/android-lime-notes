package com.terabyte.realmnotes.di.component

import android.app.Application
import com.terabyte.realmnotes.di.module.AppModule
import com.terabyte.realmnotes.di.module.DataStoreModule
import com.terabyte.realmnotes.di.module.NoteModule
import com.terabyte.realmnotes.domain.repository.NoteRepository
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(
    modules = [AppModule::class, DataStoreModule::class, NoteModule::class]
)
interface AppComponent {

    fun noteRepository(): NoteRepository

    fun activityComponentFactory(): ActivityComponent.Factory

    @Component.Factory
    interface Factory {
        fun create(@BindsInstance application: Application): AppComponent
    }
}