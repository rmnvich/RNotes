package rmnvich.apps.notes.presentation.ui.fragment.notes

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.content.Intent
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.os.Handler
import android.support.design.widget.Snackbar
import android.support.v4.app.Fragment
import android.support.v7.widget.StaggeredGridLayoutManager
import android.view.*
import com.daimajia.swipe.util.Attributes
import kotlinx.android.synthetic.main.main_activity.*
import rmnvich.apps.notes.App
import rmnvich.apps.notes.R
import rmnvich.apps.notes.data.common.Constants.*
import rmnvich.apps.notes.databinding.DashboardNotesFragmentBinding
import rmnvich.apps.notes.di.notes.DashboardNotesModule
import rmnvich.apps.notes.domain.entity.NoteWithTag
import rmnvich.apps.notes.domain.utils.ViewModelFactory
import rmnvich.apps.notes.presentation.ui.activity.addeditnote.AddEditNoteActivity
import rmnvich.apps.notes.presentation.ui.activity.main.MainActivity
import rmnvich.apps.notes.presentation.ui.adapter.note.NotesAdapter
import javax.inject.Inject


class DashboardNotesFragment : Fragment() {

    @Inject
    lateinit var mViewModelFactory: ViewModelFactory

    @Inject
    lateinit var mNotesAdapter: NotesAdapter

    private lateinit var mDashboardNotesViewModel: DashboardNotesViewModel
    private lateinit var mDashboardNotesBinding: DashboardNotesFragmentBinding

    private var isFavoriteNotes = false

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
        isFavoriteNotes = arguments?.getBoolean(EXTRA_FAVORITE_NOTES)!!

        if (!componentHolder.isComponentReleased(javaClass))
            componentHolder.releaseComponent(javaClass)

        componentHolder.getComponent(
                javaClass,
                DashboardNotesModule(isFavoriteNotes)
        )?.inject(this)
    }

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View {
        mDashboardNotesBinding = DataBindingUtil.inflate(
                inflater,
                R.layout.dashboard_notes_fragment, container, false
        )
        mDashboardNotesBinding.swipeRefreshLayout
                .setColorSchemeResources(R.color.colorAccent)
        mDashboardNotesBinding.swipeRefreshLayout.isEnabled = false

        if (isFavoriteNotes) {
            mDashboardNotesBinding.ivEmpty.setImageResource(R.drawable.empty_favotites)
            mDashboardNotesBinding.tvEmpty.setText(R.string.you_have_no_favorite_notes)
        } else {
            mDashboardNotesBinding.ivEmpty.setImageResource(R.drawable.empty_notes)
            mDashboardNotesBinding.tvEmpty.setText(R.string.you_have_no_notes)
        }

        initToolbar()
        initRecyclerView()

        return mDashboardNotesBinding.root
    }

    private fun initToolbar() {
        val toolbarTitle = if (isFavoriteNotes) {
            R.string.title_favorites
        } else R.string.title_notes

        (activity as MainActivity).setSupportActionBar(mDashboardNotesBinding.notesToolbar)
        mDashboardNotesBinding.notesToolbar.setTitle(toolbarTitle)
        mDashboardNotesBinding.notesToolbar.setNavigationOnClickListener {
            (activity as MainActivity).drawer_layout.openDrawer(Gravity.START)
        }
        setHasOptionsMenu(true)
    }

    private fun initRecyclerView() {
        val spanCount = if (isFavoriteNotes) 1 else 2

        val gridLayoutManager = StaggeredGridLayoutManager(
                spanCount,
                StaggeredGridLayoutManager.VERTICAL
        )
        gridLayoutManager.gapStrategy = StaggeredGridLayoutManager
                .GAP_HANDLING_MOVE_ITEMS_BETWEEN_SPANS
        mDashboardNotesBinding.recyclerNotes.layoutManager = gridLayoutManager

        mNotesAdapter.mode = Attributes.Mode.Multiple
        mNotesAdapter.setOnItemClickListener(
                onClickNote = { mDashboardNotesViewModel.editNote(it) },
                onClickDelete = { noteId, position ->
                    mDashboardNotesViewModel.deleteNote(noteId, position)
                },
                onClickFavorite = { noteId, isFavorite ->
                    mDashboardNotesViewModel.updateIsFavoriteNote(noteId, isFavorite)
                })
        mDashboardNotesBinding.recyclerNotes.adapter = mNotesAdapter
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater?.inflate(R.menu.notes_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        return when (item?.itemId) {
            R.id.menu_filter -> {
                showFragment(FilterFragment.newInstance(isFavoriteNotes))
                true
            }
            R.id.menu_search -> {
                showFragment(SearchFragment.newInstance(isFavoriteNotes))
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun showFragment(fragment: Fragment) {
        Handler().postDelayed({
            activity?.supportFragmentManager?.beginTransaction()
                    ?.setCustomAnimations(
                            R.anim.slide_in_up, R.anim.slide_out_up,
                            R.anim.slide_in_down, R.anim.slide_out_down
                    )
                    ?.addToBackStack("")
                    ?.replace(R.id.content, fragment)
                    ?.commit()
        }, 100)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val viewModelKey = if (isFavoriteNotes) {
            KEY_IS_FAVORITE_NOTES
        } else KEY_ALL_NOTES

        mDashboardNotesViewModel = ViewModelProviders.of(activity!!, mViewModelFactory)
                .get(viewModelKey, DashboardNotesViewModel::class.java)
        mDashboardNotesBinding.viewmodel = mDashboardNotesViewModel

        mDashboardNotesViewModel.getNotes()?.observe(this,
                Observer { handleNotesResponse(it!!) })
        mDashboardNotesViewModel.getSharedFilter().observe(this,
                Observer { filter ->
                    mDashboardNotesViewModel.applyFilterToNotes(filter?.colors!!,
                            filter.tags, filter.isUnionConditions, filter.isOnlyWithPicture)
                })
        mDashboardNotesViewModel.getAddNoteEvent().observe(this,
                Observer { handleAddEditNoteEvent(-1) })
        mDashboardNotesViewModel.getEditNoteEvent().observe(this,
                Observer { handleAddEditNoteEvent(it!!) })
        mDashboardNotesViewModel.getDeleteNoteEvent().observe(this,
                Observer { handleDeleteNoteEvent(it!!) })
        observeSnackbar()
    }

    private fun handleNotesResponse(response: List<NoteWithTag>) {
        mNotesAdapter.setData(response)

        if (mDashboardNotesViewModel.bIsRecyclerNeedToScroll) {
            mDashboardNotesBinding.recyclerNotes.scrollToPosition(0)
            mDashboardNotesViewModel.bIsRecyclerNeedToScroll = false
        }
    }

    private fun handleAddEditNoteEvent(noteId: Int) {
        val intent = Intent(activity, AddEditNoteActivity::class.java)
        intent.putExtra(EXTRA_FAVORITE_NOTES, isFavoriteNotes)
        if (noteId != -1)
            intent.putExtra(EXTRA_NOTE_ID, noteId)

        activity?.startActivity(intent)
        activity?.overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
    }

    private fun handleDeleteNoteEvent(noteId: Int) {
        Snackbar.make(
                mDashboardNotesBinding.root, getString(R.string.note_has_been_deleted),
                Snackbar.LENGTH_LONG
        ).setAction(getString(R.string.undo)) {
            mDashboardNotesViewModel.restoreNote(noteId)
        }.show()
    }

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