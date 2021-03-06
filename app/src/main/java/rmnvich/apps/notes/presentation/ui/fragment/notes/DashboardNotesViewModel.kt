package rmnvich.apps.notes.presentation.ui.fragment.notes

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.databinding.ObservableBoolean
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import rmnvich.apps.notes.R
import rmnvich.apps.notes.domain.entity.Filter
import rmnvich.apps.notes.domain.entity.NoteBundle
import rmnvich.apps.notes.domain.entity.NoteWithTag
import rmnvich.apps.notes.domain.entity.Tag
import rmnvich.apps.notes.domain.interactors.dashboardnotes.DashboardNotesInteractor
import rmnvich.apps.notes.domain.utils.SingleLiveEvent

class DashboardNotesViewModel(
    private val dashboardNotesInteractor: DashboardNotesInteractor,
    private val isFavoriteNotes: Boolean
) : ViewModel() {

    private val mCompositeDisposable: CompositeDisposable = CompositeDisposable()

    val bIsShowingProgressBar: ObservableBoolean = ObservableBoolean(false)

    val bNotesIsEmpty: ObservableBoolean = ObservableBoolean(false)

    val bSearchedNotesIsEmpty: ObservableBoolean = ObservableBoolean(false)

    val bTagsIsEmpty: ObservableBoolean = ObservableBoolean(false)

    var bIsRecyclerNeedToScroll: Boolean = false

    private var deletedItemPosition: Int = -1

    private var mNotesDisposable: Disposable? = null

    private var mFilteredNotesDisposable: Disposable? = null

    private var mSearchedNotesDisposable: Disposable? = null

    private val mEditNoteEvent = SingleLiveEvent<NoteBundle>()

    private val mAddNoteEvent = SingleLiveEvent<Void>()

    private val mDeleteNoteEvent = SingleLiveEvent<Int>()

    private val mApplyFilterEvent = SingleLiveEvent<Void>()

    private val mResetFilterEvent = SingleLiveEvent<Void>()

    private val mSharedFilter = SingleLiveEvent<Filter>()

    private val mSnackbarMessage = SingleLiveEvent<Int>()

    private var mNotesResponse: MutableLiveData<List<NoteWithTag>>? = null

    private var mSearchedNotesResponse: MutableLiveData<List<NoteWithTag>>? = null

    private var mTagsResponse: MutableLiveData<List<Tag>>? = null

    fun getSnackbar(): SingleLiveEvent<Int> = mSnackbarMessage

    fun getEditNoteEvent(): SingleLiveEvent<NoteBundle> = mEditNoteEvent

    fun getAddNoteEvent(): SingleLiveEvent<Void> = mAddNoteEvent

    fun getDeleteNoteEvent(): SingleLiveEvent<Int> = mDeleteNoteEvent

    fun getSharedFilter(): SingleLiveEvent<Filter> = mSharedFilter

    fun getApplyFilterEvent(): SingleLiveEvent<Void> = mApplyFilterEvent

    fun getResetFilterEvent(): SingleLiveEvent<Void> = mResetFilterEvent

    fun applyFilter() = mApplyFilterEvent.call()

    fun resetFilter() = mResetFilterEvent.call()

    private fun showSnackbarMessage(message: Int?) {
        mSnackbarMessage.value = message
    }

    fun getNotes(): LiveData<List<NoteWithTag>>? {
        if (mNotesResponse == null) {
            mNotesResponse = MutableLiveData()
            loadNotes()
        }
        return mNotesResponse
    }

    fun getSearchedNotes(): LiveData<List<NoteWithTag>>? {
        if (mSearchedNotesResponse == null) {
            mSearchedNotesResponse = MutableLiveData()
            loadSearchedNotes("")
        }
        return mSearchedNotesResponse
    }

    fun getTags(): LiveData<List<Tag>>? {
        if (mTagsResponse == null) {
            mTagsResponse = MutableLiveData()
            loadTags()
        }
        return mTagsResponse
    }

    fun applyFilterToNotes(
        colors: List<Int>, tags: List<Int>, isUnionConditions: Boolean,
        isOnlyWithPicture: Boolean, isOnlyLockedNotes: Boolean
    ) {
        if (mNotesDisposable != null && !mNotesDisposable!!.isDisposed)
            mNotesDisposable!!.dispose()

        if (mFilteredNotesDisposable != null && !mFilteredNotesDisposable!!.isDisposed)
            mFilteredNotesDisposable!!.dispose()

        bIsShowingProgressBar.set(true)
        if (colors.isEmpty() && tags.isEmpty()) {
            loadNotes()
        } else loadFilteredNotes(
            colors, tags, isUnionConditions,
                isOnlyWithPicture, isOnlyLockedNotes
        )

    }

    fun searchNotes(query: String) {
        loadSearchedNotes(query)
    }

    fun addNote() {
        mAddNoteEvent.call()
        bIsRecyclerNeedToScroll = true
    }

    fun editNote(noteBundle: NoteBundle?) {
        mEditNoteEvent.value = noteBundle
    }

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
        mNotesDisposable = dashboardNotesInteractor
            .getNotes(isFavoriteNotes)
            .doOnSubscribe { bIsShowingProgressBar.set(true) }
            .subscribe({
                bIsShowingProgressBar.set(false)
                bNotesIsEmpty.set(it.isEmpty())
                mNotesResponse?.value = it
            }, {
                bIsShowingProgressBar.set(false)
                showSnackbarMessage(R.string.error_message)
            })
    }

    private fun loadSearchedNotes(query: String) {
        disposeSearchedNotesDisposable()
        mSearchedNotesDisposable = dashboardNotesInteractor
            .getSearchedNotes(query, isFavoriteNotes)
            .doOnSubscribe { bIsShowingProgressBar.set(true) }
            .subscribe({
                bIsShowingProgressBar.set(false)
                bSearchedNotesIsEmpty.set(it.isEmpty())
                mSearchedNotesResponse?.value = it
            }, {
                bIsShowingProgressBar.set(false)
                showSnackbarMessage(R.string.error_message)
            })
    }

    private fun loadFilteredNotes(
        colors: List<Int>, tags: List<Int>, isUnionConditions: Boolean,
        isOnlyWithPicture: Boolean, isOnlyLockedNotes: Boolean
    ) {
        mFilteredNotesDisposable = dashboardNotesInteractor
            .getAllFilteredNotes(
                colors, tags, isFavoriteNotes, isUnionConditions,
                    isOnlyWithPicture, isOnlyLockedNotes
            )
            .doOnSubscribe { bIsShowingProgressBar.set(true) }
            .subscribe({
                bIsShowingProgressBar.set(false)
                bNotesIsEmpty.set(it.isEmpty())
                mNotesResponse?.value = it
            }, {
                bIsShowingProgressBar.set(false)
                showSnackbarMessage(R.string.error_message)
            })
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

    fun disposeSearchedNotesDisposable() {
        if (mSearchedNotesDisposable != null && !mSearchedNotesDisposable!!.isDisposed)
            mSearchedNotesDisposable!!.dispose()
    }

    override fun onCleared() {
        super.onCleared()
        mCompositeDisposable.clear()

        if (mNotesDisposable != null && !mNotesDisposable!!.isDisposed)
            mNotesDisposable!!.dispose()

        if (mFilteredNotesDisposable != null && !mFilteredNotesDisposable!!.isDisposed)
            mFilteredNotesDisposable!!.dispose()
    }
}
