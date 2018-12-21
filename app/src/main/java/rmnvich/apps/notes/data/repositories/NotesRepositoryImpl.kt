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
                .subscribeOn(Schedulers.io())
                .delay(DEFAULT_DELAY, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
    }

    override fun getAllFilteredByColorNotes(color: Int): Flowable<List<Note>> {
        return noteDao.getAllFilteredByColorNotes(color)
                .subscribeOn(Schedulers.io())
                .delay(DEFAULT_DELAY, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
    }

    override fun getAllFilteredByTagNotes(tagName: String): Flowable<List<Note>> {
        return noteDao.getAllFilteredByTagNotes(tagName)
                .subscribeOn(Schedulers.io())
                .delay(DEFAULT_DELAY, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
    }

    override fun getAllFavoritesNotes(): Flowable<List<Note>> {
        return noteDao.getAllFavoritesNotes(true)
                .subscribeOn(Schedulers.io())
                .delay(DEFAULT_DELAY, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
    }

    override fun getNoteById(noteId: Int): Single<Note> {
        return noteDao.getNoteById(noteId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
    }

    override fun insertNote(note: Note): Completable {
        return Completable.fromAction {
            noteDao.insertNote(note)
        }.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
    }

    override fun deleteNote(note: Note): Completable {
        return Completable.fromAction {
            noteDao.deleteNote(note)
        }.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
    }

    override fun deleteNotes(notes: List<Note>): Completable {
        return Completable.fromAction {
            noteDao.deleteNotes(notes)
        }.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
    }
}