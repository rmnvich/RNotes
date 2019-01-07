package rmnvich.apps.notes.domain.utils

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import rmnvich.apps.notes.domain.interactors.addeditnote.AddEditNoteInteractor
import rmnvich.apps.notes.domain.interactors.dashboardnotes.DashboardNotesInteractor
import rmnvich.apps.notes.domain.interactors.dashboardtags.DashboardTagsInteractor
import rmnvich.apps.notes.domain.interactors.trash.TrashInteractor
import rmnvich.apps.notes.presentation.ui.activity.addeditnote.AddEditNoteViewModel
import rmnvich.apps.notes.presentation.ui.fragment.dashboardnotes.DashboardNotesViewModel
import rmnvich.apps.notes.presentation.ui.fragment.dashboardtags.DashboardTagsViewModel

class ViewModelFactory : ViewModelProvider.NewInstanceFactory {

    private var isFavoriteNotes = false

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

    constructor(addEditNoteInteractor: AddEditNoteInteractor) : super() {
        this.mAddEditNoteInteractor = addEditNoteInteractor
    }

    override fun <VM : ViewModel?> create(modelClass: Class<VM>): VM {
        return when {
            modelClass.isAssignableFrom(DashboardNotesViewModel::class.java) ->
                DashboardNotesViewModel(mDashboardNotesInteractor, isFavoriteNotes) as VM
            modelClass.isAssignableFrom(AddEditNoteViewModel::class.java) ->
                AddEditNoteViewModel(mAddEditNoteInteractor) as VM
            modelClass.isAssignableFrom(DashboardTagsViewModel::class.java) ->
                DashboardTagsViewModel(mDashboardTagsInteractor) as VM
            else -> throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
        }
    }

}