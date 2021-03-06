package rmnvich.apps.notes.domain.entity

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.ForeignKey
import android.arch.persistence.room.PrimaryKey

@Entity(
    foreignKeys = [ForeignKey(
        entity = Tag::class,
        parentColumns = ["id"],
        childColumns = ["tag_id"],
        onDelete = ForeignKey.SET_NULL
    )]
)
class Note {

    @PrimaryKey(autoGenerate = true)
    var id: Int = 0

    @ColumnInfo(name = "tag_id")
    var tagId: Int? = null

    var title: String = ""
    var text: String = ""
    var imagePath: String = ""

    var color: Int = 0
    var timestamp: Long = 0L

    var isLocked: Boolean = false
    var isDeleted: Boolean = false
    var isFavorite: Boolean = false
}