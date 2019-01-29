package rmnvich.apps.notes.domain.entity

import android.arch.persistence.room.Embedded
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

@Entity
open class Note {

    @PrimaryKey(autoGenerate = true)
    var noteId: Int = 0

    var text: String = ""

    var imagePath: String = ""
    var timestamp: Long = 0L
    var color: Int = 0

    @Embedded
    var tag: Tag? = null

    var isFavorite: Boolean = false
    var isDeleted: Boolean = false

    var isSelected: Boolean = false
}