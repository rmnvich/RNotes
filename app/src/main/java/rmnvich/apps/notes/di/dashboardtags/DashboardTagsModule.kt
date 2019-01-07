package rmnvich.apps.notes.di.dashboardtags

import dagger.Module
import dagger.Provides
import rmnvich.apps.notes.data.common.SchedulersProvider
import rmnvich.apps.notes.data.repositories.TagsRepositoryImpl
import rmnvich.apps.notes.data.repositories.datasource.Database
import rmnvich.apps.notes.di.global.base.BaseModule
import rmnvich.apps.notes.di.global.scope.PerFragment
import rmnvich.apps.notes.domain.interactors.dashboardtags.DashboardTagsInteractor
import rmnvich.apps.notes.domain.repositories.TagsRepository
import rmnvich.apps.notes.domain.utils.ViewModelFactory

@Module
class DashboardTagsModule : BaseModule {

    @Provides
    fun provideViewModelFactory(interactor: DashboardTagsInteractor): ViewModelFactory {
        return ViewModelFactory(interactor)
    }

    @PerFragment
    @Provides
    fun provideInteractor(
            tagsRepository: TagsRepository,
            schedulersProvider: SchedulersProvider
    ): DashboardTagsInteractor {
        return DashboardTagsInteractor(tagsRepository, schedulersProvider)
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
}