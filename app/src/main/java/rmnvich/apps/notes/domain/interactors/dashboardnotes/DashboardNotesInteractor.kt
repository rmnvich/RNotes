package rmnvich.apps.notes.domain.interactors.dashboardnotes

import io.reactivex.Completable
import io.reactivex.Flowable
import rmnvich.apps.notes.data.common.Constants
import rmnvich.apps.notes.data.common.SchedulersProvider
import rmnvich.apps.notes.domain.entity.Note
import rmnvich.apps.notes.domain.entity.NoteWithTag
import rmnvich.apps.notes.domain.entity.Tag
import rmnvich.apps.notes.domain.repositories.NotesRepository
import rmnvich.apps.notes.domain.repositories.TagsRepository
import java.util.concurrent.TimeUnit

open class DashboardNotesInteractor(
        private val notesRepository: NotesRepository,
        private val tagsRepository: TagsRepository,
        private val schedulersProvider: SchedulersProvider
) {

    fun getNotes(isFavorite: Boolean): Flowable<List<NoteWithTag>> {
        return notesRepository.getAllNotes(isFavorite)
                .subscribeOn(schedulersProvider.io())
                .delay(Constants.DEFAULT_DELAY, TimeUnit.MILLISECONDS)
                .observeOn(schedulersProvider.ui())
    }

    fun getAllTags(): Flowable<List<Tag>> {
        return tagsRepository.getAllTags()
                .subscribeOn(schedulersProvider.io())
                .delay(Constants.DEFAULT_DELAY, TimeUnit.MILLISECONDS)
                .observeOn(schedulersProvider.ui())
    }

    fun getAllFilteredNotes(colors: List<Int>, tags: List<Int>, isFavorite: Boolean,
                            isUnionConditions: Boolean, isOnlyWithPicture: Boolean): Flowable<List<NoteWithTag>> {
        return notesRepository.getAllFilteredNotes(colors, tags, isFavorite,
                isUnionConditions, isOnlyWithPicture)
                .subscribeOn(schedulersProvider.io())
                .delay(Constants.DEFAULT_DELAY, TimeUnit.MILLISECONDS)
                .observeOn(schedulersProvider.ui())
    }

    fun favoriteOrUnfavoriteNote(noteId: Int, isFavorite: Boolean): Completable {
        return notesRepository.favoriteOrUnfavoriteNote(noteId, isFavorite)
                .subscribeOn(schedulersProvider.io())
                .observeOn(schedulersProvider.ui())
    }

    fun removeNoteToTrash(noteId: Int): Completable {
        return notesRepository.deleteOrRestoreNote(noteId, true)
                .subscribeOn(schedulersProvider.io())
                .observeOn(schedulersProvider.ui())
    }

    fun restoreNote(noteId: Int): Completable {
        return notesRepository.deleteOrRestoreNote(noteId, false)
                .subscribeOn(schedulersProvider.io())
                .observeOn(schedulersProvider.ui())
    }
}