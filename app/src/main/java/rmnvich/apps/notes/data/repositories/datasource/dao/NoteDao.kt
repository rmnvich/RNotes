package rmnvich.apps.notes.data.repositories.datasource.dao

import android.arch.persistence.room.*
import io.reactivex.Flowable
import io.reactivex.Single
import rmnvich.apps.notes.domain.entity.Note

@Dao
interface NoteDao {

    @Query("SELECT * FROM note ORDER BY timestamp DESC")
    fun getAllNotes(): Flowable<List<Note>>

    @Query("SELECT * FROM note WHERE color LIKE :color AND isFavorite = :isFavorite ORDER BY timestamp DESC")
    fun getAllFilteredByColorNotes(color: Int, isFavorite: Boolean): Flowable<List<Note>>

    @Query("SELECT * FROM note WHERE tagId LIKE :tagId AND isFavorite = :isFavorite ORDER BY timestamp DESC")
    fun getAllFilteredByTagNotes(tagId: Int, isFavorite: Boolean): Flowable<List<Note>>

    @Query("SELECT * FROM note WHERE isFavorite LIKE :isFavorite ORDER BY timestamp DESC")
    fun getAllFavoritesNotes(isFavorite: Boolean): Flowable<List<Note>>

    @Query("SELECT * FROM note WHERE noteId = :noteId")
    fun getNoteById(noteId: Int): Single<Note>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertNote(note: Note)

    @Delete
    fun deleteNote(note: Note)

    @Delete
    fun deleteNotes(notes: List<Note>)
}