package rmnvich.apps.notes.di.trash

import dagger.Module
import dagger.Provides
import rmnvich.apps.notes.data.common.SchedulersProvider
import rmnvich.apps.notes.data.repositories.NotesRepositoryImpl
import rmnvich.apps.notes.data.repositories.datasource.Database
import rmnvich.apps.notes.di.global.base.BaseModule
import rmnvich.apps.notes.di.global.scope.PerFragment
import rmnvich.apps.notes.domain.interactors.trash.TrashInteractor
import rmnvich.apps.notes.domain.repositories.NotesRepository
import rmnvich.apps.notes.domain.utils.ViewModelFactory
import rmnvich.apps.notes.presentation.ui.adapter.trash.TrashAdapter

@Module
class TrashModule : BaseModule {

    @Provides
    fun provideViewModelFactory(interactor: TrashInteractor): ViewModelFactory {
        return ViewModelFactory(interactor)
    }

    @PerFragment
    @Provides
    fun provideInteractor(
            notesRepository: NotesRepository,
            schedulersProvider: SchedulersProvider
    ): TrashInteractor {
        return TrashInteractor(notesRepository, schedulersProvider)
    }

    @PerFragment
    @Provides
    fun provideSchedulersProvider(): SchedulersProvider {
        return SchedulersProvider()
    }

    @PerFragment
    @Provides
    fun provideNotesRepository(database: Database): NotesRepository {
        return NotesRepositoryImpl(database)
    }

    @PerFragment
    @Provides
    fun provideAdapter(): TrashAdapter {
        return TrashAdapter()
    }
}