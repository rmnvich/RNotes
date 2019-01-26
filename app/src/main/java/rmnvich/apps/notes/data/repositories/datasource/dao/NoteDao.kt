package rmnvich.apps.notes.data.repositories.datasource.dao

import android.arch.persistence.room.*
import io.reactivex.Flowable
import io.reactivex.Single
import rmnvich.apps.notes.domain.entity.Note

@Dao
interface NoteDao {

    @Query("SELECT * FROM note WHERE isDeleted LIKE :isDeleted ORDER BY timestamp DESC")
    fun getAllNotes(isDeleted: Boolean): Flowable<List<Note>>

    @Query("SELECT * FROM note WHERE isDeleted LIKE :isDeleted ORDER BY timestamp DESC")
    fun getDeletedNotes(isDeleted: Boolean): Flowable<List<Note>>

    @Query("SELECT * FROM note WHERE color LIKE :color AND isFavorite = :isFavorite AND isDeleted LIKE :isDeleted ORDER BY timestamp DESC")
    fun getAllFilteredByColorNotes(color: Int, isFavorite: Boolean, isDeleted: Boolean): Flowable<List<Note>>

    @Query("SELECT * FROM note WHERE tagId LIKE :tagId AND isFavorite = :isFavorite AND isDeleted LIKE :isDeleted ORDER BY timestamp DESC")
    fun getAllFilteredByTagNotes(tagId: Int, isFavorite: Boolean, isDeleted: Boolean): Flowable<List<Note>>

    @Query("SELECT * FROM note WHERE isFavorite LIKE :isFavorite AND isDeleted LIKE :isDeleted ORDER BY timestamp DESC")
    fun getAllFavoritesNotes(isFavorite: Boolean, isDeleted: Boolean): Flowable<List<Note>>

    @Query("SELECT * FROM note WHERE noteId = :noteId")
    fun getNoteById(noteId: Int): Single<Note>

    @Query("UPDATE note SET isFavorite = :isFavorite WHERE noteId = :noteId")
    fun updateIsFavoriteNote(noteId: Int, isFavorite: Boolean)

    @Query("UPDATE note SET isDeleted = :isDeleted AND isSelected = :isSelected WHERE noteId = :noteId")
    fun updateIsDeletedNote(noteId: Int, isDeleted: Boolean, isSelected: Boolean)

    @Update
    fun updateNotes(notes: List<Note>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertNote(note: Note)

    @Delete
    fun deleteNote(note: Note)

    @Delete
    fun deleteNotes(notes: List<Note>)
}