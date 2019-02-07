package rmnvich.apps.notes.di.reminders

import dagger.Module
import dagger.Provides
import rmnvich.apps.notes.data.common.SchedulersProvider
import rmnvich.apps.notes.data.repositories.RemindersRepositoryImpl
import rmnvich.apps.notes.data.repositories.datasource.Database
import rmnvich.apps.notes.di.global.base.BaseModule
import rmnvich.apps.notes.di.global.scope.PerFragment
import rmnvich.apps.notes.domain.interactors.dashboardreminders.DashboardRemindersInteractor
import rmnvich.apps.notes.domain.repositories.RemindersRepository
import rmnvich.apps.notes.domain.utils.ViewModelFactory
import rmnvich.apps.notes.presentation.ui.adapter.reminder.RemindersAdapter

@Module
class DashboardRemindersModule(private val isCompletedReminders: Boolean) : BaseModule {

    @Provides
    fun provideViewModelFactory(interactor: DashboardRemindersInteractor): ViewModelFactory {
        return ViewModelFactory(interactor, isCompletedReminders)
    }

    @PerFragment
    @Provides
    fun provideInteractor(
        remindersRepository: RemindersRepository,
        schedulersProvider: SchedulersProvider
    ): DashboardRemindersInteractor {
        return DashboardRemindersInteractor(remindersRepository, schedulersProvider)
    }

    @PerFragment
    @Provides
    fun provideSchedulersProvider(): SchedulersProvider {
        return SchedulersProvider()
    }

    @PerFragment
    @Provides
    fun provideRemindersRepository(database: Database): RemindersRepository {
        return RemindersRepositoryImpl(database)
    }

    @PerFragment
    @Provides
    fun provideRemindersAdapter(): RemindersAdapter {
        return RemindersAdapter()
    }
}