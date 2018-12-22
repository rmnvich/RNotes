package rmnvich.apps.notes.domain.interactors.dashboardnotes

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.LiveDataReactiveStreams
import android.util.Log.d
import io.reactivex.Flowable
import rmnvich.apps.notes.data.common.Constants
import rmnvich.apps.notes.data.common.SchedulersProvider
import rmnvich.apps.notes.domain.entity.Note
import rmnvich.apps.notes.domain.repositories.NotesRepository
import rmnvich.apps.notes.presentation.utils.DebugLogger
import java.util.concurrent.TimeUnit

class DashboardNotesInteractor(
    private val notesRepository: NotesRepository,
    private val schedulersProvider: SchedulersProvider
) {

    fun getAllNotes(): LiveData<List<Note>> {
        return LiveDataReactiveStreams.fromPublisher(
            notesRepository.getAllNotes()
                .subscribeOn(schedulersProvider.io())
                .delay(Constants.DEFAULT_DELAY, TimeUnit.MILLISECONDS)
                .observeOn(schedulersProvider.ui())
        )
    }

    fun getAllFilteredByColorNotes(color: Int): LiveData<List<Note>> {
        return LiveDataReactiveStreams.fromPublisher(
            notesRepository.getAllFilteredByColorNotes(color)
                .subscribeOn(schedulersProvider.io())
                .delay(Constants.DEFAULT_DELAY, TimeUnit.MILLISECONDS)
                .observeOn(schedulersProvider.ui())
        )
    }

    fun getAllFilteredByTagNotes(tagName: String): LiveData<List<Note>> {
        return LiveDataReactiveStreams.fromPublisher(
            notesRepository.getAllFilteredByTagNotes(tagName)
                .subscribeOn(schedulersProvider.io())
                .delay(Constants.DEFAULT_DELAY, TimeUnit.MILLISECONDS)
                .observeOn(schedulersProvider.ui())
        )
    }

    fun getAllFavoritesNotes(): LiveData<List<Note>> {
        return LiveDataReactiveStreams.fromPublisher(
            notesRepository.getAllFavoriteNotes()
                .subscribeOn(schedulersProvider.io())
                .delay(Constants.DEFAULT_DELAY, TimeUnit.MILLISECONDS)
                .observeOn(schedulersProvider.ui())
        )
    }
}