package rmnvich.apps.notes.di.dashboard

import dagger.Subcomponent
import rmnvich.apps.notes.di.global.base.BaseComponent
import rmnvich.apps.notes.di.global.base.BaseComponentBuilder
import rmnvich.apps.notes.di.global.scope.PerFragment
import rmnvich.apps.notes.presentation.ui.fragment.dashboard.DashboardFragment

@PerFragment
@Subcomponent(modules = [DashboardModule::class])
interface DashboardComponent : BaseComponent<DashboardFragment> {

    @Subcomponent.Builder
    interface Builder : BaseComponentBuilder<DashboardComponent, DashboardModule>
}