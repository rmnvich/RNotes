package rmnvich.apps.notes.di.trash

import dagger.Subcomponent
import rmnvich.apps.notes.di.global.base.BaseComponent
import rmnvich.apps.notes.di.global.base.BaseComponentBuilder
import rmnvich.apps.notes.di.global.scope.PerFragment
import rmnvich.apps.notes.presentation.ui.fragment.trash.TrashFragment

@PerFragment
@Subcomponent(modules = [TrashModule::class])
interface TrashComponent : BaseComponent<TrashFragment> {

    @Subcomponent.Builder
    interface Builder : BaseComponentBuilder<TrashComponent, TrashModule>
}