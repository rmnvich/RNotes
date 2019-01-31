package rmnvich.apps.notes.domain.interactors.addeditnote

import android.graphics.Bitmap
import android.net.Uri
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import rmnvich.apps.notes.data.common.SchedulersProvider
import rmnvich.apps.notes.domain.entity.Note
import rmnvich.apps.notes.domain.entity.NoteWithTag
import rmnvich.apps.notes.domain.repositories.FileRepository
import rmnvich.apps.notes.domain.repositories.NotesRepository
import java.io.File
import java.util.concurrent.Callable

class AddEditNoteInteractor(
    private val notesRepository: NotesRepository,
    private val fileRepository: FileRepository,
    private val schedulersProvider: SchedulersProvider
) {

    fun getNoteById(noteId: Int): Single<NoteWithTag> {
        return notesRepository.getNoteById(noteId)
            .subscribeOn(schedulersProvider.io())
            .observeOn(schedulersProvider.ui())
    }

    fun insertNote(note: Note): Completable {
        return notesRepository.insertNote(note)
            .subscribeOn(schedulersProvider.io())
            .observeOn(schedulersProvider.ui())
    }

    fun updateNote(note: Note, noteId: Int): Completable {
        return notesRepository.updateNote(note, noteId)
            .subscribeOn(schedulersProvider.io())
            .observeOn(schedulersProvider.ui())
    }

    fun getFilePathFromUri(uri: Uri): Observable<String> {
        return Observable.fromCallable(CallableUriAction(uri))
    }

    fun getImageFileFromBitmap(bitmap: Bitmap): Observable<File> {
        return Observable.fromCallable(CallableBitmapAction(bitmap))
            .subscribeOn(Schedulers.computation())
            .observeOn(AndroidSchedulers.mainThread())
    }

    inner class CallableUriAction(private var uri: Uri) : Callable<String> {
        override fun call(): String {
            return fileRepository.getPathFromUri(uri)
        }
    }

    inner class CallableBitmapAction(private var bitmap: Bitmap) : Callable<File> {
        override fun call(): File {
            return fileRepository.saveTempImage(bitmap)
        }
    }
}