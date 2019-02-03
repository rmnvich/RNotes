package rmnvich.apps.notes.di.dashboardnotes.search

import dagger.Module
import dagger.Provides
import rmnvich.apps.notes.di.global.base.BaseModule
import rmnvich.apps.notes.di.global.scope.PerFragment
import rmnvich.apps.notes.presentation.ui.adapter.dashboard.NotesAdapter

@Module
class SearchModule : BaseModule {

    @PerFragment
    @Provides
    fun provideNotesAdapter(): NotesAdapter {
        return NotesAdapter()
    }
}