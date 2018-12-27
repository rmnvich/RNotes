package rmnvich.apps.notes.di.addeditnote

import dagger.Subcomponent
import rmnvich.apps.notes.di.global.base.BaseComponent
import rmnvich.apps.notes.di.global.base.BaseComponentBuilder
import rmnvich.apps.notes.di.global.scope.PerFragment
import rmnvich.apps.notes.presentation.ui.activity.addeditnote.AddEditNoteActivity

@PerFragment
@Subcomponent(modules = [AddEditNoteModule::class])
interface AddEditNoteComponent : BaseComponent<AddEditNoteActivity> {

    @Subcomponent.Builder
    interface Builder : BaseComponentBuilder<AddEditNoteComponent, AddEditNoteModule>
}