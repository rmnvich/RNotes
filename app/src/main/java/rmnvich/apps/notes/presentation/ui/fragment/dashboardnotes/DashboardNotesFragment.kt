package rmnvich.apps.notes.presentation.ui.fragment.dashboardnotes

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.content.Intent
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.*
import kotlinx.android.synthetic.main.app_bar_main.*
import rmnvich.apps.notes.App
import rmnvich.apps.notes.R
import rmnvich.apps.notes.data.common.Constants.*
import rmnvich.apps.notes.databinding.DashboardNotesFragmentBinding
import rmnvich.apps.notes.di.dashboardnotes.DashboardNotesModule
import rmnvich.apps.notes.domain.entity.Note
import rmnvich.apps.notes.domain.utils.ViewModelFactory
import rmnvich.apps.notes.presentation.ui.activity.addeditnote.AddEditNoteActivity
import rmnvich.apps.notes.presentation.ui.activity.main.MainActivity
import rmnvich.apps.notes.presentation.ui.adapter.dashboard.NotesAdapter
import javax.inject.Inject
import android.support.v7.widget.StaggeredGridLayoutManager


class DashboardNotesFragment : Fragment() {

    @Inject
    lateinit var mViewModelFactory: ViewModelFactory

    @Inject
    lateinit var mAdapter: NotesAdapter

    private lateinit var mDashboardNotesViewModel: DashboardNotesViewModel
    private lateinit var mDashboardNotesBinding: DashboardNotesFragmentBinding

    companion object {
        fun newInstance(isFavoriteNotes: Boolean): DashboardNotesFragment {
            val args = Bundle()
            args.putBoolean(EXTRA_FAVORITE_NOTES, isFavoriteNotes)

            val fragment = DashboardNotesFragment()
            fragment.arguments = args

            return fragment
        }
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        val componentHolder = App.getApp(activity?.applicationContext).componentsHolder
        val isFavoriteNotes = arguments?.getBoolean(EXTRA_FAVORITE_NOTES)

        if (!componentHolder.isComponentReleased(javaClass))
            componentHolder.releaseComponent(javaClass)

        componentHolder.getComponent(javaClass,
                DashboardNotesModule(isFavoriteNotes!!))
                ?.inject(this)
    }

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View {
        mDashboardNotesBinding = DataBindingUtil.inflate(
                inflater,
                R.layout.dashboard_notes_fragment, container, false
        )
        var toolbarTitle = R.string.title_notes
        var viewModelKey = KEY_ALL_NOTES

        if (arguments?.getBoolean(EXTRA_FAVORITE_NOTES)!!) {
            viewModelKey = KEY_IS_FAVORITE_NOTES
            toolbarTitle = R.string.title_favorites
        }

        mDashboardNotesViewModel = ViewModelProviders.of(activity!!, mViewModelFactory)
                .get(viewModelKey, DashboardNotesViewModel::class.java)
        mDashboardNotesBinding.viewmodel = mDashboardNotesViewModel

        mDashboardNotesBinding.swipeRefreshLayout.isEnabled = false
        mDashboardNotesBinding.swipeRefreshLayout.setColorSchemeResources(R.color.colorAccent)

        val gridLayoutManager = StaggeredGridLayoutManager(2,
                StaggeredGridLayoutManager.VERTICAL)
        gridLayoutManager.gapStrategy = StaggeredGridLayoutManager
                .GAP_HANDLING_MOVE_ITEMS_BETWEEN_SPANS
        mDashboardNotesBinding.recyclerNotes.layoutManager = LinearLayoutManager(context,
                LinearLayoutManager.VERTICAL, false)

        mDashboardNotesBinding.recyclerNotes.adapter = mAdapter
        mAdapter.setOnItemClickListener(
                onClickNote = { mDashboardNotesViewModel.editNote(it) },
                onClickFavoriteButton = { noteId, isFavorite ->
                    mDashboardNotesViewModel.updateIsFavoriteNote(noteId, isFavorite)
                })

        (activity as MainActivity).toolbar.setTitle(toolbarTitle)
        setHasOptionsMenu(true)

        return mDashboardNotesBinding.root
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater?.inflate(R.menu.dashboard_menu, menu)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        mDashboardNotesViewModel.getNotes()?.observe(this,
                Observer<List<Note>> { handleResponse(it!!) })
        mDashboardNotesViewModel.getAddNoteEvent().observe(this,
                Observer { handleAddEditNoteEvent(-1) })
        mDashboardNotesViewModel.getEditNoteEvent().observe(this,
                Observer { handleAddEditNoteEvent(it!!) })
        observeSnackbar()
    }

    private fun handleResponse(response: List<Note>) {
        mAdapter.setData(response)

        if (mDashboardNotesViewModel.bRecyclerIsScroll)
            mDashboardNotesBinding.recyclerNotes.scrollToPosition(0)
    }

    private fun handleAddEditNoteEvent(noteId: Int) {
        val intent = Intent(activity, AddEditNoteActivity::class.java)
        if (noteId != -1)
            intent.putExtra(EXTRA_NOTE_ID, noteId)

        activity?.startActivity(intent)
        activity?.overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
    }

    //TODO: Make snackbar with action
    private fun observeSnackbar() {
        mDashboardNotesViewModel.getSnackbar().observe(this, Observer {
            Snackbar.make(
                    mDashboardNotesBinding.root, getString(it!!),
                    Snackbar.LENGTH_LONG
            ).show()
        })
    }

    override fun onDetach() {
        super.onDetach()
        App.getApp(activity?.applicationContext).componentsHolder
                .releaseComponent(javaClass)
    }
}