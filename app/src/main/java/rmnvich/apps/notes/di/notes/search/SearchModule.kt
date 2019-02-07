package rmnvich.apps.notes.di.notes.search

import dagger.Module
import dagger.Provides
import rmnvich.apps.notes.di.global.base.BaseModule
import rmnvich.apps.notes.di.global.scope.PerFragment
import rmnvich.apps.notes.presentation.ui.adapter.note.NotesAdapter

@Module
class SearchModule : BaseModule {

    @PerFragment
    @Provides
    fun provideNotesAdapter(): NotesAdapter {
        return NotesAdapter()
    }
}