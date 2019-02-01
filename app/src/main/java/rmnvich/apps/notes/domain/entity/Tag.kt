package rmnvich.apps.notes.domain.entity

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

@Entity
class Tag {

    @PrimaryKey(autoGenerate = true)
    var id: Int? = null

    var name: String? = null
}