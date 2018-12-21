package rmnvich.apps.notes.presentation.utils

import android.util.Log.d
import rmnvich.apps.notes.data.common.Constants.LOG_TAG

class DebugLogger {

    companion object {
        fun log(text: String) {
            d(LOG_TAG, text)
        }
    }
}