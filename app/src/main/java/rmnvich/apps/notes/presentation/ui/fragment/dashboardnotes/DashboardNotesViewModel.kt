package rmnvich.apps.notes.presentation.ui.fragment.dashboardnotes

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.databinding.ObservableBoolean
import android.util.Log
import io.reactivex.disposables.CompositeDisposable
import rmnvich.apps.notes.R
import rmnvich.apps.notes.domain.entity.Color
import rmnvich.apps.notes.domain.entity.Note
import rmnvich.apps.notes.domain.entity.Tag
import rmnvich.apps.notes.domain.interactors.dashboardnotes.DashboardNotesInteractor
import rmnvich.apps.notes.domain.utils.SingleLiveEvent
import rmnvich.apps.notes.presentation.utils.DebugLogger

class DashboardNotesViewModel(
        private val dashboardNotesInteractor: DashboardNotesInteractor,
        private val isFavoriteNotes: Boolean
) : ViewModel() {

    val bIsShowingProgressBar: ObservableBoolean = ObservableBoolean(false)
    val bNotesIsEmpty: ObservableBoolean = ObservableBoolean(false)
    val bTagsIsEmpty: ObservableBoolean = ObservableBoolean(false)

    var bIsRecyclerNeedToScroll: Boolean = false

    private val mCompositeDisposable: CompositeDisposable = CompositeDisposable()

    private val mSelectedNoteId = SingleLiveEvent<Int>()

    private val mApplyFilterEvent = SingleLiveEvent<Void>()
    private val mResetFilterEvent = SingleLiveEvent<Void>()

    private val mAddEditNoteEvent = SingleLiveEvent<Void>()
    private val mDeleteNoteEvent = SingleLiveEvent<Int>()

    private val mSnackbarMessage = SingleLiveEvent<Int>()

    private var mNotesResponse: MutableLiveData<List<Note>>? = null
    private var mTagsResponse: MutableLiveData<List<Tag>>? = null

    fun forceUpdate() {
        mCompositeDisposable.clear()

        getNotes(true)
        getTags(true)
    }

    fun getNotes(forceUpdate: Boolean): LiveData<List<Note>>? {
        if (mNotesResponse == null) {
            mNotesResponse = MutableLiveData()
            loadNotes()
        }

        if (forceUpdate)
            loadNotes()

        return mNotesResponse
    }

    fun getTags(forceUpdate: Boolean): LiveData<List<Tag>>? {
        if (mTagsResponse == null) {
            mTagsResponse = MutableLiveData()
            loadTags()
        }

        if (forceUpdate)
            loadTags()

        return mTagsResponse
    }

    fun getFilteredNotes(colors: List<Color>, tags: List<Tag>): LiveData<List<Note>>? {
        for (i in 0 until tags.size)
            DebugLogger.log("tags = ${tags[i].name}")

        for (i in 0 until colors.size) {
            DebugLogger.log("colors = ${colors[i].colorName}")
        }
        return mNotesResponse
    }

    fun getSnackbar(): SingleLiveEvent<Int> = mSnackbarMessage

    fun getEditNoteEvent(): SingleLiveEvent<Int> = mSelectedNoteId

    fun getAddNoteEvent(): SingleLiveEvent<Void> = mAddEditNoteEvent

    fun getDeleteNoteEvent(): SingleLiveEvent<Int> = mDeleteNoteEvent

    fun getApplyFilterEvent(): SingleLiveEvent<Void> = mApplyFilterEvent

    fun getResetFilterEvent(): SingleLiveEvent<Void> = mResetFilterEvent

    fun resetFilter() {
        mResetFilterEvent.call()
    }

    fun applyFilter() {
        mApplyFilterEvent.call()
    }

    fun addNote() {
        mAddEditNoteEvent.call()
        bIsRecyclerNeedToScroll = true
    }

    fun editNote(noteId: Int?) {
        mSelectedNoteId.value = noteId
    }

    private var deletedItemPosition: Int = -1

    fun deleteNote(noteId: Int, position: Int) {
        deletedItemPosition = position

        mCompositeDisposable.add(
                dashboardNotesInteractor
                        .removeNoteToTrash(noteId)
                        .subscribe({ mDeleteNoteEvent.value = noteId },
                                { showSnackbarMessage(R.string.error_message) })
        )
    }

    fun restoreNote(noteId: Int) {
        if (deletedItemPosition == 0)
            bIsRecyclerNeedToScroll = true

        mCompositeDisposable.add(
                dashboardNotesInteractor
                        .restoreNote(noteId)
                        .subscribe({}, { showSnackbarMessage(R.string.error_message) })
        )
    }

    fun updateIsFavoriteNote(noteId: Int, isFavorite: Boolean) {
        mCompositeDisposable.add(
                dashboardNotesInteractor
                        .updateIsFavoriteNote(noteId, isFavorite)
                        .subscribe()
        )
    }

    private fun loadNotes() {
        mCompositeDisposable.add(dashboardNotesInteractor.getNotes(isFavoriteNotes)
                .doOnSubscribe { bIsShowingProgressBar.set(true) }
                .subscribe({
                    bIsShowingProgressBar.set(false)
                    bNotesIsEmpty.set(it.isEmpty())
                    mNotesResponse?.value = it
                }, {
                    bIsShowingProgressBar.set(false)
                    showSnackbarMessage(R.string.error_message)
                })
        )
    }

    private fun loadTags() {
        mCompositeDisposable.add(dashboardNotesInteractor.getAllTags()
                .doOnSubscribe { bIsShowingProgressBar.set(true) }
                .subscribe({
                    bIsShowingProgressBar.set(false)
                    bTagsIsEmpty.set(it.isEmpty())
                    mTagsResponse?.value = it
                }, {
                    bIsShowingProgressBar.set(false)
                    showSnackbarMessage(R.string.error_message)
                }))
    }

    private fun showSnackbarMessage(message: Int?) {
        mSnackbarMessage.value = message
    }

    override fun onCleared() {
        super.onCleared()
        mCompositeDisposable.clear()
    }
}
