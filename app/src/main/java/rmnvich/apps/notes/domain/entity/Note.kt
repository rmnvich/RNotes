package rmnvich.apps.notes.domain.entity

import android.arch.persistence.room.Embedded
import android.arch.persistence.room.Entity
import android.arch.persistence.room.ForeignKey
import android.arch.persistence.room.ForeignKey.CASCADE
import android.arch.persistence.room.PrimaryKey
import rmnvich.apps.notes.data.common.Constants.DEFAULT_COLOR

@Entity
class Note {

    @PrimaryKey(autoGenerate = true)
    var noteId: Int = 0

    var text: String = ""

    var timestamp: Long = 0L
    var color: Int = DEFAULT_COLOR

    @Embedded
    var tag: Tag? = null

    var isFavorite: Boolean = false
    var isDeleted: Boolean = false
}