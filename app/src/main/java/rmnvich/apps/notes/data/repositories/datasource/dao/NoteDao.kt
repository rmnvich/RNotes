package rmnvich.apps.notes.data.repositories.datasource.dao

import android.arch.persistence.room.*
import io.reactivex.Flowable
import io.reactivex.Single
import rmnvich.apps.notes.domain.entity.Note
import rmnvich.apps.notes.domain.entity.NoteWithTag

@Dao
interface NoteDao {

    @Transaction
    @Query("SELECT note.id AS note_id, note.text AS note_text, note.imagePath AS note_image_path, note.timestamp AS note_timestamp, note.color AS note_color, note.isFavorite AS note_is_favorite, note.isDeleted AS note_is_deleted, tag.name AS note_tag_name, tag.id AS note_tag_id FROM note LEFT JOIN tag ON note.tag_id = tag.id WHERE note.isDeleted = :isDeleted ORDER BY timestamp DESC")
    fun getAllNotes(isDeleted: Boolean): Flowable<List<NoteWithTag>>

    @Transaction
    @Query("SELECT note.id AS note_id, note.text AS note_text, note.imagePath AS note_image_path, note.timestamp AS note_timestamp, note.color AS note_color, note.isFavorite AS note_is_favorite, note.isDeleted AS note_is_deleted, tag.name AS note_tag_name, tag.id AS note_tag_id FROM note LEFT JOIN tag ON note.tag_id = tag.id WHERE note.isFavorite = :isFavorite AND note.isDeleted = :isDeleted ORDER BY timestamp DESC")
    fun getAllFavoritesNotes(isFavorite: Boolean, isDeleted: Boolean): Flowable<List<NoteWithTag>>

    @Transaction
    @Query("SELECT note.id AS note_id, note.text AS note_text, note.imagePath AS note_image_path, note.timestamp AS note_timestamp, note.color AS note_color, note.isFavorite AS note_is_favorite, note.isDeleted AS note_is_deleted, tag.name AS note_tag_name, tag.id AS note_tag_id FROM note LEFT JOIN tag ON tag_id = tag.id WHERE note.id = :noteId")
    fun getNoteWithTagByNoteId(noteId: Int): Single<NoteWithTag>

    @Query("SELECT * FROM note WHERE id = :noteId")
    fun getNoteById(noteId: Int): Single<Note>

    @Query("UPDATE note SET isFavorite = :isFavorite WHERE id = :noteId")
    fun favoriteOrUnfavoriteNote(noteId: Int, isFavorite: Boolean)

    @Query("SELECT * FROM note WHERE isDeleted LIKE :isDeleted ORDER BY timestamp DESC")
    fun getDeletedNotes(isDeleted: Boolean): Flowable<List<Note>>

    @Query("UPDATE note SET isDeleted = :isDeleted WHERE id = :noteId")
    fun deleteOrRestoreNote(noteId: Int, isDeleted: Boolean)

    @Update
    fun updateNotes(notes: List<Note>)

    @Update
    fun updateNote(note: Note)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertNote(note: Note)

    @Delete
    fun deleteNote(note: Note)

    @Delete
    fun deleteNotes(notes: List<Note>)
}