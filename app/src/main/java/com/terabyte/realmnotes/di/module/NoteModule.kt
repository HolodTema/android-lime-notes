package com.terabyte.realmnotes.di.module

import com.terabyte.realmnotes.data.local.realm.CategoryRealmObject
import com.terabyte.realmnotes.data.local.realm.NoteRealmObject
import com.terabyte.realmnotes.data.local.realm.NoteRepositoryImpl
import com.terabyte.realmnotes.domain.repository.NoteRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import io.realm.kotlin.Realm
import io.realm.kotlin.RealmConfiguration
import javax.inject.Singleton

@Module
interface NoteModule {

    @Binds
    @Singleton
    fun bindNoteRepositoryImpl(noteRepositoryImpl: NoteRepositoryImpl): NoteRepository

    companion object {

        @Provides
        @Singleton
        fun provideRealmConfiguration(): RealmConfiguration {
            return RealmConfiguration.Builder(
                    schema = setOf(NoteRealmObject::class, CategoryRealmObject::class)
                )
                .deleteRealmIfMigrationNeeded() //for development
                .build()
        }

        @Provides
        @Singleton
        fun provideRealm(realmConfiguration: RealmConfiguration): Realm {
            return Realm.open(realmConfiguration)
        }
    }
}