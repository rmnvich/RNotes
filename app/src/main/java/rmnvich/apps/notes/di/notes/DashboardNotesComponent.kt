package rmnvich.apps.notes.di.notes

import dagger.Subcomponent
import rmnvich.apps.notes.di.global.base.BaseComponent
import rmnvich.apps.notes.di.global.base.BaseComponentBuilder
import rmnvich.apps.notes.di.global.scope.PerFragment
import rmnvich.apps.notes.presentation.ui.fragment.notes.DashboardNotesFragment

@PerFragment
@Subcomponent(modules = [DashboardNotesModule::class])
interface DashboardNotesComponent : BaseComponent<DashboardNotesFragment> {

    @Subcomponent.Builder
    interface Builder : BaseComponentBuilder<DashboardNotesComponent, DashboardNotesModule>
}