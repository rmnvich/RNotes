package rmnvich.apps.notes.di.addeditreminder

import dagger.Subcomponent
import rmnvich.apps.notes.di.global.base.BaseComponent
import rmnvich.apps.notes.di.global.base.BaseComponentBuilder
import rmnvich.apps.notes.di.global.scope.PerFragment
import rmnvich.apps.notes.presentation.ui.activity.addeditreminder.AddEditReminderActivity

@PerFragment
@Subcomponent(modules = [AddEditReminderModule::class])
interface AddEditReminderComponent : BaseComponent<AddEditReminderActivity> {

    @Subcomponent.Builder
    interface Builder : BaseComponentBuilder<AddEditReminderComponent, AddEditReminderModule>
}