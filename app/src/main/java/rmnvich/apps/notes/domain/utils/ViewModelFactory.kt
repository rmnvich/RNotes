package rmnvich.apps.notes.domain.utils

import android.app.Application
import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import rmnvich.apps.notes.domain.interactors.addeditnote.AddEditNoteInteractor
import rmnvich.apps.notes.domain.interactors.addeditreminder.AddEditReminderInteractor
import rmnvich.apps.notes.domain.interactors.dashboardnotes.DashboardNotesInteractor
import rmnvich.apps.notes.domain.interactors.dashboardreminders.DashboardRemindersInteractor
import rmnvich.apps.notes.domain.interactors.dashboardtags.DashboardTagsInteractor
import rmnvich.apps.notes.domain.interactors.trash.TrashInteractor
import rmnvich.apps.notes.presentation.ui.activity.addeditnote.AddEditNoteViewModel
import rmnvich.apps.notes.presentation.ui.activity.addeditreminder.AddEditReminderViewModel
import rmnvich.apps.notes.presentation.ui.fragment.notes.DashboardNotesViewModel
import rmnvich.apps.notes.presentation.ui.fragment.reminders.DashboardRemindersViewModel
import rmnvich.apps.notes.presentation.ui.fragment.tags.DashboardTagsViewModel
import rmnvich.apps.notes.presentation.ui.fragment.trash.TrashViewModel

class ViewModelFactory : ViewModelProvider.NewInstanceFactory {

    private var isFavoriteNotes = false

    private var isCompletedReminders = false

    private lateinit var mApplicationContext: Application

    private lateinit var mDashboardNotesInteractor: DashboardNotesInteractor

    private lateinit var mDashboardTagsInteractor: DashboardTagsInteractor

    private lateinit var mDashboardRemindersInteractor: DashboardRemindersInteractor

    private lateinit var mTrashInteractor: TrashInteractor

    private lateinit var mAddEditNoteInteractor: AddEditNoteInteractor

    private lateinit var mAddEditReminderInteractor: AddEditReminderInteractor

    constructor(dashboardNotesInteractor: DashboardNotesInteractor, isFavoriteNotes: Boolean) : super() {
        this.isFavoriteNotes = isFavoriteNotes
        this.mDashboardNotesInteractor = dashboardNotesInteractor
    }

    constructor(dashboardTagsInteractor: DashboardTagsInteractor) : super() {
        this.mDashboardTagsInteractor = dashboardTagsInteractor
    }

    constructor(dashboardRemindersInteractor: DashboardRemindersInteractor, isCompleted: Boolean) : super() {
        this.mDashboardRemindersInteractor = dashboardRemindersInteractor
        this.isCompletedReminders = isCompleted
    }

    constructor(trashInteractor: TrashInteractor) : super() {
        this.mTrashInteractor = trashInteractor
    }

    constructor(addEditNoteInteractor: AddEditNoteInteractor, applicationContext: Application) : super() {
        this.mAddEditNoteInteractor = addEditNoteInteractor
        this.mApplicationContext = applicationContext
    }

    constructor(addEditReminderInteractor: AddEditReminderInteractor): super() {
        this.mAddEditReminderInteractor = addEditReminderInteractor
    }

    @Suppress("UNCHECKED_CAST")
    override fun <VM : ViewModel?> create(modelClass: Class<VM>): VM {
        return when {
            modelClass.isAssignableFrom(DashboardNotesViewModel::class.java) ->
                DashboardNotesViewModel(mDashboardNotesInteractor, isFavoriteNotes) as VM
            modelClass.isAssignableFrom(AddEditNoteViewModel::class.java) ->
                AddEditNoteViewModel(mApplicationContext, mAddEditNoteInteractor) as VM
            modelClass.isAssignableFrom(DashboardTagsViewModel::class.java) ->
                DashboardTagsViewModel(mDashboardTagsInteractor) as VM
            modelClass.isAssignableFrom(TrashViewModel::class.java) ->
                TrashViewModel(mTrashInteractor) as VM
            modelClass.isAssignableFrom(DashboardRemindersViewModel::class.java) ->
                DashboardRemindersViewModel(mDashboardRemindersInteractor, isCompletedReminders) as VM
            modelClass.isAssignableFrom(AddEditReminderViewModel::class.java) ->
                AddEditReminderViewModel(mAddEditReminderInteractor) as VM
            else -> throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
        }
    }

}