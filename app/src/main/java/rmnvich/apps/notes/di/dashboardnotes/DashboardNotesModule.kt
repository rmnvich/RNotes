package rmnvich.apps.notes.di.dashboardnotes

import android.app.Application
import dagger.Module
import dagger.Provides
import rmnvich.apps.notes.data.common.SchedulersProvider
import rmnvich.apps.notes.data.repositories.NotesRepositoryImpl
import rmnvich.apps.notes.data.repositories.datasource.Database
import rmnvich.apps.notes.di.global.base.BaseModule
import rmnvich.apps.notes.di.global.scope.PerFragment
import rmnvich.apps.notes.domain.interactors.dashboardnotes.DashboardNotesInteractor
import rmnvich.apps.notes.domain.repositories.NotesRepository
import rmnvich.apps.notes.domain.utils.ViewModelFactory
import rmnvich.apps.notes.presentation.ui.adapter.dashboard.NotesAdapter

@Module
class DashboardNotesModule(private val application: Application) : BaseModule {

    @PerFragment
    @Provides
    fun provideViewModelFactory(interactor: DashboardNotesInteractor): ViewModelFactory {
        return ViewModelFactory(application, interactor)
    }

    @PerFragment
    @Provides
    fun provideInteractor(
        notesRepository: NotesRepository,
        schedulersProvider: SchedulersProvider
    ): DashboardNotesInteractor {
        return DashboardNotesInteractor(notesRepository, schedulersProvider)
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
    fun provideApadter(): NotesAdapter {
        return NotesAdapter()
    }
}