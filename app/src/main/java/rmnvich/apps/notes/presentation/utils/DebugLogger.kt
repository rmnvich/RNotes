package rmnvich.apps.notes.presentation.utils

import android.util.Log.e
import rmnvich.apps.notes.data.common.Constants.LOG_TAG

class DebugLogger {

    companion object {
        fun log(text: String) {
            e(LOG_TAG, text)
        }
    }
}