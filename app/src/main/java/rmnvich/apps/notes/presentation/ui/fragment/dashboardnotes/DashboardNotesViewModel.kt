package rmnvich.apps.notes.presentation.ui.fragment.dashboardnotes

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.LiveData
import android.databinding.ObservableBoolean
import io.reactivex.disposables.CompositeDisposable
import rmnvich.apps.notes.domain.interactors.dashboardnotes.DashboardNotesInteractor
import android.arch.lifecycle.MutableLiveData
import android.os.Debug
import rmnvich.apps.notes.R
import rmnvich.apps.notes.domain.entity.Note
import rmnvich.apps.notes.domain.utils.SingleLiveEvent
import rmnvich.apps.notes.presentation.utils.DebugLogger
import rmnvich.apps.notes.presentation.utils.SnackbarMessage


class DashboardNotesViewModel(
    private val applicationContext: Application,
    private val dashboardNotesInteractor: DashboardNotesInteractor
) : AndroidViewModel(applicationContext) {

    val bIsShowingProgressBar: ObservableBoolean = ObservableBoolean(false)
    val bDataIsEmpty: ObservableBoolean = ObservableBoolean(false)

    private val mCompositeDisposable: CompositeDisposable = CompositeDisposable()

    private val mSelectedNoteId: MutableLiveData<Int> = MutableLiveData()

    private val mSnackbarMessage: SnackbarMessage = SnackbarMessage()
    private val mAddEditNoteEvent = SingleLiveEvent<Void>()

    private var mResponse: MutableLiveData<List<Note>>? = null

    fun getNotes(): LiveData<List<Note>>? {
        if (mResponse == null) {
            mResponse = MutableLiveData()
            loadNotes()
        }
        return mResponse
    }

    fun getSnackbar(): SnackbarMessage = mSnackbarMessage

    fun getSelected(): MutableLiveData<Int> = mSelectedNoteId

    fun getAddEditNoteEvent(): SingleLiveEvent<Void> = mAddEditNoteEvent

    fun addNote() {
        mSelectedNoteId.value = null
        mAddEditNoteEvent.call()
    }

    fun selectNote(noteId: Int?) {
        mSelectedNoteId.value = noteId
        mAddEditNoteEvent.call()
    }

    private fun loadNotes() {
        mCompositeDisposable.add(dashboardNotesInteractor.getAllNotes()
            .doOnSubscribe { bIsShowingProgressBar.set(true) }
            .subscribe({
                bIsShowingProgressBar.set(false)
                bDataIsEmpty.set(it.isEmpty())
                mResponse?.value = it
            }, {
                bIsShowingProgressBar.set(false)
                showSnackbarMessage(R.string.error_message)
            })
        )
    }

    private fun showSnackbarMessage(message: Int?) {
        mSnackbarMessage.value = message
    }

    override fun onCleared() {
        super.onCleared()
        mCompositeDisposable.dispose()
        mCompositeDisposable.clear()
    }
}
