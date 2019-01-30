package rmnvich.apps.notes.data.repositories

import android.content.Context
import android.content.CursorLoader
import android.graphics.Bitmap
import android.net.Uri
import android.os.Environment
import android.provider.MediaStore
import rmnvich.apps.notes.domain.repositories.FileRepository
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.util.*

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

    override fun saveTempImage(bitmap: Bitmap): File {
        val calendar = Calendar.getInstance()

        val bytes = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
        val file = File(
            "${Environment.getExternalStorageDirectory()}${File.separator}" +
                    "temp_file${calendar.timeInMillis}.jpg"
        )
        try {
            file.createNewFile()
            val fo = FileOutputStream(file)
            fo.write(bytes.toByteArray())
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return file
    }
}