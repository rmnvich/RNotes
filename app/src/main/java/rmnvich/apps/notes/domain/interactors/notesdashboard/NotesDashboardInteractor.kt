package rmnvich.apps.notes.domain.interactors.notesdashboard

import io.reactivex.Flowable
import rmnvich.apps.notes.data.common.Constants
import rmnvich.apps.notes.data.common.SchedulersProvider
import rmnvich.apps.notes.domain.entity.Note
import rmnvich.apps.notes.domain.repositories.NotesRepository
import java.util.concurrent.TimeUnit

class NotesDashboardInteractor(
    private val notesRepository: NotesRepository,
    private val schedulersProvider: SchedulersProvider
) {

    fun getAllNotes(): Flowable<List<Note>> {
        return notesRepository.getAllNotes()
            .subscribeOn(schedulersProvider.io())
            .delay(Constants.DEFAULT_DELAY, TimeUnit.MILLISECONDS)
            .observeOn(schedulersProvider.ui())
    }

    fun getAllFilteredByColorNotes(color: Int): Flowable<List<Note>> {
        return notesRepository.getAllFilteredByColorNotes(color)
            .subscribeOn(schedulersProvider.io())
            .delay(Constants.DEFAULT_DELAY, TimeUnit.MILLISECONDS)
            .observeOn(schedulersProvider.ui())
    }

    fun getAllFilteredByTagNotes(tagName: String): Flowable<List<Note>> {
        return notesRepository.getAllFilteredByTagNotes(tagName)
            .subscribeOn(schedulersProvider.io())
            .delay(Constants.DEFAULT_DELAY, TimeUnit.MILLISECONDS)
            .observeOn(schedulersProvider.ui())
    }

    fun getAllFavoritesNotes(): Flowable<List<Note>> {
        return notesRepository.getAllFavoriteNotes()
            .subscribeOn(schedulersProvider.io())
            .delay(Constants.DEFAULT_DELAY, TimeUnit.MILLISECONDS)
            .observeOn(schedulersProvider.ui())
    }
}