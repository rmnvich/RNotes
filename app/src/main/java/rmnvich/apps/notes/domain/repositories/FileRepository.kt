package rmnvich.apps.notes.domain.repositories

import android.graphics.Bitmap
import android.net.Uri
import java.io.File

interface FileRepository {

    fun getPathFromUri(contentUri: Uri): String

    fun saveTempImage(bitmap: Bitmap): File
}