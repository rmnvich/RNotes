package rmnvich.apps.notes.di.tags

import dagger.Subcomponent
import rmnvich.apps.notes.di.global.base.BaseComponent
import rmnvich.apps.notes.di.global.base.BaseComponentBuilder
import rmnvich.apps.notes.di.global.scope.PerFragment
import rmnvich.apps.notes.presentation.ui.fragment.tags.DashboardTagsFragment

@PerFragment
@Subcomponent(modules = [DashboardTagsModule::class])
interface DashboardTagsComponent : BaseComponent<DashboardTagsFragment> {

    @Subcomponent.Builder
    interface Builder : BaseComponentBuilder<DashboardTagsComponent, DashboardTagsModule>
}