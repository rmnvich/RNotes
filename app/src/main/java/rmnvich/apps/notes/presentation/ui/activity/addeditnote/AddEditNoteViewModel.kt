package rmnvich.apps.notes.presentation.ui.activity.addeditnote

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.content.Intent
import android.databinding.ObservableBoolean
import android.databinding.ObservableField
import android.graphics.Bitmap
import android.graphics.Color
import android.support.v4.content.FileProvider
import io.reactivex.disposables.CompositeDisposable
import rmnvich.apps.notes.R
import rmnvich.apps.notes.data.common.Constants.REQUEST_CODE_IMAGE
import rmnvich.apps.notes.domain.entity.Note
import rmnvich.apps.notes.domain.entity.NoteWithTag
import rmnvich.apps.notes.domain.interactors.addeditnote.AddEditNoteInteractor
import rmnvich.apps.notes.domain.utils.SingleLiveEvent
import rmnvich.apps.notes.presentation.utils.DateHelper
import java.io.File
import java.io.IOException

class AddEditNoteViewModel(
    private val applicationContext: Application,
    private val addEditNoteNotesInteractor: AddEditNoteInteractor
) : AndroidViewModel(applicationContext) {

    val bIsShowingProgressBar: ObservableBoolean = ObservableBoolean(false)
    val bIsShowingImage: ObservableBoolean = ObservableBoolean(false)

    val noteText: ObservableField<String> = ObservableField("")
    val noteColor: ObservableField<Int> = ObservableField(Color.BLACK)
    val noteTag: ObservableField<String> = ObservableField("")
    val noteTimestamp: ObservableField<Long> = ObservableField(DateHelper.getCurrentTimeInMills())

    var noteIsFavorite: Boolean = false
    var noteTagId: Int = -1
    var noteId: Int = -1

    private var mNoteResponse: MutableLiveData<NoteWithTag>? = null

    private val onBackPressedEvent: SingleLiveEvent<Void> = SingleLiveEvent()
    private val onDeleteTagEvent: SingleLiveEvent<Void> = SingleLiveEvent()
    private val onPickColorEvent: SingleLiveEvent<Void> = SingleLiveEvent()
    private val onPickImageEvent: SingleLiveEvent<Void> = SingleLiveEvent()
    private val onImagePathEvent: SingleLiveEvent<String> = SingleLiveEvent()
    private val onShareNoteEvent: SingleLiveEvent<Intent> = SingleLiveEvent()
    private val onClickImageEvent: SingleLiveEvent<String> = SingleLiveEvent()
    private val onClickDateEvent: SingleLiveEvent<Void> = SingleLiveEvent()
    private val mSnackbarMessage: SingleLiveEvent<Int> = SingleLiveEvent()

    private val mCompositeDisposable: CompositeDisposable = CompositeDisposable()

    private var files: MutableList<File> = mutableListOf()

    fun getSnackbar(): SingleLiveEvent<Int> = mSnackbarMessage

    fun getBackPressedEvent(): SingleLiveEvent<Void> = onBackPressedEvent

    fun getDeleteTagEvent(): SingleLiveEvent<Void> = onDeleteTagEvent

    fun getPickColorEvent(): SingleLiveEvent<Void> = onPickColorEvent

    fun getPickImageEvent(): SingleLiveEvent<Void> = onPickImageEvent

    fun getImagePathEvent(): SingleLiveEvent<String> = onImagePathEvent

    fun getShareNoteEvent(): SingleLiveEvent<Intent> = onShareNoteEvent

    fun getClickImageEvent(): SingleLiveEvent<String> = onClickImageEvent

    fun getClickDateEvent(): SingleLiveEvent<Void> = onClickDateEvent

    fun showImagePickerDialog() = onPickImageEvent.call()

    fun showDatePickerDialog() = onClickDateEvent.call()

    fun deleteTag() = onDeleteTagEvent.call()

    fun pickColor() = onPickColorEvent.call()

    fun onClickImage() {
        onClickImageEvent.value = onImagePathEvent.value
    }

    fun onDatePickerDialogClicked(year: Int, month: Int, day: Int) {
        noteTimestamp.set(DateHelper.getDate(year, month, day))
    }

    fun getNote(noteId: Int): LiveData<NoteWithTag>? {
        if (mNoteResponse == null) {
            mNoteResponse = MutableLiveData()
            loadNote(noteId)
        }
        return mNoteResponse
    }

    private fun loadNote(noteId: Int) {
        mCompositeDisposable.add(addEditNoteNotesInteractor.getNoteById(noteId)
            .doOnSubscribe { bIsShowingProgressBar.set(true) }
            .subscribe({
                bIsShowingProgressBar.set(false)
                mNoteResponse?.value = it
                this.noteId = it.noteId

                setObservableFields(
                    it.noteText, it.noteColor, it.tagName,
                    it.noteTimestamp, it.noteImagePath
                )
            }, {
                bIsShowingProgressBar.set(false)
                showSnackbarMessage(R.string.error_message)
            })
        )
    }

    fun insertOrUpdateNote() {
        if (!noteText.get()?.isEmpty()!!) {
            val note = Note()
            note.text = noteText.get()?.trim()!!
            note.timestamp = noteTimestamp.get()!!
            note.color = noteColor.get()!!
            note.tagId = noteTagId

            if (onImagePathEvent.value != null)
                note.imagePath = onImagePathEvent.value!!

            if (noteId == -1) {
                note.isFavorite = noteIsFavorite
                insertNote(note)
            } else updateNote(note, noteId)
        } else onBackPressedEvent.call()
    }

    fun deleteImage() {
        onImagePathEvent.value = ""
        bIsShowingImage.set(false)
    }

    fun onActivityResult(data: Intent?, requestCode: Int) {
        if (requestCode == REQUEST_CODE_IMAGE) {
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
        } else {
            for (file in files) {
                file.delete()
            }
        }
    }

    fun onShareClicked(bitmap: Bitmap?) {
        if (!noteText.get()?.isEmpty()!!) {
            val shareBody = noteText.get()!!

            val intent = Intent(Intent.ACTION_SEND)
            intent.type = "text/plain"
            intent.putExtra(Intent.EXTRA_TEXT, shareBody)

            if (bitmap != null) {
                bIsShowingProgressBar.set(true)
                mCompositeDisposable.add(addEditNoteNotesInteractor
                    .getImageFileFromBitmap(bitmap)
                    .subscribe {
                        bIsShowingProgressBar.set(false)

                        val photoURI = FileProvider.getUriForFile(
                            applicationContext, "rmnvich.apps.notes.fileprovider", it
                        )
                        files.add(it)

                        intent.type = "image/jpeg"
                        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                        intent.putExtra(Intent.EXTRA_STREAM, photoURI)

                        onShareNoteEvent.value = intent
                    })
            } else onShareNoteEvent.value = intent
        }
    }

    private fun insertNote(note: Note) {
        bIsShowingProgressBar.set(true)
        mCompositeDisposable.add(
            addEditNoteNotesInteractor
                .insertNote(note)
                .subscribe({
                    bIsShowingProgressBar.set(false)
                    onBackPressedEvent.call()
                }, {
                    bIsShowingProgressBar.set(false)
                    showSnackbarMessage(R.string.error_message)
                })
        )
    }

    private fun updateNote(note: Note, noteId: Int) {
        bIsShowingProgressBar.set(true)
        mCompositeDisposable.add(
            addEditNoteNotesInteractor
                .updateNote(note, noteId)
                .subscribe({
                    bIsShowingProgressBar.set(false)
                    onBackPressedEvent.call()
                }, {
                    bIsShowingProgressBar.set(false)
                    showSnackbarMessage(R.string.error_message)
                })
        )
    }

    private fun setObservableFields(
        noteText: String, noteColor: Int, noteTag: String,
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