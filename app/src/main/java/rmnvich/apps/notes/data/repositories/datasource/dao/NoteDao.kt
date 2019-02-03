package rmnvich.apps.notes.data.repositories.datasource.dao

import android.arch.persistence.room.*
import io.reactivex.Flowable
import io.reactivex.Single
import rmnvich.apps.notes.domain.entity.Note
import rmnvich.apps.notes.domain.entity.NoteWithTag

@Dao
interface NoteDao {

    @Transaction
    @Query("SELECT note.id AS note_id, note.text AS note_text, note.imagePath AS note_image_path, note.timestamp AS note_timestamp, note.color AS note_color, note.isFavorite AS note_is_favorite, note.isDeleted AS note_is_deleted, tag.name AS note_tag_name, tag.id AS note_tag_id FROM note LEFT JOIN tag ON note.tag_id = tag.id WHERE note.isDeleted = 0 AND CASE WHEN :isFavorite == 1 THEN note.isFavorite = 1 ELSE note.isFavorite = 0 OR note.isFavorite = 1 END ORDER BY timestamp DESC")
    fun getAllNotes(isFavorite: Boolean): Flowable<List<NoteWithTag>>

    @Transaction
    @Query("SELECT note.id AS note_id, note.text AS note_text, note.imagePath AS note_image_path, note.timestamp AS note_timestamp, note.color AS note_color, note.isFavorite AS note_is_favorite, note.isDeleted AS note_is_deleted, tag.name AS note_tag_name, tag.id AS note_tag_id FROM note LEFT JOIN tag ON note.tag_id = tag.id WHERE note.isDeleted = 0 AND CASE WHEN :isFavorite == 1 THEN note.isFavorite = 1 ELSE note.isFavorite = 0 OR note.isFavorite = 1 END AND CASE WHEN :isUnionConditions == 1 THEN (note.color IN (:colors) AND tag_id IN (:tags)) ELSE (note.color IN (:colors) OR tag_id IN (:tags)) END AND CASE WHEN :isOnlyWithPicture == 1 THEN note.imagePath != '' ELSE note.imagePath = '' OR note.imagePath != '' END ORDER BY timestamp DESC")
    fun getFilteredNotes(
            colors: List<Int>,
            tags: List<Int>,
            isFavorite: Boolean,
            isUnionConditions: Boolean,
            isOnlyWithPicture: Boolean
    ): Flowable<List<NoteWithTag>>

    @Transaction
    @Query("SELECT note.id AS note_id, note.text AS note_text, note.imagePath AS note_image_path, note.timestamp AS note_timestamp, note.color AS note_color, note.isFavorite AS note_is_favorite, note.isDeleted AS note_is_deleted, tag.name AS note_tag_name, tag.id AS note_tag_id FROM note LEFT JOIN tag ON note.tag_id = tag.id WHERE note.isDeleted = 0 AND CASE WHEN :isFavorite == 1 THEN note.isFavorite = 1 ELSE note.isFavorite = 0 OR note.isFavorite = 1 END AND note.text LIKE '%' || :query || '%' ORDER BY timestamp DESC")
    fun getSearchedNotes(query: String, isFavorite: Boolean): Flowable<List<NoteWithTag>>

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

    @Insert
    fun insertNote(note: Note)

    @Update
    fun updateNotes(notes: List<Note>)

    @Update
    fun updateNote(note: Note)

    @Delete
    fun deleteNote(note: Note)

    @Delete
    fun deleteNotes(notes: List<Note>)
}