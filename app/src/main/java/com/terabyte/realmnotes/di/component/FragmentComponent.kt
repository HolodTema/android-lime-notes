package com.terabyte.realmnotes.di.component

import com.terabyte.realmnotes.di.scope.FragmentScope
import com.terabyte.realmnotes.ui.fragment.SettingsFragment
import dagger.Subcomponent

@Subcomponent
@FragmentScope
interface FragmentComponent {

    fun inject(fragment: SettingsFragment)

    @Subcomponent.Factory
    interface Factory {
        fun create(): FragmentComponent
    }
}