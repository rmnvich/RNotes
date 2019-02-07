package rmnvich.apps.notes.domain.entity

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

@Entity
class Reminder {

    @PrimaryKey(autoGenerate = true)
    var id: Int = 0

    var text: String = ""

    var timeRemind: Long = 0L
    var repeatType: Int = 0

    var isCompleted: Boolean = false
}