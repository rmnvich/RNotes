package rmnvich.apps.notes.di.addeditnote

import dagger.Subcomponent
import rmnvich.apps.notes.di.global.base.BaseComponent
import rmnvich.apps.notes.di.global.base.BaseComponentBuilder
import rmnvich.apps.notes.di.global.scope.PerFragment
import rmnvich.apps.notes.presentation.ui.fragment.addeditnote.AddEditNoteFragment

@PerFragment
@Subcomponent(modules = [AddEditNoteModule::class])
interface AddEditNoteComponent : BaseComponent<AddEditNoteFragment> {

    @Subcomponent.Builder
    interface Builder : BaseComponentBuilder<AddEditNoteComponent, AddEditNoteModule>
}