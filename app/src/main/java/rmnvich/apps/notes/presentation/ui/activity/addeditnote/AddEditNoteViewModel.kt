package rmnvich.apps.notes.presentation.ui.activity.addeditnote

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.databinding.ObservableBoolean
import android.databinding.ObservableField
import android.graphics.Color
import android.support.v4.content.ContextCompat
import io.reactivex.disposables.CompositeDisposable
import rmnvich.apps.notes.R
import rmnvich.apps.notes.data.common.Constants.DEFAULT_COLOR
import rmnvich.apps.notes.domain.entity.Note
import rmnvich.apps.notes.domain.entity.Tag
import rmnvich.apps.notes.domain.interactors.addeditnote.AddEditNoteInteractor
import rmnvich.apps.notes.domain.utils.SingleLiveEvent
import rmnvich.apps.notes.presentation.utils.DateHelper
import rmnvich.apps.notes.presentation.utils.SnackbarMessage

class AddEditNoteViewModel(private val addEditNoteNotesInteractor: AddEditNoteInteractor) : ViewModel() {

    val bIsShowingProgressBar: ObservableBoolean = ObservableBoolean(false)

    val noteText: ObservableField<String> = ObservableField("")
    val noteColor: ObservableField<Int> = ObservableField(Color.DKGRAY)
    val noteTag: ObservableField<Tag> = ObservableField()
    val noteFavorite: ObservableBoolean = ObservableBoolean()

    private val mCompositeDisposable: CompositeDisposable = CompositeDisposable()

    private val onBackPressedEvent: SingleLiveEvent<Void> = SingleLiveEvent()
    private val onDeleteTagEvent: SingleLiveEvent<Void> = SingleLiveEvent()
    private val onPickColorEvent: SingleLiveEvent<Void> = SingleLiveEvent()

    private val mSnackbarMessage: SnackbarMessage = SnackbarMessage()

    private var mResponse: MutableLiveData<Note> = MutableLiveData()

    private var existsNote: Note? = null

    fun getSnackbar(): SnackbarMessage = mSnackbarMessage

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
                    mResponse.value = it

                    existsNote = it
                    setObservableFields(it.text, it.color, it.tag, it.isFavorite)
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
        existsNote?.isFavorite = noteFavorite.get()

        if (noteTag.get() == null)
            existsNote?.tag = null
        else existsNote?.tag = noteTag.get()!!

        if (!existsNote?.text?.isEmpty()!!) {
            bIsShowingProgressBar.set(true)
            mCompositeDisposable.add(addEditNoteNotesInteractor
                    .insertOrUpdateNote(existsNote!!)
                    .subscribe {
                        onBackPressedEvent.call()
                        bIsShowingProgressBar.set(false)
                    }
            )
        } else onBackPressedEvent.call()
    }

    private fun setObservableFields(noteText: String, noteColor: Int,
                                    noteTag: Tag?, noteFavorite: Boolean) {
        this.noteText.set(noteText)
        this.noteColor.set(noteColor)
        this.noteTag.set(noteTag)
        this.noteFavorite.set(noteFavorite)
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