package rmnvich.apps.notes.presentation.ui.fragment.dashboardnotes

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.databinding.ObservableBoolean
import io.reactivex.disposables.CompositeDisposable
import rmnvich.apps.notes.R
import rmnvich.apps.notes.domain.entity.Filter
import rmnvich.apps.notes.domain.entity.NoteWithTag
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

    private val mAddEditNoteEvent = SingleLiveEvent<Void>()
    private val mDeleteNoteEvent = SingleLiveEvent<Int>()

    private val mApplyFilterEvent = SingleLiveEvent<Void>()
    private val mResetFilterEvent = SingleLiveEvent<Void>()

    private val mSharedFilter = SingleLiveEvent<Filter>()

    private val mSnackbarMessage = SingleLiveEvent<Int>()

    private var mNotesResponse: MutableLiveData<List<NoteWithTag>>? = null
    private var mTagsResponse: MutableLiveData<List<Tag>>? = null

    fun getSnackbar(): SingleLiveEvent<Int> = mSnackbarMessage

    fun getEditNoteEvent(): SingleLiveEvent<Int> = mSelectedNoteId

    fun getAddNoteEvent(): SingleLiveEvent<Void> = mAddEditNoteEvent

    fun getDeleteNoteEvent(): SingleLiveEvent<Int> = mDeleteNoteEvent

    fun getSharedFilter(): SingleLiveEvent<Filter> = mSharedFilter

    fun getApplyFilterEvent(): SingleLiveEvent<Void> = mApplyFilterEvent

    fun getResetFilterEvent(): SingleLiveEvent<Void> = mResetFilterEvent

    fun applyFilter() = mApplyFilterEvent.call()

    fun resetFilter() = mResetFilterEvent.call()

    fun getNotes(): LiveData<List<NoteWithTag>>? {
        if (mNotesResponse == null) {
            mNotesResponse = MutableLiveData()
            loadNotes()
        }
        return mNotesResponse
    }

    //TODO: filter
    fun getFilteredNotes(colors: List<Int>, tags: List<Int>, isUnionConditions: Boolean) {
        DebugLogger.log("------------------------------------------")
        bIsShowingProgressBar.set(true)
        mCompositeDisposable.add(dashboardNotesInteractor
            .getAllFilteredNotes(colors, tags, isFavoriteNotes, isUnionConditions)
            .subscribe({
                for (i in 0 until it.size)
                    DebugLogger.log(it[i].noteText)
                bIsShowingProgressBar.set(false)
            }, {
                bIsShowingProgressBar.set(false)
                showSnackbarMessage(R.string.error_message)
            })
        )
    }

    fun getTags(): LiveData<List<Tag>>? {
        if (mTagsResponse == null) {
            mTagsResponse = MutableLiveData()
            loadTags()
        }
        return mTagsResponse
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
                .favoriteOrUnfavoriteNote(noteId, isFavorite)
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
        mCompositeDisposable.add(
            dashboardNotesInteractor.getAllTags()
                .subscribe({
                    bTagsIsEmpty.set(it.isEmpty())
                    mTagsResponse?.value = it
                }, { showSnackbarMessage(R.string.error_message) })
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
