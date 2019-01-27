package rmnvich.apps.notes.domain.repositories

import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Single
import rmnvich.apps.notes.domain.entity.Note
import rmnvich.apps.notes.domain.entity.Tag

interface NotesRepository {

    fun getAllNotes(isFavorite: Boolean): Flowable<List<Note>>

    fun getDeletedNotes(): Flowable<List<Note>>

    fun getAllFilteredNotes(colors: List<Int>, tags: List<Tag>, isFavorite: Boolean): Flowable<List<Note>>

    fun getAllFavoriteNotes(): Flowable<List<Note>>

    fun getNoteById(noteId: Int): Single<Note>

    fun insertOrUpdateNote(note: Note): Completable

    fun updateIsFavoriteNote(noteId: Int, isFavorite: Boolean): Completable

    fun updateIsDeleteNote(noteId: Int, isDeleted: Boolean): Completable

    fun deleteNote(note: Note): Completable

    fun deleteNotes(notes: List<Note>): Completable

    fun restoreNotes(notes: List<Note>): Completable
}