package rmnvich.apps.notes.domain.repositories

import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Single
import rmnvich.apps.notes.domain.entity.Note
import rmnvich.apps.notes.domain.entity.NoteWithTag

interface NotesRepository {

    fun getAllNotes(isFavorite: Boolean): Flowable<List<NoteWithTag>>

    fun getAllFilteredNotes(colors: List<Int>, tags: List<Int>, isFavorite: Boolean,
                            isUnionConditions: Boolean, isOnlyWithPicture: Boolean,
                            isOnlyLockedNotes: Boolean): Flowable<List<NoteWithTag>>

    fun getSearchedNotes(query: String, isFavorite: Boolean): Flowable<List<NoteWithTag>>

    fun getNoteById(noteId: Int): Single<NoteWithTag>

    fun insertNote(note: Note): Completable

    fun updateNote(note: Note, noteId: Int): Completable

    fun favoriteOrUnfavoriteNote(noteId: Int, isFavorite: Boolean): Completable

    fun getDeletedNotes(): Flowable<List<Note>>

    fun deleteOrRestoreNote(noteId: Int, isDeleted: Boolean): Completable

    fun deleteNote(note: Note): Completable

    fun deleteNotes(notes: List<Note>): Completable

    fun restoreNotes(notes: List<Note>): Completable
}