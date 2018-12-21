package rmnvich.apps.notes.dao

import android.arch.persistence.room.Room
import android.support.test.InstrumentationRegistry
import android.support.test.runner.AndroidJUnit4
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import rmnvich.apps.notes.data.repositories.datasource.Database
import rmnvich.apps.notes.data.repositories.datasource.dao.NoteDao
import rmnvich.apps.notes.domain.entity.Note


@RunWith(AndroidJUnit4::class)
class NoteDaoTest {

    private var db: Database? = null
    private var noteDao: NoteDao? = null

    @Before
    @Throws(Exception::class)
    fun createDb() {
        db = Room.inMemoryDatabaseBuilder(
            InstrumentationRegistry.getContext(),
            Database::class.java
        ).allowMainThreadQueries().build()
        noteDao = db!!.noteDao()
    }

    private fun createFakeNotes(size: Int): List<Note> {
        val notes: MutableList<Note> = ArrayList()

        for (i in 0 until size) {
            val note = Note()
            note.text = "#$i"
            note.timestamp = i.toLong()
            note.noteId = i

            notes.add(note)
        }

        return notes
    }

    private fun notesAreIdentical(first: Note, second: Note): Boolean {
        return first.color == second.color &&
                first.noteId == second.noteId &&
                first.isDeleted == second.isDeleted &&
                first.isFavorite == second.isFavorite &&
                first.text == second.text &&
                first.timestamp == second.timestamp
    }

    @Test
    @Throws(Exception::class)
    fun whenInsertEmployeeThenReadTheSameOne() {
        val note = Note()
        note.noteId = 1
        note.text = "123"
        noteDao?.insertNote(note)

        noteDao?.getAllNotes()
            ?.test()
            ?.assertValue(listOf(note))
    }

    @Test
    @Throws(Exception::class)
    fun whenUpdateEmployeeThenReadTheSameOne() {
    }

    @After
    @Throws(Exception::class)
    fun closeDb() {
        db!!.close()
    }
}