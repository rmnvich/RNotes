package rmnvich.apps.notes.data.repositories

import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Single
import rmnvich.apps.notes.data.repositories.datasource.Database
import rmnvich.apps.notes.domain.entity.Note
import rmnvich.apps.notes.domain.entity.NoteWithTag
import rmnvich.apps.notes.domain.entity.Tag
import rmnvich.apps.notes.domain.repositories.NotesRepository

class NotesRepositoryImpl(database: Database) : NotesRepository {

    private val noteDao = database.noteDao()

    override fun getAllNotes(isFavorite: Boolean): Flowable<List<NoteWithTag>> {
        return if (isFavorite) getAllFavoriteNotes() else noteDao.getAllNotes(false)
    }

    override fun getAllFilteredNotes(
        colors: List<Int>,
        tags: List<Tag>,
        isFavorite: Boolean
    ): Flowable<List<NoteWithTag>> {
        return getAllFavoriteNotes()
    }

    override fun getAllFavoriteNotes(): Flowable<List<NoteWithTag>> {
        return noteDao.getAllFavoritesNotes(true, false)
    }

    override fun getNoteById(noteId: Int): Single<NoteWithTag> {
        return noteDao.getNoteWithTagByNoteId(noteId)
    }

    override fun insertNote(note: Note): Completable {
        return Completable.fromAction { noteDao.insertNote(note) }
    }

    override fun updateNote(note: Note, noteId: Int): Completable {
        return noteDao.getNoteById(noteId)
            .flatMapCompletable {
                Completable.fromAction {
                    it.text = note.text
                    it.color = note.color
                    it.imagePath = note.imagePath
                    it.timestamp = note.timestamp
                    it.tagId = note.tagId
                    noteDao.updateNote(it)
                }
            }
    }

    override fun favoriteOrUnfavoriteNote(noteId: Int, isFavorite: Boolean): Completable {
        return Completable.fromAction { noteDao.favoriteOrUnfavoriteNote(noteId, isFavorite) }
    }

    override fun getDeletedNotes(): Flowable<List<Note>> {
        return noteDao.getDeletedNotes(true)
    }

    override fun deleteOrRestoreNote(noteId: Int, isDeleted: Boolean): Completable {
        return Completable.fromAction { noteDao.deleteOrRestoreNote(noteId, isDeleted) }
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
        }
        return Completable.fromAction { noteDao.updateNotes(notes) }
    }
}