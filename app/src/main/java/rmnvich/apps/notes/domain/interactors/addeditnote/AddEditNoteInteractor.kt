package rmnvich.apps.notes.domain.interactors.addeditnote

import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Single
import rmnvich.apps.notes.data.common.Constants
import rmnvich.apps.notes.data.common.SchedulersProvider
import rmnvich.apps.notes.domain.entity.Note
import rmnvich.apps.notes.domain.entity.Tag
import rmnvich.apps.notes.domain.repositories.NotesRepository
import rmnvich.apps.notes.domain.repositories.TagsRepository
import java.util.concurrent.TimeUnit

class AddEditNoteInteractor(
    private val notesRepository: NotesRepository,
    private val schedulersProvider: SchedulersProvider
) {

    fun getNoteById(noteId: Int): Single<Note> {
        return notesRepository.getNoteById(noteId)
            .subscribeOn(schedulersProvider.io())
            .observeOn(schedulersProvider.ui())
    }

    fun insertOrUpdateNote(note: Note): Completable {
        return notesRepository.insertOrUpdateNote(note)
            .subscribeOn(schedulersProvider.io())
            .observeOn(schedulersProvider.ui())
    }
}