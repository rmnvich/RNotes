package rmnvich.apps.notes.presentation.utils

import android.app.Application
import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import rmnvich.apps.notes.domain.interactors.dashboardnotes.DashboardNotesInteractor
import rmnvich.apps.notes.domain.interactors.dashboardtags.DashboardTagsInteractor
import rmnvich.apps.notes.domain.interactors.trash.TrashInteractor
import rmnvich.apps.notes.domain.interactors.viewnote.ViewNoteInteractor
import rmnvich.apps.notes.presentation.ui.fragment.dashboardnotes.DashboardNotesViewModel

class ViewModelFactory : ViewModelProvider.NewInstanceFactory {

    private var application: Application

    private lateinit var dashboardNotesInteractor: DashboardNotesInteractor

    private lateinit var dashboardTagsInteractor: DashboardTagsInteractor

    private lateinit var trashInteractor: TrashInteractor

    private lateinit var viewNoteInteractor: ViewNoteInteractor

    constructor(application: Application,
                dashboardNotesInteractor: DashboardNotesInteractor) : super() {
        this.application = application
        this.dashboardNotesInteractor = dashboardNotesInteractor
    }

    constructor(application: Application,
                dashboardTagsInteractor: DashboardTagsInteractor) : super() {
        this.application = application
        this.dashboardTagsInteractor = dashboardTagsInteractor
    }

    constructor(application: Application,
                trashInteractor: TrashInteractor) : super() {
        this.application = application
        this.trashInteractor = trashInteractor
    }

    constructor(application: Application,
                viewNoteInteractor: ViewNoteInteractor) : super() {
        this.application = application
        this.viewNoteInteractor = viewNoteInteractor
    }

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(DashboardNotesViewModel::class.java) ->
                DashboardNotesViewModel(application, dashboardNotesInteractor) as T
            else -> throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
        }
    }

}