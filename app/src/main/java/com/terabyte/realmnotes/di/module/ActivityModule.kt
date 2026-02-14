package com.terabyte.realmnotes.di.module

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.terabyte.realmnotes.di.mapkey.ViewModelKey
import com.terabyte.realmnotes.ui.viewmodel.CategoryDetailsViewModel
import com.terabyte.realmnotes.ui.viewmodel.MainViewModel
import com.terabyte.realmnotes.ui.viewmodel.NoteDetailsViewModel
import com.terabyte.realmnotes.ui.viewmodel.SettingsViewModel
import com.terabyte.realmnotes.ui.viewmodel.ViewModelFactory
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap


@Module
interface ActivityModule {

    @Binds
    fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(MainViewModel::class)
    fun bindMainViewModel(viewModel: MainViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(CategoryDetailsViewModel::class)
    fun bindCategoryDetailsViewModel(viewModel: CategoryDetailsViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(NoteDetailsViewModel::class)
    fun bindNoteDetailsViewModel(viewModel: NoteDetailsViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(SettingsViewModel::class)
    fun bindSettingsViewModel(viewModel: SettingsViewModel): ViewModel

    companion object {

    }
}