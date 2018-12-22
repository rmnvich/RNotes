package rmnvich.apps.notes.presentation.ui.fragment.dashboardnotes

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.LiveData
import android.databinding.ObservableBoolean
import io.reactivex.disposables.CompositeDisposable
import rmnvich.apps.notes.domain.interactors.dashboardnotes.DashboardNotesInteractor
import android.arch.lifecycle.MutableLiveData
import rmnvich.apps.notes.domain.interactors.Response


class DashboardNotesViewModel(
        private val applicationContext: Application,
        private val disposables: CompositeDisposable,
        private val dashboardNotesInteractor: DashboardNotesInteractor
) : AndroidViewModel(applicationContext) {

    val bIsShowingProgressBar: ObservableBoolean = ObservableBoolean(false)
    val bDataIsEmpty: ObservableBoolean = ObservableBoolean(false)
    val bIsError: ObservableBoolean = ObservableBoolean(false)

    private var mResponse: MutableLiveData<Response>? = null

    fun response(): LiveData<Response>? {
        if (mResponse == null) {
            mResponse = MutableLiveData()
            loadNotes()
        }
        return mResponse
    }

    private fun loadNotes() {
        bIsShowingProgressBar.set(true)
        disposables.add(dashboardNotesInteractor.getAllNotes()
                .doOnSubscribe { mResponse?.value = Response.onLoading() }
                .subscribe({ mResponse?.value = Response.onSuccess(it) },
                        { mResponse?.value = Response.onError(it) })
        )
    }

    override fun onCleared() {
        super.onCleared()
        disposables.clear()
    }

}
