package rmnvich.apps.notes.domain.repositories

import android.net.Uri

interface FileRepository {

    fun getPathFromUri(contentUri: Uri): String
}