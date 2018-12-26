package rmnvich.apps.notes.presentation.ui.fragment.addeditnote

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.databinding.ObservableBoolean
import android.databinding.ObservableField
import io.reactivex.Single
import io.reactivex.disposables.CompositeDisposable
import rmnvich.apps.notes.R
import rmnvich.apps.notes.data.common.Constants.DEFAULT_COLOR
import rmnvich.apps.notes.domain.entity.Note
import rmnvich.apps.notes.domain.entity.Tag
import rmnvich.apps.notes.domain.interactors.addeditnote.AddEditNoteInteractor
import rmnvich.apps.notes.domain.utils.SingleLiveEvent
import rmnvich.apps.notes.presentation.utils.DateHelper
import rmnvich.apps.notes.presentation.utils.SnackbarMessage

class AddEditNoteViewModel(
        private val applicationContext: Application,
        private val disposables: CompositeDisposable,
        private val addEditNoteNotesInteractor: AddEditNoteInteractor
) : AndroidViewModel(applicationContext) {

    val bIsShowingProgressBar: ObservableBoolean = ObservableBoolean(false)

    val noteText: ObservableField<String> = ObservableField("")
    val noteColor: ObservableField<Int> = ObservableField(DEFAULT_COLOR)
    val noteTag: ObservableField<Tag> = ObservableField()

    private val noteInsertedOrUpdated: SingleLiveEvent<Void> = SingleLiveEvent()
    private val mSnackbarMessage: SnackbarMessage = SnackbarMessage()

    private var mResponse: MutableLiveData<Note> = MutableLiveData()

    private var existsNote: Note? = null

    fun getSnackbar(): SnackbarMessage = mSnackbarMessage

    fun getInsertNoteEvent(): SingleLiveEvent<Void> = noteInsertedOrUpdated

    fun getNote(noteId: Int): LiveData<Note> {
        loadNote(noteId)
        return mResponse
    }

    fun insertOrUpdateNote() {
        if (existsNote == null)
            existsNote = Note()

        existsNote?.text = noteText.get()!!
        existsNote?.color = noteColor.get()!!
        existsNote?.timestamp = DateHelper.getCurrentTimeInMills()
        if (noteTag.get() != null)
            existsNote?.tag = noteTag.get()!!

        if (existsNote?.text?.isEmpty()!!) {
            showSnackbarMessage(R.string.empty_note_error)
        } else {
            disposables.add(addEditNoteNotesInteractor
                    .insertOrUpdateNote(existsNote!!)
                    .subscribe {
                        noteInsertedOrUpdated.call()
                    }
            )
            noteText.set("")
            noteColor.set(DEFAULT_COLOR)
            noteTag.set(null)
        }
    }

    private fun loadNote(noteId: Int) {
        disposables.add(addEditNoteNotesInteractor.getNoteById(noteId)
                .doOnSubscribe { bIsShowingProgressBar.set(true) }
                .subscribe({
                    bIsShowingProgressBar.set(false)
                    mResponse.value = it
                    existsNote = it
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
        disposables.clear()
    }
}