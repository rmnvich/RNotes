package rmnvich.apps.notes.presentation.ui.fragment.trash

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.app.Fragment
import android.support.v7.widget.StaggeredGridLayoutManager
import android.view.*
import kotlinx.android.synthetic.main.main_activity.*
import org.jetbrains.anko.noButton
import org.jetbrains.anko.support.v4.alert
import org.jetbrains.anko.support.v4.selector
import org.jetbrains.anko.yesButton
import rmnvich.apps.notes.App
import rmnvich.apps.notes.R
import rmnvich.apps.notes.databinding.TrashFragmentBinding
import rmnvich.apps.notes.domain.entity.Note
import rmnvich.apps.notes.domain.utils.ViewModelFactory
import rmnvich.apps.notes.presentation.ui.activity.main.MainActivity
import rmnvich.apps.notes.presentation.ui.adapter.trash.TrashAdapter
import javax.inject.Inject

class TrashFragment : Fragment() {

    @Inject
    lateinit var mViewModelFactory: ViewModelFactory

    @Inject
    lateinit var mAdapter: TrashAdapter

    private lateinit var mTrashViewModel: TrashViewModel
    private lateinit var mTrashBinding: TrashFragmentBinding

    companion object {
        fun newInstance() = TrashFragment()
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        App.getApp(activity?.applicationContext).componentsHolder
                .getComponent(javaClass).inject(this)
    }

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        mTrashBinding = DataBindingUtil.inflate(
                inflater,
                R.layout.trash_fragment, container, false
        )
        mTrashBinding.swipeRefreshLayout
                .setColorSchemeResources(R.color.colorAccent)
        mTrashBinding.swipeRefreshLayout.isEnabled = false
        (activity as MainActivity).setSupportActionBar(mTrashBinding.trashToolbar)

        initRecyclerView()
        initDefaultToolbar()

        setHasOptionsMenu(true)
        return mTrashBinding.root
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater?.inflate(R.menu.trash_menu, menu)
    }

    override fun onPrepareOptionsMenu(menu: Menu?) {
        super.onPrepareOptionsMenu(menu)
        menu?.findItem(R.id.menu_empty_trash)?.isVisible = !mTrashViewModel.bIsNotesSelected
        menu?.findItem(R.id.menu_restore)?.isVisible = mTrashViewModel.bIsNotesSelected
        menu?.findItem(R.id.menu_delete_forever)?.isVisible = mTrashViewModel.bIsNotesSelected
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        return when (item?.itemId) {
            R.id.menu_empty_trash -> {
                if (mAdapter.itemCount > 0)
                    handleDeleteNotes(true)
                true
            }
            R.id.menu_delete_forever -> {
                handleDeleteNotes(false)
                true
            }
            R.id.menu_restore -> {
                mTrashViewModel.restoreNotes(mAdapter.mSelectedNotes)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun initDefaultToolbar() {
        mTrashBinding.trashToolbar.title = getString(R.string.title_trash)
        mTrashBinding.trashToolbar.setNavigationIcon(R.drawable.ic_action_menu_inverted)
        mTrashBinding.trashToolbar.setNavigationOnClickListener {
            (activity as MainActivity).drawer_layout.openDrawer(Gravity.START)
        }
    }

    private fun initAlternativeToolbar(selectedNotesSize: Int) {
        mTrashBinding.trashToolbar.title = "$selectedNotesSize ${getString(R.string.notes_selected)}"
        mTrashBinding.trashToolbar.setNavigationIcon(R.drawable.ic_action_close_inverted)
        mTrashBinding.trashToolbar.setNavigationOnClickListener { mAdapter.unselectAllNotes() }
    }

    private fun initRecyclerView() {
        val gridLayoutManager = StaggeredGridLayoutManager(
                2,
                StaggeredGridLayoutManager.VERTICAL
        )
        gridLayoutManager.gapStrategy = StaggeredGridLayoutManager
                .GAP_HANDLING_MOVE_ITEMS_BETWEEN_SPANS
        mTrashBinding.recyclerTrashNotes.layoutManager = gridLayoutManager

        mAdapter.setOnItemClickListener { handleOnClickNote(it) }
        mAdapter.setOnItemSelectListener { handleSelectNote(it) }
        mTrashBinding.recyclerTrashNotes.adapter = mAdapter
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        mTrashViewModel = ViewModelProviders.of(activity!!, mViewModelFactory)
                .get(TrashViewModel::class.java)
        mTrashBinding.viewmodel = mTrashViewModel

        mTrashViewModel.getNotes()?.observe(this,
                Observer<List<Note>> { handleResponse(it!!) })
        mTrashViewModel.getDeleteOrRestoreNotesEvent().observe(this,
                Observer { mAdapter.clearSelectedNotes() })
        observeSnackbar()
    }

    private fun handleResponse(response: List<Note>) {
        mAdapter.setData(response)
    }

    private fun handleDeleteNotes(allNotes: Boolean) {
        alert(getString(R.string.restore_notes_impossible), getString(R.string.are_you_sure)) {
            yesButton {
                if (allNotes)
                    mTrashViewModel.deleteNotes(mAdapter.mNoteList)
                else mTrashViewModel.deleteNotes(mAdapter.mSelectedNotes)
            }
            noButton {}
        }.show()
    }

    private fun handleSelectNote(selectedNotesSize: Int) {
        mTrashViewModel.bIsNotesSelected = selectedNotesSize > 0
        if (mTrashViewModel.bIsNotesSelected) {
            initAlternativeToolbar(selectedNotesSize)
        } else initDefaultToolbar()
        (activity)?.invalidateOptionsMenu()
    }

    private fun handleOnClickNote(note: Note) {
        selector(
                getString(R.string.what_to_do),
                listOf(getString(R.string.restore), getString(R.string.delete))
        ) { _, position ->
            if (position == 0) {
                mTrashViewModel.restoreNote(note.id)
            } else mTrashViewModel.deleteNote(note)
        }
    }

    private fun observeSnackbar() {
        mTrashViewModel.getSnackbar().observe(this, Observer {
            Snackbar.make(
                    mTrashBinding.root, getString(it!!),
                    Snackbar.LENGTH_LONG
            ).show()
        })
    }

    override fun onPause() {
        super.onPause()
        mAdapter.unselectAllNotes()
    }

    override fun onDetach() {
        super.onDetach()
        App.getApp(activity?.applicationContext).componentsHolder
                .releaseComponent(javaClass)
    }
}