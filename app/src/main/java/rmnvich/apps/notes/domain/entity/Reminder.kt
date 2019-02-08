package rmnvich.apps.notes.domain.entity

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import rmnvich.apps.notes.data.common.Constants.REPEAT_TYPE_ONCE

@Entity
class Reminder {

    @PrimaryKey(autoGenerate = true)
    var id: Int = 0

    var text: String = ""

    var timeRemind: Long = 0L
    var repeatType: Int = REPEAT_TYPE_ONCE

    var colorNumber: Int = 0

    var isCompleted: Boolean = false
}