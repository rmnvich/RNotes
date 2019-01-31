package rmnvich.apps.notes.domain.entity

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

@Entity
class Note {

    @PrimaryKey(autoGenerate = true)
    var id: Int = 0

    @ColumnInfo(name = "tag_id")
    var tagId: Int = 0

    var text: String = ""
    var imagePath: String = ""

    var color: Int = 0
    var timestamp: Long = 0L

    var isFavorite: Boolean = false
    var isDeleted: Boolean = false
}