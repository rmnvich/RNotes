package rmnvich.apps.notes.presentation.ui.fragment.dashboardnotes

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.LiveData
import android.databinding.ObservableBoolean
import io.reactivex.Completable
import rmnvich.apps.notes.domain.entity.Note
import rmnvich.apps.notes.domain.interactors.dashboardnotes.DashboardNotesInteractor

class DashboardNotesViewModel(
        private val applicationContext: Application,
        private val mDashboardNotesInteractor: DashboardNotesInteractor
) : AndroidViewModel(applicationContext) {

    val bIsShowingProgressBar: ObservableBoolean = ObservableBoolean(false)
    val bDataIsEmpty: ObservableBoolean = ObservableBoolean(false)

    private var mNotesLiveData: LiveData<List<Note>>? = null

    fun getNotes(): LiveData<List<Note>> {
        bIsShowingProgressBar.set(true)

        if (mNotesLiveData == null) {
            mNotesLiveData = mDashboardNotesInteractor.getAllNotes()
        }
        return mNotesLiveData as LiveData<List<Note>>
    }

}
