package rmnvich.apps.notes.di.dashboardnotes.filter

import dagger.Module
import dagger.Provides
import rmnvich.apps.notes.di.global.base.BaseModule
import rmnvich.apps.notes.di.global.scope.PerFragment
import rmnvich.apps.notes.presentation.ui.adapter.filter.CheckableCirclesAdapter
import rmnvich.apps.notes.presentation.ui.adapter.filter.CheckableTagsAdapter

@Module
class FilterModule : BaseModule {

    @PerFragment
    @Provides
    fun provideTagsAdapter(): CheckableTagsAdapter {
        return CheckableTagsAdapter()
    }

    @PerFragment
    @Provides
    fun provideCirclesAdapter(): CheckableCirclesAdapter {
        return CheckableCirclesAdapter()
    }
}