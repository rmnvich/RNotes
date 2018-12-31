package rmnvich.apps.notes.data.repositories

import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import rmnvich.apps.notes.data.common.Constants.DEFAULT_DELAY
import rmnvich.apps.notes.data.repositories.datasource.Database
import rmnvich.apps.notes.domain.entity.Note
import rmnvich.apps.notes.domain.repositories.NotesRepository
import java.util.concurrent.TimeUnit

class NotesRepositoryImpl(database: Database) : NotesRepository {

    private val noteDao = database.noteDao()

    override fun getAllNotes(isFavorite: Boolean): Flowable<List<Note>> {
        return if (isFavorite) {
            getAllFavoriteNotes()
        } else noteDao.getAllNotes()
    }

    override fun getAllFilteredByColorNotes(color: Int, isFavorite: Boolean): Flowable<List<Note>> {
        return noteDao.getAllFilteredByColorNotes(color, isFavorite)
    }

    override fun getAllFilteredByTagNotes(tagId: Int, isFavorite: Boolean): Flowable<List<Note>> {
        return noteDao.getAllFilteredByTagNotes(tagId, isFavorite)
    }

    override fun getAllFavoriteNotes(): Flowable<List<Note>> {
        return noteDao.getAllFavoritesNotes(true)
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

    override fun deleteNote(note: Note): Completable {
        return Completable.fromAction { noteDao.deleteNote(note) }
    }

    override fun deleteNotes(notes: List<Note>): Completable {
        return Completable.fromAction { noteDao.deleteNotes(notes) }
    }
}