package rmnvich.apps.notes.presentation.ui.fragment.addeditnote

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.databinding.ObservableBoolean
import io.reactivex.disposables.CompositeDisposable
import rmnvich.apps.notes.domain.interactors.addeditnote.AddEditNoteInteractor
import rmnvich.apps.notes.presentation.utils.SnackbarMessage

class AddEditNoteViewModel(
        private val applicationContext: Application,
        private val disposables: CompositeDisposable,
        private val addEditNoteNotesInteractor: AddEditNoteInteractor
) : AndroidViewModel(applicationContext) {

    val bIsShowingProgressBar: ObservableBoolean = ObservableBoolean(false)

    private val mSnackbarMessage: SnackbarMessage = SnackbarMessage()
}