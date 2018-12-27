package rmnvich.apps.notes.presentation.ui.activity.addeditnote

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.databinding.ObservableBoolean
import android.databinding.ObservableField
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
        private val addEditNoteNotesInteractor: AddEditNoteInteractor
) : AndroidViewModel(applicationContext) {

    val bIsShowingProgressBar: ObservableBoolean = ObservableBoolean(false)

    val noteText: ObservableField<String> = ObservableField("")
    val noteColor: ObservableField<Int> = ObservableField(DEFAULT_COLOR)
    val noteTag: ObservableField<Tag> = ObservableField()

    private val mCompositeDisposable: CompositeDisposable = CompositeDisposable()

    private val noteInsertedOrUpdated: SingleLiveEvent<Void> = SingleLiveEvent()
    private val mSnackbarMessage: SnackbarMessage = SnackbarMessage()

    private var mResponse: MutableLiveData<Note> = MutableLiveData()

    private var existsNote: Note? = null

    fun getSnackbar(): SnackbarMessage = mSnackbarMessage

    fun getInsertNoteEvent(): SingleLiveEvent<Void> = noteInsertedOrUpdated

    fun insertOrUpdateNote() {
        if (existsNote == null) {
            existsNote = Note()
            existsNote?.timestamp =
                    DateHelper.getCurrentTimeInMills()
        }
        existsNote?.text = noteText.get()!!
        existsNote?.color = noteColor.get()!!
        if (noteTag.get() != null)
            existsNote?.tag = noteTag.get()!!

        if (existsNote?.text?.isEmpty()!!) {
            showSnackbarMessage(R.string.empty_note_error)
        } else {
            mCompositeDisposable.add(addEditNoteNotesInteractor
                    .insertOrUpdateNote(existsNote!!)
                    .subscribe { noteInsertedOrUpdated.call() }
            )
        }
    }

    fun loadNote(noteId: Int) {
        mCompositeDisposable.add(addEditNoteNotesInteractor.getNoteById(noteId)
                .doOnSubscribe { bIsShowingProgressBar.set(true) }
                .subscribe({
                    bIsShowingProgressBar.set(false)
                    mResponse.value = it

                    existsNote = it
                    setObservableFields(it.text, it.color, it.tag)
                }, {
                    bIsShowingProgressBar.set(false)
                    showSnackbarMessage(R.string.error_message)
                })
        )
    }

    private fun setObservableFields(noteText: String, noteColor: Int, noteTag: Tag?) {
        this.noteText.set(noteText)
        this.noteColor.set(noteColor)
        this.noteTag.set(noteTag)
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