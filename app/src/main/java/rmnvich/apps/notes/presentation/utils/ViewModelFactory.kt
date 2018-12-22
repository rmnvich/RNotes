package rmnvich.apps.notes.presentation.utils

import android.app.Application
import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import io.reactivex.disposables.CompositeDisposable
import rmnvich.apps.notes.domain.interactors.dashboardnotes.DashboardNotesInteractor
import rmnvich.apps.notes.domain.interactors.dashboardtags.DashboardTagsInteractor
import rmnvich.apps.notes.domain.interactors.trash.TrashInteractor
import rmnvich.apps.notes.domain.interactors.viewnote.ViewNoteInteractor
import rmnvich.apps.notes.presentation.ui.fragment.dashboardnotes.DashboardNotesViewModel

class ViewModelFactory : ViewModelProvider.NewInstanceFactory {

    private var mApplication: Application

    private lateinit var mDashboardNotesInteractor: DashboardNotesInteractor

    private lateinit var mDashboardTagsInteractor: DashboardTagsInteractor

    private lateinit var mTrashInteractor: TrashInteractor

    private lateinit var mViewNoteInteractor: ViewNoteInteractor

    constructor(application: Application,
                dashboardNotesInteractor: DashboardNotesInteractor) : super() {
        this.mApplication = application
        this.mDashboardNotesInteractor = dashboardNotesInteractor
    }

    constructor(application: Application,
                dashboardTagsInteractor: DashboardTagsInteractor) : super() {
        this.mApplication = application
        this.mDashboardTagsInteractor = dashboardTagsInteractor
    }

    constructor(application: Application,
                trashInteractor: TrashInteractor) : super() {
        this.mApplication = application
        this.mTrashInteractor = trashInteractor
    }

    constructor(application: Application,
                viewNoteInteractor: ViewNoteInteractor) : super() {
        this.mApplication = application
        this.mViewNoteInteractor = viewNoteInteractor
    }

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(DashboardNotesViewModel::class.java) ->
                DashboardNotesViewModel(mApplication, CompositeDisposable(),
                        mDashboardNotesInteractor) as T
            else -> throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
        }
    }

}