package rmnvich.apps.notes.presentation.ui.fragment.dashboardnotes

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.LiveData
import android.databinding.ObservableBoolean
import io.reactivex.disposables.CompositeDisposable
import rmnvich.apps.notes.domain.interactors.dashboardnotes.DashboardNotesInteractor
import android.arch.lifecycle.MutableLiveData
import rmnvich.apps.notes.domain.interactors.Response
import rmnvich.apps.notes.presentation.utils.SingleLiveEvent
import rmnvich.apps.notes.presentation.utils.SnackbarMessage


class DashboardNotesViewModel(
        private val applicationContext: Application,
        private val disposables: CompositeDisposable,
        private val dashboardNotesInteractor: DashboardNotesInteractor
) : AndroidViewModel(applicationContext) {

    val bIsShowingProgressBar: ObservableBoolean = ObservableBoolean(false)
    val bDataIsEmpty: ObservableBoolean = ObservableBoolean(false)

    val mSnackbarMessage: SnackbarMessage = SnackbarMessage()
    val mAddEditNoteEvent = SingleLiveEvent<Void>()

    private var mResponse: MutableLiveData<Response>? = null

    fun getNotes(): LiveData<Response>? {
        if (mResponse == null) {
            mResponse = MutableLiveData()
            loadNotes()
        }
        return mResponse
    }

    private fun loadNotes() {
        bIsShowingProgressBar.set(true)
        disposables.add(dashboardNotesInteractor.getAllNotes()
                .doOnSubscribe { mResponse?.value = Response.onLoading() }
                .subscribe({ mResponse?.value = Response.onSuccess(it) },
                        { mResponse?.value = Response.onError(it) })
        )
    }

    fun addNote() {
        mAddEditNoteEvent.call()
    }

    fun showSnackbarMessage(message: Int?) {
        mSnackbarMessage.value = message
    }

    override fun onCleared() {
        super.onCleared()
        disposables.clear()
    }

}
