package rmnvich.apps.notes.di.dashboardnotes

import dagger.Module
import dagger.Provides
import rmnvich.apps.notes.data.common.SchedulersProvider
import rmnvich.apps.notes.data.repositories.NotesRepositoryImpl
import rmnvich.apps.notes.data.repositories.TagsRepositoryImpl
import rmnvich.apps.notes.data.repositories.datasource.Database
import rmnvich.apps.notes.di.global.base.BaseModule
import rmnvich.apps.notes.di.global.scope.PerFragment
import rmnvich.apps.notes.domain.interactors.dashboardnotes.DashboardNotesInteractor
import rmnvich.apps.notes.domain.repositories.NotesRepository
import rmnvich.apps.notes.domain.repositories.TagsRepository
import rmnvich.apps.notes.domain.utils.ViewModelFactory
import rmnvich.apps.notes.presentation.ui.adapter.filter.CheckableCirclesAdapter
import rmnvich.apps.notes.presentation.ui.adapter.filter.CheckableTagsAdapter
import rmnvich.apps.notes.presentation.ui.adapter.dashboard.NotesAdapter

@Module
class DashboardNotesModule(private val isFavoriteNotes: Boolean) : BaseModule {

    @Provides
    fun provideViewModelFactory(interactor: DashboardNotesInteractor): ViewModelFactory {
        return ViewModelFactory(interactor, isFavoriteNotes)
    }

    @PerFragment
    @Provides
    fun provideInteractor(
        notesRepository: NotesRepository,
        tagsRepository: TagsRepository,
        schedulersProvider: SchedulersProvider
    ): DashboardNotesInteractor {
        return DashboardNotesInteractor(notesRepository, tagsRepository, schedulersProvider)
    }

    @PerFragment
    @Provides
    fun provideSchedulersProvider(): SchedulersProvider {
        return SchedulersProvider()
    }

    @PerFragment
    @Provides
    fun provideTagsRepository(database: Database): TagsRepository {
        return TagsRepositoryImpl(database)
    }

    @PerFragment
    @Provides
    fun provideNotesRepository(database: Database): NotesRepository {
        return NotesRepositoryImpl(database)
    }

    @PerFragment
    @Provides
    fun provideNotesAdapter(): NotesAdapter {
        return NotesAdapter()
    }
}