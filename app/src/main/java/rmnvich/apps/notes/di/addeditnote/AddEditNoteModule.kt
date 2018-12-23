package rmnvich.apps.notes.di.addeditnote

import android.app.Application
import dagger.Module
import dagger.Provides
import rmnvich.apps.notes.data.common.SchedulersProvider
import rmnvich.apps.notes.data.repositories.NotesRepositoryImpl
import rmnvich.apps.notes.data.repositories.TagsRepositoryImpl
import rmnvich.apps.notes.data.repositories.datasource.Database
import rmnvich.apps.notes.di.global.base.BaseModule
import rmnvich.apps.notes.di.global.scope.PerFragment
import rmnvich.apps.notes.domain.interactors.addeditnote.AddEditNoteInteractor
import rmnvich.apps.notes.domain.repositories.NotesRepository
import rmnvich.apps.notes.domain.repositories.TagsRepository
import rmnvich.apps.notes.presentation.utils.ViewModelFactory

@Module
class AddEditNoteModule(private val application: Application) : BaseModule {

    @PerFragment
    @Provides
    fun provideViewModelFactory(interactor: AddEditNoteInteractor): ViewModelFactory {
        return ViewModelFactory(application, interactor)
    }

    @PerFragment
    @Provides
    fun provideInteractor(notesRepository: NotesRepository,
                          tagsRepository: TagsRepository,
                          schedulersProvider: SchedulersProvider): AddEditNoteInteractor {
        return AddEditNoteInteractor(notesRepository, tagsRepository, schedulersProvider)
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
    fun provideTagsRepository(database: Database): TagsRepository {
        return TagsRepositoryImpl(database)
    }
}