package rmnvich.apps.notes.presentation.ui.activity.addeditnote

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.content.Intent
import android.databinding.ObservableBoolean
import android.databinding.ObservableField
import android.graphics.Bitmap
import android.support.v4.content.FileProvider
import io.reactivex.disposables.CompositeDisposable
import rmnvich.apps.notes.R
import rmnvich.apps.notes.data.common.Constants.DEFAULT_COLOR
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

    val bIsShowingProgressBar = ObservableBoolean(false)

    val bIsShowingImage = ObservableBoolean(false)

    val noteText = ObservableField("")

    val noteTag = ObservableField("")

    val noteImagePath = ObservableField("")

    val noteColor = ObservableField(DEFAULT_COLOR)

    val noteTimestamp = ObservableField(DateHelper.getCurrentTimeInMills())

    var noteIsFavorite = false

    var noteIsLocked = false

    var noteTagId: Int? = null

    var noteId = 0

    private var mNoteResponse: MutableLiveData<NoteWithTag>? = null

    private val onBackPressedEvent = SingleLiveEvent<Void>()

    private val onActionMoreEvent = SingleLiveEvent<Void>()

    private val onDeleteTagEvent = SingleLiveEvent<Void>()

    private val onPickImageEvent = SingleLiveEvent<Void>()

    private val onShareNoteEvent = SingleLiveEvent<Intent>()

    private val onClickImageEvent = SingleLiveEvent<String>()

    private val mSnackbarMessage = SingleLiveEvent<Int>()

    private val mCompositeDisposable = CompositeDisposable()

    private var files: MutableList<File> = mutableListOf()

    fun getSnackbar(): SingleLiveEvent<Int> = mSnackbarMessage

    fun getBackPressedEvent(): SingleLiveEvent<Void> = onBackPressedEvent

    fun getActionMoreEvent(): SingleLiveEvent<Void> = onActionMoreEvent

    fun getDeleteTagEvent(): SingleLiveEvent<Void> = onDeleteTagEvent

    fun getPickImageEvent(): SingleLiveEvent<Void> = onPickImageEvent

    fun getShareNoteEvent(): SingleLiveEvent<Intent> = onShareNoteEvent

    fun getClickImageEvent(): SingleLiveEvent<String> = onClickImageEvent

    fun onClickActionMore() = onActionMoreEvent.call()

    fun showImagePickerDialog() = onPickImageEvent.call()

    fun deleteTag() = onDeleteTagEvent.call()

    fun onClickImage() {
        onClickImageEvent.value = noteImagePath.get()
    }

    fun tagsIsEmpty() {
        showSnackbarMessage(R.string.you_have_no_tags)
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
                    this.noteTagId = it.tagId

                    setObservableFields(
                            it.noteText, it.noteColor, it.tagName, it.noteTimestamp,
                            it.noteImagePath, it.noteIsFavorite, it.noteIsLocked
                    )
                }, {
                    bIsShowingProgressBar.set(false)
                    showSnackbarMessage(R.string.error_message)
                })
        )
    }

    fun insertOrUpdateNote() {
        val isDataCorrect = !noteText.get()?.trim().isNullOrEmpty() ||
                !noteImagePath.get().isNullOrEmpty()

        if (isDataCorrect) {
            val note = Note()
            note.text = noteText.get()?.trim()!!
            note.timestamp = noteTimestamp.get()!!
            note.imagePath = noteImagePath.get()!!
            note.color = noteColor.get()!!

            note.isFavorite = noteIsFavorite
            note.isLocked = noteIsLocked
            note.tagId = noteTagId

            if (noteId == 0) {
                insertNote(note)
            } else updateNote(note, noteId)
        } else onBackPressedEvent.call()
    }

    fun deleteImage() {
        noteImagePath.set("")
        bIsShowingImage.set(false)
    }

    fun onActivityResult(data: Intent?, requestCode: Int) {
        if (requestCode == REQUEST_CODE_IMAGE) {
            try {
                bIsShowingProgressBar.set(true)
                val disposable = addEditNoteNotesInteractor.getFilePathFromUri(data?.data!!)
                        .subscribe {
                            noteImagePath.set(it)

                            bIsShowingImage.set(true)
                            bIsShowingProgressBar.set(false)
                        }
                mCompositeDisposable.add(disposable)
            } catch (e: IOException) {
                noteImagePath.set("")

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
        val emptyText = noteText.get()?.isEmpty()!!
        val emptyImage = bitmap == null

        if (!(emptyText && emptyImage)) {
            val intent = Intent(Intent.ACTION_SEND)

            if (!emptyText) {
                intent.type = "text/plain"
                intent.putExtra(Intent.EXTRA_TEXT, noteText.get()!!)
            }

            if (!emptyImage) {
                bIsShowingProgressBar.set(true)
                mCompositeDisposable.add(addEditNoteNotesInteractor
                        .getImageFileFromBitmap(bitmap!!)
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
        } else showSnackbarMessage(R.string.share_blank_note)
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
            noteText: String, noteColor: Int, noteTag: String?, noteTimestamp: Long,
            noteImagePath: String, noteIsFavorite: Boolean, noteIsLocked: Boolean
    ) {
        this.noteText.set(noteText)
        this.noteColor.set(noteColor)
        this.noteTimestamp.set(noteTimestamp)

        if (noteTag != null)
            this.noteTag.set(noteTag)
        else this.noteTag.set("")

        this.noteIsFavorite = noteIsFavorite
        this.noteIsLocked = noteIsLocked

        this.noteImagePath.set(noteImagePath)
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