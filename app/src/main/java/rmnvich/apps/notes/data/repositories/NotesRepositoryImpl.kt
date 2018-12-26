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

    override fun getAllNotes(): Flowable<List<Note>> {
        return noteDao.getAllNotes()
    }

    override fun getAllFilteredByColorNotes(color: Int): Flowable<List<Note>> {
        return noteDao.getAllFilteredByColorNotes(color)
    }

    override fun getAllFilteredByTagNotes(tagId: Int): Flowable<List<Note>> {
        return noteDao.getAllFilteredByTagNotes(tagId)
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

    override fun deleteNote(note: Note): Completable {
        return Completable.fromAction { noteDao.deleteNote(note) }
    }

    override fun deleteNotes(notes: List<Note>): Completable {
        return Completable.fromAction { noteDao.deleteNotes(notes) }
    }
}