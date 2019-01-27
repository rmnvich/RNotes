package rmnvich.apps.notes.data.repositories

import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Single
import rmnvich.apps.notes.data.repositories.datasource.Database
import rmnvich.apps.notes.domain.entity.Note
import rmnvich.apps.notes.domain.entity.Tag
import rmnvich.apps.notes.domain.repositories.NotesRepository

class NotesRepositoryImpl(database: Database) : NotesRepository {

    private val noteDao = database.noteDao()

    override fun getAllNotes(isFavorite: Boolean): Flowable<List<Note>> {
        return if (isFavorite) {
            getAllFavoriteNotes()
        } else noteDao.getAllNotes(false)
    }

    //TODO: filter
    override fun getAllFilteredNotes(colors: List<Int>, tags: List<Tag>, isFavorite: Boolean): Flowable<List<Note>> {
//        return noteDao.getAllFilteredNotes(colors, tags, isFavorite, false)
        return getAllFavoriteNotes()
    }

    override fun getAllFavoriteNotes(): Flowable<List<Note>> {
        return noteDao.getAllFavoritesNotes(true, false)
    }

    override fun getDeletedNotes(): Flowable<List<Note>> {
        return noteDao.getDeletedNotes(true)
    }

    override fun getNoteById(noteId: Int): Single<Note> {
        return noteDao.getNoteById(noteId)
    }

    override fun insertOrUpdateNote(note: Note): Completable {
        return Completable.fromAction { noteDao.insertNote(note) }
    }

    override fun updateIsFavoriteNote(noteId: Int, isFavorite: Boolean): Completable {
        return Completable.fromAction { noteDao.updateIsFavoriteNote(noteId, isFavorite) }
    }

    override fun updateIsDeleteNote(noteId: Int, isDeleted: Boolean): Completable {
        return Completable.fromAction { noteDao.updateIsDeletedNote(noteId, isDeleted, false) }
    }

    override fun deleteNote(note: Note): Completable {
        return Completable.fromAction { noteDao.deleteNote(note) }
    }

    override fun deleteNotes(notes: List<Note>): Completable {
        return Completable.fromAction { noteDao.deleteNotes(notes) }
    }

    override fun restoreNotes(notes: List<Note>): Completable {
        for (i in 0 until notes.size) {
            notes[i].isDeleted = false
            notes[i].isSelected = false
        }
        return Completable.fromAction { noteDao.updateNotes(notes) }
    }
}