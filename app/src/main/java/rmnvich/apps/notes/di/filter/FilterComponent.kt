package rmnvich.apps.notes.di.filter

import dagger.Subcomponent
import rmnvich.apps.notes.di.global.base.BaseComponent
import rmnvich.apps.notes.di.global.base.BaseComponentBuilder
import rmnvich.apps.notes.di.global.scope.PerFragment
import rmnvich.apps.notes.presentation.ui.fragment.dashboardnotes.FilterFragment

@PerFragment
@Subcomponent(modules = [FilterModule::class])
interface FilterComponent : BaseComponent<FilterFragment> {

    @Subcomponent.Builder
    interface Builder : BaseComponentBuilder<FilterComponent, FilterModule>
}