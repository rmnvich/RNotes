package rmnvich.apps.notes.presentation.ui.fragment.notes

import android.app.Application
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import rmnvich.apps.notes.data.repositories.NotesRepositoryImpl
import rmnvich.apps.notes.data.repositories.datasource.Database
import rmnvich.apps.notes.domain.entity.Note
import rmnvich.apps.notes.domain.interactors.dashboardnotes.DashboardNotesInteractor
import rmnvich.apps.notes.domain.repositories.NotesRepository


@RunWith(JUnit4::class)
class DashboardNotesViewModelTest {

    private lateinit var mViewModel: DashboardNotesViewModel

    @Mock
    private lateinit var mApplication: Application

    @Mock
    private lateinit var mInteractor: DashboardNotesInteractor

    @Mock
    private lateinit var mDatabase: Database

    @Mock
    private lateinit var mNotesRepository: NotesRepository

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)

        mNotesRepository = NotesRepositoryImpl(mDatabase)
//        mInteractor = DashboardNotesInteractor(
//            mNotesRepository,
//            TestSchedulersProvider()
//        )
//        mViewModel = DashboardNotesViewModel(mApplication, mInteractor)
    }

    private fun getFakeNote(): Note {
        val note = Note()
        note.text = "Some text"
        note.color = 1
        note.tagId = 2
        note.timestamp = 3L

        return note
    }

    private fun getFakeNotes(size: Int): List<Note> {
        val notes: MutableList<Note> = mutableListOf()

        for (i in 0 until size) {
            val note = Note()
            note.text = "Some text #$i"
            note.color = i
            note.tagId = i
            note.timestamp = i.toLong()

            notes.add(note)
        }
        return notes
    }

    @Test
    fun loadAllNotes() {

    }
}