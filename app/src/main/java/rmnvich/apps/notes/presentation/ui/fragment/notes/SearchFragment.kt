package rmnvich.apps.notes.presentation.ui.fragment.notes

import android.app.Service
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
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import com.daimajia.swipe.util.Attributes
import com.jakewharton.rxbinding2.widget.RxTextView
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import rmnvich.apps.notes.App
import rmnvich.apps.notes.R
import rmnvich.apps.notes.data.common.Constants
import rmnvich.apps.notes.databinding.SearchFragmentBinding
import rmnvich.apps.notes.domain.entity.NoteWithTag
import rmnvich.apps.notes.presentation.ui.activity.addeditnote.AddEditNoteActivity
import rmnvich.apps.notes.presentation.ui.activity.main.MainActivity
import rmnvich.apps.notes.presentation.ui.adapter.note.NotesAdapter
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class SearchFragment : Fragment() {

    @Inject
    lateinit var mNotesAdapter: NotesAdapter

    private lateinit var mSearchDisposable: Disposable

    private lateinit var mSharedViewModel: DashboardNotesViewModel
    private lateinit var mSearchNotesBinding: SearchFragmentBinding

    private var isFavoriteNotes = false

    companion object {
        fun newInstance(isFavoriteNotes: Boolean): SearchFragment {
            val args = Bundle()
            args.putBoolean(Constants.EXTRA_FAVORITE_NOTES, isFavoriteNotes)

            val fragment = SearchFragment()
            fragment.arguments = args

            return fragment
        }
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        isFavoriteNotes = arguments?.getBoolean(Constants.EXTRA_FAVORITE_NOTES)!!
        App.getApp(activity?.applicationContext).componentsHolder
                .getComponent(javaClass).inject(this)
    }

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View {
        mSearchNotesBinding = DataBindingUtil.inflate(
                inflater,
                R.layout.search_fragment, container, false
        )
        mSearchNotesBinding.swipeRefreshLayout
                .setColorSchemeResources(R.color.colorAccent)
        mSearchNotesBinding.swipeRefreshLayout.isEnabled = false

        mSearchNotesBinding.btnArrowBack.setOnClickListener { popBackStack() }

        mSearchDisposable = RxTextView.textChanges(mSearchNotesBinding.etSearch)
                .skipInitialValue()
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.computation())
                .debounce(400, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { mSharedViewModel.searchNotes(it.toString()) }

        initRecyclerView()
        showKeyboard()

        return mSearchNotesBinding.root
    }

    private fun initRecyclerView() {
        val spanCount = if (isFavoriteNotes) 1 else 2

        val gridLayoutManager = StaggeredGridLayoutManager(
                spanCount,
                StaggeredGridLayoutManager.VERTICAL
        )
        gridLayoutManager.gapStrategy = StaggeredGridLayoutManager
                .GAP_HANDLING_MOVE_ITEMS_BETWEEN_SPANS
        mSearchNotesBinding.recyclerSearchedNotes.layoutManager = gridLayoutManager

        mNotesAdapter.mode = Attributes.Mode.Multiple
        mNotesAdapter.setOnItemClickListener(
                onClickNote = { mSharedViewModel.editNote(it) },
                onClickDelete = { noteId, position ->
                    mSharedViewModel.deleteNote(noteId, position)
                },
                onClickFavorite = { noteId, isFavorite ->
                    mSharedViewModel.updateIsFavoriteNote(noteId, isFavorite)
                })
        mSearchNotesBinding.recyclerSearchedNotes.adapter = mNotesAdapter
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val viewModelKey = if (isFavoriteNotes) {
            Constants.KEY_IS_FAVORITE_NOTES
        } else Constants.KEY_ALL_NOTES

        mSharedViewModel = ViewModelProviders.of(activity!!)
                .get(viewModelKey, DashboardNotesViewModel::class.java)
        mSearchNotesBinding.viewmodel = mSharedViewModel

        mSharedViewModel.searchNotes("")

        mSharedViewModel.getSearchedNotes()?.observe(this,
                Observer { handleNotesResponse(it!!) })
        mSharedViewModel.getAddNoteEvent().observe(this,
                Observer { handleAddEditNoteEvent(-1) })
        mSharedViewModel.getEditNoteEvent().observe(this,
                Observer { handleAddEditNoteEvent(it!!) })
        mSharedViewModel.getDeleteNoteEvent().observe(this,
                Observer { handleDeleteNoteEvent(it!!) })
        observeSnackbar()
    }

    private fun handleNotesResponse(response: List<NoteWithTag>) {
        mNotesAdapter.setData(response)

        if (mSharedViewModel.bIsRecyclerNeedToScroll) {
            mSearchNotesBinding.recyclerSearchedNotes.scrollToPosition(0)
            mSharedViewModel.bIsRecyclerNeedToScroll = false
        }
    }

    private fun handleAddEditNoteEvent(noteId: Int) {
        val intent = Intent(activity, AddEditNoteActivity::class.java)
        intent.putExtra(Constants.EXTRA_FAVORITE_NOTES, isFavoriteNotes)
        if (noteId != -1)
            intent.putExtra(Constants.EXTRA_NOTE_ID, noteId)

        dismissKeyboard()
        activity?.startActivity(intent)
        activity?.overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
    }

    private fun handleDeleteNoteEvent(noteId: Int) {
        Snackbar.make(
                mSearchNotesBinding.root, getString(R.string.note_has_been_deleted),
                Snackbar.LENGTH_LONG
        ).setAction(getString(R.string.undo)) {
            mSharedViewModel.restoreNote(noteId)
        }.show()
    }

    private fun observeSnackbar() {
        mSharedViewModel.getSnackbar().observe(this, Observer {
            Snackbar.make(
                    mSearchNotesBinding.root, getString(it!!),
                    Snackbar.LENGTH_LONG
            ).show()
        })
    }

    private fun showKeyboard() {
        mSearchNotesBinding.etSearch.requestFocus()

        val inputMethodManager = activity?.getSystemService(Service
                .INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0)
    }

    private fun dismissKeyboard() {
        val inputMethodManager = activity?.getSystemService(Service
                .INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(mSearchNotesBinding
                .etSearch.windowToken, 0)
    }

    private fun popBackStack() {
        dismissKeyboard()
        Handler().postDelayed({
            (activity as MainActivity).supportFragmentManager?.popBackStack()
        }, 250)
    }

    override fun onDetach() {
        super.onDetach()
        mSearchDisposable.dispose()
        mSharedViewModel.disposeSearchedNotesDisposable()
        App.getApp(activity?.applicationContext).componentsHolder
                .releaseComponent(javaClass)
    }
}