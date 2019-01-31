package rmnvich.apps.notes.domain.entity

import android.arch.persistence.room.ColumnInfo

class NoteWithTag {

    @ColumnInfo(name = "note_id")
    var noteId: Int = 0

    @ColumnInfo(name = "note_text")
    var noteText: String = ""

    @ColumnInfo(name = "note_image_path")
    var noteImagePath: String = ""

    @ColumnInfo(name = "note_timestamp")
    var noteTimestamp: Long = 0L

    @ColumnInfo(name = "note_color")
    var noteColor: Int = 0

    @ColumnInfo(name = "note_is_favorite")
    var noteIsFavorite: Boolean = false

    @ColumnInfo(name = "note_is_deleted")
    var noteIsDeleted: Boolean = false

    @ColumnInfo(name = "note_tag_name")
    var tagName: String = ""
}