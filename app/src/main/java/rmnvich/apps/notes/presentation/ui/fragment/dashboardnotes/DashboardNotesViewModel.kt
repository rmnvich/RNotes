package rmnvich.apps.notes.presentation.ui.fragment.dashboardnotes

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.databinding.ObservableBoolean
import io.reactivex.disposables.CompositeDisposable
import rmnvich.apps.notes.R
import rmnvich.apps.notes.domain.entity.Note
import rmnvich.apps.notes.domain.interactors.dashboardnotes.DashboardNotesInteractor
import rmnvich.apps.notes.domain.utils.SingleLiveEvent
import rmnvich.apps.notes.presentation.utils.SnackbarMessage

class DashboardNotesViewModel(private val dashboardNotesInteractor: DashboardNotesInteractor) : ViewModel() {

    val bIsShowingProgressBar: ObservableBoolean = ObservableBoolean(false)
    val bDataIsEmpty: ObservableBoolean = ObservableBoolean(false)

    var bRecyclerIsScroll: Boolean = false

    private val mCompositeDisposable: CompositeDisposable = CompositeDisposable()

    private val mSelectedNoteId: SingleLiveEvent<Int> = SingleLiveEvent()

    private val mAddEditNoteEvent = SingleLiveEvent<Void>()

    private val mSnackbarMessage: SnackbarMessage = SnackbarMessage()

    private var mResponse: MutableLiveData<List<Note>>? = null

    fun getNotes(): LiveData<List<Note>>? {
        if (mResponse == null) {
            mResponse = MutableLiveData()
            loadNotes()
        }
        return mResponse
    }

    fun getSnackbar(): SnackbarMessage = mSnackbarMessage

    fun getEditNoteEvent(): SingleLiveEvent<Int> = mSelectedNoteId

    fun getAddNoteEvent(): SingleLiveEvent<Void> = mAddEditNoteEvent

    fun addNote() {
        mAddEditNoteEvent.call()
        bRecyclerIsScroll = true
    }

    fun editNote(noteId: Int?) {
        mSelectedNoteId.value = noteId
        bRecyclerIsScroll = false
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
