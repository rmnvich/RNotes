package rmnvich.apps.notes.presentation.utils

import android.annotation.SuppressLint
import java.text.SimpleDateFormat
import java.util.*

object DateHelper {

    fun getCurrentTimeInMills(): Long {
        return Calendar.getInstance().timeInMillis
    }

    @SuppressLint("SimpleDateFormat")
    fun convertLongTimeToString(time: Long): String {
        val dateFormat = SimpleDateFormat("EEEE, dd MMMM yyyy, HH:mm")
        return dateFormat.format(time)
    }
}