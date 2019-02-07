package rmnvich.apps.notes.di.reminders

import dagger.Subcomponent
import rmnvich.apps.notes.di.global.base.BaseComponent
import rmnvich.apps.notes.di.global.base.BaseComponentBuilder
import rmnvich.apps.notes.di.global.scope.PerFragment
import rmnvich.apps.notes.presentation.ui.fragment.reminders.DashboardRemindersFragment

@PerFragment
@Subcomponent(modules = [DashboardRemindersModule::class])
interface DashboardRemindersComponent : BaseComponent<DashboardRemindersFragment> {

    @Subcomponent.Builder
    interface Builder : BaseComponentBuilder<DashboardRemindersComponent, DashboardRemindersModule>
}