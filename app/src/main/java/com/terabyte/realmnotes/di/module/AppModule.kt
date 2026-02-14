package com.terabyte.realmnotes.di.module

import android.app.Application
import android.content.Context
import com.terabyte.realmnotes.di.qualifier.ApplicationContext
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
interface AppModule {

    companion object {

        @Provides
        @Singleton
        @ApplicationContext
        fun provideApplicationContext(application: Application): Context {
            return application.applicationContext
        }

    }
}