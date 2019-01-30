package rmnvich.apps.notes.domain.utils

import android.app.Application
import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import rmnvich.apps.notes.domain.interactors.addeditnote.AddEditNoteInteractor
import rmnvich.apps.notes.domain.interactors.dashboardnotes.DashboardNotesInteractor
import rmnvich.apps.notes.domain.interactors.dashboardtags.DashboardTagsInteractor
import rmnvich.apps.notes.domain.interactors.trash.TrashInteractor
import rmnvich.apps.notes.presentation.ui.activity.addeditnote.AddEditNoteViewModel
import rmnvich.apps.notes.presentation.ui.fragment.dashboardnotes.DashboardNotesViewModel
import rmnvich.apps.notes.presentation.ui.fragment.dashboardtags.DashboardTagsViewModel
import rmnvich.apps.notes.presentation.ui.fragment.trash.TrashViewModel

class ViewModelFactory : ViewModelProvider.NewInstanceFactory {

    private var isFavoriteNotes = false

    private lateinit var mApplicationContext: Application

    private lateinit var mDashboardNotesInteractor: DashboardNotesInteractor

    private lateinit var mDashboardTagsInteractor: DashboardTagsInteractor

    private lateinit var mTrashInteractor: TrashInteractor

    private lateinit var mAddEditNoteInteractor: AddEditNoteInteractor

    constructor(dashboardNotesInteractor: DashboardNotesInteractor, isFavoriteNotes: Boolean) : super() {
        this.isFavoriteNotes = isFavoriteNotes
        this.mDashboardNotesInteractor = dashboardNotesInteractor
    }

    constructor(dashboardTagsInteractor: DashboardTagsInteractor) : super() {
        this.mDashboardTagsInteractor = dashboardTagsInteractor
    }

    constructor(trashInteractor: TrashInteractor) : super() {
        this.mTrashInteractor = trashInteractor
    }

    constructor(addEditNoteInteractor: AddEditNoteInteractor, applicationContext: Application) : super() {
        this.mAddEditNoteInteractor = addEditNoteInteractor
        this.mApplicationContext = applicationContext
    }

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
            else -> throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
        }
    }

}