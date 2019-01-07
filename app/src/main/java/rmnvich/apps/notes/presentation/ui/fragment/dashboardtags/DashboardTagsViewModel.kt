package rmnvich.apps.notes.presentation.ui.fragment.dashboardtags

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.databinding.ObservableBoolean
import android.databinding.ObservableField
import io.reactivex.disposables.CompositeDisposable
import rmnvich.apps.notes.R
import rmnvich.apps.notes.domain.entity.Tag
import rmnvich.apps.notes.domain.interactors.dashboardtags.DashboardTagsInteractor
import rmnvich.apps.notes.domain.utils.SingleLiveEvent

class DashboardTagsViewModel(
        private val dashboardTagsInteractor: DashboardTagsInteractor
) : ViewModel() {


    val bIsShowingProgressBar: ObservableBoolean = ObservableBoolean(false)
    val bDataIsEmpty: ObservableBoolean = ObservableBoolean(false)

    val tagName: ObservableField<String> = ObservableField("")

    private val mCompositeDisposable: CompositeDisposable = CompositeDisposable()

    private val mSnackbarMessage: SingleLiveEvent<Int> = SingleLiveEvent()
    private var mResponse: MutableLiveData<List<Tag>>? = null

    private var existsTag: Tag? = null

    fun getSnackbar(): SingleLiveEvent<Int> = mSnackbarMessage

    fun forceUpdate() {
        getTags(true)
    }

    fun getTags(forceUpdate: Boolean): LiveData<List<Tag>>? {
        if (mResponse == null) {
            mResponse = MutableLiveData()
            loadTags()
        }

        if (forceUpdate) {
            mCompositeDisposable.clear()
            loadTags()
        }

        return mResponse
    }

    fun insertOrUpdateTag() {
        if (existsTag == null) {
            existsTag = Tag()
        }
        existsTag?.name = tagName.get()!!

        if (!existsTag?.name?.isEmpty()!!) {
            bIsShowingProgressBar.set(true)
            mCompositeDisposable.add(dashboardTagsInteractor
                    .insertOrUpdateTag(existsTag!!)
                    .subscribe({
                        bIsShowingProgressBar.set(false)
                        tagName.set("")
                    }, {
                        bIsShowingProgressBar.set(false)
                        showSnackbarMessage(R.string.error_message)
                    }))
        } else showSnackbarMessage(R.string.empty_tag_error)
    }

    private fun loadTags() {
        mCompositeDisposable.add(dashboardTagsInteractor.getAllTags()
                .doOnSubscribe { bIsShowingProgressBar.set(true) }
                .subscribe({
                    bIsShowingProgressBar.set(false)
                    bDataIsEmpty.set(it.isEmpty())
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
        mCompositeDisposable.clear()
    }
}
