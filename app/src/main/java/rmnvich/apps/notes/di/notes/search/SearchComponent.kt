package rmnvich.apps.notes.di.notes.search

import dagger.Subcomponent
import rmnvich.apps.notes.di.global.base.BaseComponent
import rmnvich.apps.notes.di.global.base.BaseComponentBuilder
import rmnvich.apps.notes.di.global.scope.PerFragment
import rmnvich.apps.notes.presentation.ui.fragment.notes.SearchFragment

@PerFragment
@Subcomponent(modules = [SearchModule::class])
interface SearchComponent : BaseComponent<SearchFragment> {

    @Subcomponent.Builder
    interface Builder : BaseComponentBuilder<SearchComponent, SearchModule>
}