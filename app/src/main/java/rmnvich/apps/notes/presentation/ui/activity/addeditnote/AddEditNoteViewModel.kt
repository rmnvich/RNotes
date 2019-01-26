package rmnvich.apps.notes.presentation.ui.activity.addeditnote

import android.arch.lifecycle.ViewModel
import android.databinding.ObservableBoolean
import android.databinding.ObservableField
import android.graphics.Color
import io.reactivex.disposables.CompositeDisposable
import rmnvich.apps.notes.R
import rmnvich.apps.notes.domain.entity.Note
import rmnvich.apps.notes.domain.entity.Tag
import rmnvich.apps.notes.domain.interactors.addeditnote.AddEditNoteInteractor
import rmnvich.apps.notes.domain.utils.SingleLiveEvent
import rmnvich.apps.notes.presentation.utils.DateHelper

class AddEditNoteViewModel(private val addEditNoteNotesInteractor: AddEditNoteInteractor) : ViewModel() {

    val bIsShowingProgressBar: ObservableBoolean = ObservableBoolean(false)

    val noteText: ObservableField<String> = ObservableField("")
    val noteColor: ObservableField<Int> = ObservableField(Color.DKGRAY)
    val noteTag: ObservableField<Tag> = ObservableField()
    val noteTimestamp: ObservableField<Long> = ObservableField(DateHelper.getCurrentTimeInMills())

    private val mCompositeDisposable: CompositeDisposable = CompositeDisposable()

    private val onBackPressedEvent: SingleLiveEvent<Void> = SingleLiveEvent()
    private val onDeleteTagEvent: SingleLiveEvent<Void> = SingleLiveEvent()
    private val onPickColorEvent: SingleLiveEvent<Void> = SingleLiveEvent()

    private val mSnackbarMessage: SingleLiveEvent<Int> = SingleLiveEvent()

    private var existsNote: Note? = null

    fun getSnackbar(): SingleLiveEvent<Int> = mSnackbarMessage

    fun getBackPressedEvent(): SingleLiveEvent<Void> = onBackPressedEvent

    fun getDeleteTagEvent(): SingleLiveEvent<Void> = onDeleteTagEvent

    fun getPickColorEvent(): SingleLiveEvent<Void> = onPickColorEvent

    fun deleteTag() = onDeleteTagEvent.call()

    fun pickColor() = onPickColorEvent.call()

    fun loadNote(noteId: Int) {
        mCompositeDisposable.add(addEditNoteNotesInteractor.getNoteById(noteId)
                .doOnSubscribe { bIsShowingProgressBar.set(true) }
                .subscribe({
                    bIsShowingProgressBar.set(false)
                    existsNote = it

                    setObservableFields(it.text, it.color, it.tag, it.timestamp)
                }, {
                    bIsShowingProgressBar.set(false)
                    showSnackbarMessage(R.string.error_message)
                })
        )
    }

    fun insertOrUpdateNote() {
        if (existsNote == null) {
            existsNote = Note()
            existsNote?.timestamp = DateHelper.getCurrentTimeInMills()
        }
        existsNote?.text = noteText.get()?.trim()!!
        existsNote?.color = noteColor.get()!!

        if (noteTag.get() == null)
            existsNote?.tag = null
        else existsNote?.tag = noteTag.get()!!

        existsNote?.isSelected = false

        if (!existsNote?.text?.isEmpty()!!) {
            bIsShowingProgressBar.set(true)
            mCompositeDisposable.add(addEditNoteNotesInteractor
                    .insertOrUpdateNote(existsNote!!)
                    .subscribe({
                        bIsShowingProgressBar.set(false)
                        onBackPressedEvent.call()
                    }, {
                        bIsShowingProgressBar.set(false)
                        showSnackbarMessage(R.string.error_message)
                    })
            )
        } else onBackPressedEvent.call()
    }

    private fun setObservableFields(
            noteText: String, noteColor: Int,
            noteTag: Tag?, noteTimestamp: Long
    ) {
        this.noteText.set(noteText)
        this.noteColor.set(noteColor)
        this.noteTag.set(noteTag)
        this.noteTimestamp.set(noteTimestamp)
    }

    private fun showSnackbarMessage(message: Int?) {
        mSnackbarMessage.value = message
    }

    override fun onCleared() {
        super.onCleared()
        mCompositeDisposable.clear()
    }
}