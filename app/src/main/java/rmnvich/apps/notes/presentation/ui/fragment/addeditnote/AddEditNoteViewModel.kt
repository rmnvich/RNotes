package rmnvich.apps.notes.presentation.ui.fragment.addeditnote

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.databinding.ObservableBoolean
import io.reactivex.disposables.CompositeDisposable
import rmnvich.apps.notes.R
import rmnvich.apps.notes.domain.entity.Note
import rmnvich.apps.notes.domain.interactors.addeditnote.AddEditNoteInteractor
import rmnvich.apps.notes.presentation.utils.SnackbarMessage

class AddEditNoteViewModel(
        private val applicationContext: Application,
        private val disposables: CompositeDisposable,
        private val addEditNoteNotesInteractor: AddEditNoteInteractor
) : AndroidViewModel(applicationContext) {

    val bIsShowingProgressBar: ObservableBoolean = ObservableBoolean(false)

    private val mSnackbarMessage: SnackbarMessage = SnackbarMessage()

    private var mResponse: MutableLiveData<Note>? = null

    fun getSnackbar(): SnackbarMessage = mSnackbarMessage

    fun getNote(noteId: Int): LiveData<Note>? {
        if (mResponse == null) {
            mResponse = MutableLiveData()
            loadNote(noteId)
        }
        return mResponse
    }

    private fun loadNote(noteId: Int) {
        disposables.add(addEditNoteNotesInteractor.getNoteById(noteId)
                .doOnSubscribe { bIsShowingProgressBar.set(true) }
                .subscribe({
                    bIsShowingProgressBar.set(false)
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
        disposables.clear()
    }
}