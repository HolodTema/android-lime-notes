package com.terabyte.realmnotes.di.component

import com.terabyte.realmnotes.di.module.ActivityModule
import com.terabyte.realmnotes.di.scope.ActivityScope
import com.terabyte.realmnotes.ui.activity.CategoryDetailsActivity
import com.terabyte.realmnotes.ui.activity.MainActivity
import com.terabyte.realmnotes.ui.activity.NoteDetailsActivity
import dagger.Subcomponent

@Subcomponent(modules = [ActivityModule::class])
@ActivityScope
interface ActivityComponent {

    fun inject(activity: CategoryDetailsActivity)

    fun inject(activity: MainActivity)

    fun inject(activity: NoteDetailsActivity)

    fun fragmentComponentFactory(): FragmentComponent.Factory

    @Subcomponent.Factory
    interface Factory {
        fun create(): ActivityComponent
    }
}