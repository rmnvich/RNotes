package rmnvich.apps.notes.presentation.ui.fragment.trash

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.databinding.ObservableBoolean
import io.reactivex.disposables.CompositeDisposable
import rmnvich.apps.notes.R
import rmnvich.apps.notes.domain.entity.Note
import rmnvich.apps.notes.domain.interactors.trash.TrashInteractor
import rmnvich.apps.notes.domain.utils.SingleLiveEvent

class TrashViewModel(private val trashInteractor: TrashInteractor) : ViewModel() {

    val bIsShowingProgressBar: ObservableBoolean = ObservableBoolean(false)

    val bDataIsEmpty: ObservableBoolean = ObservableBoolean(false)

    var bIsNotesSelected: Boolean = false

    private val mCompositeDisposable: CompositeDisposable = CompositeDisposable()

    private val mSnackbarMessage: SingleLiveEvent<Int> = SingleLiveEvent()

    private val mDeleteOrRestoreNotesEvent: SingleLiveEvent<Void> = SingleLiveEvent()

    private var mResponse: MutableLiveData<List<Note>>? = null

    fun getNotes(): LiveData<List<Note>>? {
        if (mResponse == null) {
            mResponse = MutableLiveData()
            loadNotes()
        }
        return mResponse
    }

    fun getSnackbar(): SingleLiveEvent<Int> = mSnackbarMessage

    fun getDeleteOrRestoreNotesEvent(): SingleLiveEvent<Void> = mDeleteOrRestoreNotesEvent

    fun deleteNotes(notes: List<Note>) {
        mCompositeDisposable.addAll(
                trashInteractor
                        .deleteNotes(notes)
                        .subscribe({
                            mDeleteOrRestoreNotesEvent.call()
                        }, { showSnackbarMessage(R.string.error_message) })
        )
    }

    fun restoreNotes(notes: List<Note>) {
        mCompositeDisposable.add(
                trashInteractor
                        .restoreNotes(notes)
                        .subscribe({
                            mDeleteOrRestoreNotesEvent.call()
                        }, { showSnackbarMessage(R.string.error_message) })
        )
    }

    fun deleteNote(note: Note) {
        mCompositeDisposable.add(
                trashInteractor
                        .deleteNote(note)
                        .subscribe({}, { showSnackbarMessage(R.string.error_message) })
        )
    }

    fun restoreNote(noteId: Int) {
        mCompositeDisposable.add(
                trashInteractor
                        .restoreNote(noteId)
                        .subscribe({}, { showSnackbarMessage(R.string.error_message) })
        )
    }

    private fun loadNotes() {
        mCompositeDisposable.add(trashInteractor.getDeletedNotes()
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
        mCompositeDisposable.clear()
    }
}