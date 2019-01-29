package rmnvich.apps.notes.data.repositories

import android.content.Context
import android.content.CursorLoader
import android.net.Uri
import android.provider.MediaStore
import rmnvich.apps.notes.domain.repositories.FileRepository

class FileRepositoryImpl(private val context: Context): FileRepository {

    override fun getPathFromUri(contentUri: Uri): String {
        val projection = arrayOf(MediaStore.Images.Media.DATA)
        val loader = CursorLoader(context, contentUri, projection, null, null, null)
        val cursor = loader.loadInBackground()
        val columnIndex: Int
        try {
            columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
        } catch (e: NullPointerException) {
            return contentUri.path
        }

        cursor.moveToFirst()
        val result = cursor.getString(columnIndex)
        cursor.close()
        return result
    }
}