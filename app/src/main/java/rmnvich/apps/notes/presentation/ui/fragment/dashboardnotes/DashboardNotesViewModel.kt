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

class DashboardNotesViewModel(
        private val dashboardNotesInteractor: DashboardNotesInteractor,
        private val isFavoriteNotes: Boolean
) : ViewModel() {

    val bIsShowingProgressBar: ObservableBoolean = ObservableBoolean(false)
    val bNotesIsEmpty: ObservableBoolean = ObservableBoolean(false)

    var bIsRecyclerNeedToScroll: Boolean = false

    private val mCompositeDisposable: CompositeDisposable = CompositeDisposable()

    private val mSelectedNoteId = SingleLiveEvent<Int>()

    private val mAddEditNoteEvent = SingleLiveEvent<Void>()
    private val mDeleteNoteEvent = SingleLiveEvent<Int>()

    private val mSnackbarMessage = SingleLiveEvent<Int>()

    private var mNotesResponse: MutableLiveData<List<Note>>? = null

    fun forceUpdate() {
        mCompositeDisposable.clear()
        getNotes(true)
    }

    fun getNotes(forceUpdate: Boolean): LiveData<List<Note>>? {
        if (mNotesResponse == null) {
            mNotesResponse = MutableLiveData()
            loadNotes()
        }

        if (forceUpdate)
            loadNotes()

        return mNotesResponse
    }

    fun getSnackbar(): SingleLiveEvent<Int> = mSnackbarMessage

    fun getEditNoteEvent(): SingleLiveEvent<Int> = mSelectedNoteId

    fun getAddNoteEvent(): SingleLiveEvent<Void> = mAddEditNoteEvent

    fun getDeleteNoteEvent(): SingleLiveEvent<Int> = mDeleteNoteEvent

    fun addNote() {
        mAddEditNoteEvent.call()
        bIsRecyclerNeedToScroll = true
    }

    fun editNote(noteId: Int?) {
        mSelectedNoteId.value = noteId
    }

    private var deletedItemPosition: Int = -1

    fun deleteNote(noteId: Int, position: Int) {
        deletedItemPosition = position

        mCompositeDisposable.add(
                dashboardNotesInteractor
                        .removeNoteToTrash(noteId)
                        .subscribe({ mDeleteNoteEvent.value = noteId },
                                { showSnackbarMessage(R.string.error_message) })
        )
    }

    fun restoreNote(noteId: Int) {
        if (deletedItemPosition == 0)
            bIsRecyclerNeedToScroll = true

        mCompositeDisposable.add(
                dashboardNotesInteractor
                        .restoreNote(noteId)
                        .subscribe({}, { showSnackbarMessage(R.string.error_message) })
        )
    }

    fun updateIsFavoriteNote(noteId: Int, isFavorite: Boolean) {
        mCompositeDisposable.add(
                dashboardNotesInteractor
                        .updateIsFavoriteNote(noteId, isFavorite)
                        .subscribe()
        )
    }

    private fun loadNotes() {
        mCompositeDisposable.add(dashboardNotesInteractor.getNotes(isFavoriteNotes)
                .doOnSubscribe { bIsShowingProgressBar.set(true) }
                .subscribe({
                    bIsShowingProgressBar.set(false)
                    bNotesIsEmpty.set(it.isEmpty())
                    mNotesResponse?.value = it
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
        mCompositeDisposable.clear()
    }
}
