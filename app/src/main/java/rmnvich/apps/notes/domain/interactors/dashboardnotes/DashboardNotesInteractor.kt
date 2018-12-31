package rmnvich.apps.notes.domain.interactors.dashboardnotes

import io.reactivex.Completable
import io.reactivex.Flowable
import rmnvich.apps.notes.data.common.Constants
import rmnvich.apps.notes.data.common.SchedulersProvider
import rmnvich.apps.notes.domain.entity.Note
import rmnvich.apps.notes.domain.repositories.NotesRepository
import java.util.concurrent.TimeUnit

open class DashboardNotesInteractor(
        private val notesRepository: NotesRepository,
        private val schedulersProvider: SchedulersProvider
) {

    fun getNotes(isFavorite: Boolean): Flowable<List<Note>> {
        return notesRepository.getAllNotes(isFavorite)
                .subscribeOn(schedulersProvider.io())
                .delay(Constants.DEFAULT_DELAY, TimeUnit.MILLISECONDS)
                .observeOn(schedulersProvider.ui())
    }

    fun getAllFilteredByColorNotes(color: Int, isFavorite: Boolean): Flowable<List<Note>> {
        return notesRepository.getAllFilteredByColorNotes(color, isFavorite)
                .subscribeOn(schedulersProvider.io())
                .delay(Constants.DEFAULT_DELAY, TimeUnit.MILLISECONDS)
                .observeOn(schedulersProvider.ui())
    }

    fun getAllFilteredByTagNotes(tagId: Int, isFavorite: Boolean): Flowable<List<Note>> {
        return notesRepository.getAllFilteredByTagNotes(tagId, isFavorite)
                .subscribeOn(schedulersProvider.io())
                .delay(Constants.DEFAULT_DELAY, TimeUnit.MILLISECONDS)
                .observeOn(schedulersProvider.ui())
    }

    fun updateIsFavoriteNote(noteId: Int, isFavorite: Boolean): Completable {
        return notesRepository.updateIsFavoriteNote(noteId, isFavorite)
                .subscribeOn(schedulersProvider.io())
                .observeOn(schedulersProvider.ui())
    }
}