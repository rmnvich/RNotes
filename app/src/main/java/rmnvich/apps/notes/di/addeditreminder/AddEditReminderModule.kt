package rmnvich.apps.notes.di.addeditreminder

import android.app.Application
import android.content.Context
import dagger.Module
import dagger.Provides
import rmnvich.apps.notes.data.common.SchedulersProvider
import rmnvich.apps.notes.data.repositories.RemindersRepositoryImpl
import rmnvich.apps.notes.data.repositories.datasource.Database
import rmnvich.apps.notes.di.global.base.BaseModule
import rmnvich.apps.notes.di.global.scope.PerFragment
import rmnvich.apps.notes.domain.interactors.addeditreminder.AddEditReminderInteractor
import rmnvich.apps.notes.domain.repositories.RemindersRepository
import rmnvich.apps.notes.domain.utils.ViewModelFactory

@Module
class AddEditReminderModule(private val application: Application) : BaseModule {

    @PerFragment
    @Provides
    fun provideViewModelFactory(interactor: AddEditReminderInteractor): ViewModelFactory {
        return ViewModelFactory(interactor, application)
    }

    @PerFragment
    @Provides
    fun provideAddEditReminderInteractor(
        remindersRepository: RemindersRepository,
        schedulersProvider: SchedulersProvider
    ): AddEditReminderInteractor {
        return AddEditReminderInteractor(remindersRepository, schedulersProvider)
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
}