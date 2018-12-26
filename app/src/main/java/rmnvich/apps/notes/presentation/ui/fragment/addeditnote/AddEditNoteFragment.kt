package rmnvich.apps.notes.presentation.ui.fragment.addeditnote

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.app.Fragment
import android.view.*
import android.view.inputmethod.InputMethodManager
import rmnvich.apps.notes.App
import rmnvich.apps.notes.R
import rmnvich.apps.notes.databinding.AddEditNoteFragmentBinding
import rmnvich.apps.notes.di.addeditnote.AddEditNoteModule
import rmnvich.apps.notes.presentation.ui.activity.MainActivity
import rmnvich.apps.notes.domain.utils.ViewModelFactory
import rmnvich.apps.notes.presentation.ui.fragment.dashboardnotes.DashboardNotesViewModel
import javax.inject.Inject


class AddEditNoteFragment : Fragment() {

    @Inject
    lateinit var mViewModelFactory: ViewModelFactory

    private lateinit var mAddEditNoteViewModel: AddEditNoteViewModel
    private lateinit var mAddEditNoteBinding: AddEditNoteFragmentBinding

    companion object {
        fun newInstance() = AddEditNoteFragment()
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        App.getApp(activity?.applicationContext).componentsHolder
            .getComponent(javaClass, AddEditNoteModule(activity?.application!!))
            ?.inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mAddEditNoteBinding = DataBindingUtil.inflate(
            inflater,
            R.layout.add_edit_note_fragment, container, false
        )

        mAddEditNoteViewModel = ViewModelProviders.of(activity!!, mViewModelFactory)
            .get(AddEditNoteViewModel::class.java)
        mAddEditNoteBinding.viewmodel = mAddEditNoteViewModel

        mAddEditNoteBinding.swipeRefreshLayout.isEnabled = false
        mAddEditNoteBinding.swipeRefreshLayout.setColorSchemeResources(
            R.color.colorPrimary, R.color.colorAccent
        )

        (activity as MainActivity).setSupportActionBar(mAddEditNoteBinding.toolbar)
        mAddEditNoteBinding.toolbar.setNavigationOnClickListener { activity?.onBackPressed() }

        setHasOptionsMenu(true)
        return mAddEditNoteBinding.root
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater?.inflate(R.menu.add_edit_note_menu, menu)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val sharedViewModel = ViewModelProviders.of(activity!!)
            .get(DashboardNotesViewModel::class.java)

        sharedViewModel.getSelected().observe(this,
            Observer { noteId ->
                if (noteId != null)
                    mAddEditNoteViewModel.getNote(noteId)
            })
        observeSnackbar()
        observeFab()
    }

    override fun onResume() {
        super.onResume()
        showKeyboard()
    }

    override fun onPause() {
        super.onPause()
        dismissKeyboard()
    }

    private fun observeFab() {
        mAddEditNoteViewModel.getInsertNoteEvent().observe(this,
            Observer { activity?.onBackPressed() })
    }

    private fun observeSnackbar() {
        mAddEditNoteViewModel.getSnackbar().observe(this, Observer {
            Snackbar.make(
                mAddEditNoteBinding.root, getString(it!!),
                Snackbar.LENGTH_LONG
            ).show()
        })
    }

    private fun showKeyboard() {
        mAddEditNoteBinding.etText.requestFocus()
        val inputMethodManager = activity!!.getSystemService(
            Context.INPUT_METHOD_SERVICE
        ) as InputMethodManager
        inputMethodManager.toggleSoftInput(
            InputMethodManager.SHOW_FORCED,
            InputMethodManager.HIDE_IMPLICIT_ONLY
        )
    }

    private fun dismissKeyboard() {
        val inputMethodManager = activity?.getSystemService(
            Context.INPUT_METHOD_SERVICE
        ) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(
            mAddEditNoteBinding.etText.windowToken, 0
        )
    }

    override fun onDetach() {
        super.onDetach()
        App.getApp(activity?.applicationContext).componentsHolder
            .releaseComponent(javaClass)
    }
}