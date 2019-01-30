package rmnvich.apps.notes.presentation.ui.activity.addeditnote

import android.arch.lifecycle.ViewModel
import android.content.Intent
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
import java.io.IOException

class AddEditNoteViewModel(private val addEditNoteNotesInteractor: AddEditNoteInteractor) : ViewModel() {

    val bIsShowingProgressBar: ObservableBoolean = ObservableBoolean(false)
    val bIsShowingImage: ObservableBoolean = ObservableBoolean(false)

    val noteText: ObservableField<String> = ObservableField("")
    val noteColor: ObservableField<Int> = ObservableField(Color.BLACK)
    val noteTag: ObservableField<Tag> = ObservableField()
    val noteTimestamp: ObservableField<Long> = ObservableField(DateHelper.getCurrentTimeInMills())

    var noteIsFavorite: Boolean = false

    private val mCompositeDisposable: CompositeDisposable = CompositeDisposable()

    private val onBackPressedEvent: SingleLiveEvent<Void> = SingleLiveEvent()
    private val onDeleteTagEvent: SingleLiveEvent<Void> = SingleLiveEvent()
    private val onPickColorEvent: SingleLiveEvent<Void> = SingleLiveEvent()
    private val onPickImageEvent: SingleLiveEvent<Void> = SingleLiveEvent()
    private val onImagePathEvent: SingleLiveEvent<String> = SingleLiveEvent()

    private val mSnackbarMessage: SingleLiveEvent<Int> = SingleLiveEvent()

    private var existsNote: Note? = null

    fun getSnackbar(): SingleLiveEvent<Int> = mSnackbarMessage

    fun getBackPressedEvent(): SingleLiveEvent<Void> = onBackPressedEvent

    fun getDeleteTagEvent(): SingleLiveEvent<Void> = onDeleteTagEvent

    fun getPickColorEvent(): SingleLiveEvent<Void> = onPickColorEvent

    fun getPickImageEvent(): SingleLiveEvent<Void> = onPickImageEvent

    fun getImagePathEvent(): SingleLiveEvent<String> = onImagePathEvent

    fun showImagePickerDialog() = onPickImageEvent.call()

    fun deleteTag() = onDeleteTagEvent.call()

    fun pickColor() = onPickColorEvent.call()

    fun loadNote(noteId: Int) {
        mCompositeDisposable.add(addEditNoteNotesInteractor.getNoteById(noteId)
                .doOnSubscribe { bIsShowingProgressBar.set(true) }
                .subscribe({
                    bIsShowingProgressBar.set(false)
                    existsNote = it

                    setObservableFields(it.text, it.color, it.tag, it.timestamp, it.imagePath)
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
        existsNote?.isFavorite = noteIsFavorite

        if (onImagePathEvent.value != null)
            existsNote?.imagePath = onImagePathEvent.value!!

        if (noteTag.get() == null)
            existsNote?.tag = null
        else existsNote?.tag = noteTag.get()!!

        existsNote?.isSelected = false

        if (!existsNote?.text?.isEmpty()!!) {
            bIsShowingProgressBar.set(true)
            mCompositeDisposable.add(
                    addEditNoteNotesInteractor
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

    fun deleteImage() {
        onImagePathEvent.value = ""
        bIsShowingImage.set(false)
    }

    fun onActivityResult(data: Intent?) {
        try {
            bIsShowingProgressBar.set(true)
            val disposable = addEditNoteNotesInteractor.getFilePathFromUri(data?.data!!)
                    .subscribe {
                        onImagePathEvent.value = it

                        bIsShowingImage.set(true)
                        bIsShowingProgressBar.set(false)
                    }
            mCompositeDisposable.add(disposable)
        } catch (e: IOException) {
            onImagePathEvent.value = ""

            bIsShowingProgressBar.set(true)
            bIsShowingImage.set(false)
            showSnackbarMessage(R.string.error_message)
        }
    }

    private fun setObservableFields(
            noteText: String, noteColor: Int, noteTag: Tag?,
            noteTimestamp: Long, noteImagePath: String
    ) {
        this.noteText.set(noteText)
        this.noteColor.set(noteColor)
        this.noteTag.set(noteTag)
        this.noteTimestamp.set(noteTimestamp)

        onImagePathEvent.value = noteImagePath
        if (!noteImagePath.isEmpty())
            bIsShowingImage.set(true)
    }

    private fun showSnackbarMessage(message: Int?) {
        mSnackbarMessage.value = message
    }

    override fun onCleared() {
        super.onCleared()
        mCompositeDisposable.clear()
    }
}