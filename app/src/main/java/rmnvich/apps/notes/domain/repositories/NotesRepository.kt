package rmnvich.apps.notes.domain.repositories

import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Single
import rmnvich.apps.notes.domain.entity.Note

interface NotesRepository {

    fun getAllNotes(): Flowable<List<Note>>

    fun getAllFilteredByColorNotes(color: Int): Flowable<List<Note>>

    fun getAllFilteredByTagNotes(tagId: Int): Flowable<List<Note>>

    fun getAllFavoriteNotes(): Flowable<List<Note>>

    fun getNoteById(noteId: Int): Single<Note>

    fun insertOrUpdateNote(note: Note): Completable

    fun deleteNote(note: Note): Completable

    fun deleteNotes(notes: List<Note>): Completable
}