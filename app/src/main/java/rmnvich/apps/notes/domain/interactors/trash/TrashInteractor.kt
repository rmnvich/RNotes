package rmnvich.apps.notes.domain.interactors.trash

import io.reactivex.Completable
import io.reactivex.Flowable
import rmnvich.apps.notes.data.common.SchedulersProvider
import rmnvich.apps.notes.domain.entity.Note
import rmnvich.apps.notes.domain.repositories.NotesRepository

class TrashInteractor(
        private val notesRepository: NotesRepository,
        private val schedulersProvider: SchedulersProvider
) {

    fun getDeletedNotes(): Flowable<List<Note>> {
        return notesRepository.getDeletedNotes()
                .subscribeOn(schedulersProvider.io())
                .observeOn(schedulersProvider.ui())
    }

    fun restoreNote(noteId: Int): Completable {
        return notesRepository.updateIsDeleteNote(noteId, false)
                .subscribeOn(schedulersProvider.io())
                .observeOn(schedulersProvider.ui())
    }

    fun deleteNote(note: Note): Completable {
        return notesRepository.deleteNote(note)
                .subscribeOn(schedulersProvider.io())
                .observeOn(schedulersProvider.ui())
    }

    fun deleteNotes(notes: List<Note>): Completable {
        return notesRepository.deleteNotes(notes)
                .subscribeOn(schedulersProvider.io())
                .observeOn(schedulersProvider.ui())
    }
}