package rmnvich.apps.notes.presentation.ui.fragment.dashboardnotes

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.os.Handler
import android.support.design.widget.Snackbar
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.*
import rmnvich.apps.notes.App
import rmnvich.apps.notes.R
import rmnvich.apps.notes.databinding.DashboardNotesFragmentBinding
import rmnvich.apps.notes.di.dashboardnotes.DashboardNotesModule
import rmnvich.apps.notes.domain.entity.Note
import rmnvich.apps.notes.presentation.ui.activity.MainActivity
import rmnvich.apps.notes.presentation.ui.fragment.addeditnote.AddEditNoteFragment
import rmnvich.apps.notes.presentation.utils.DebugLogger
import rmnvich.apps.notes.domain.utils.ViewModelFactory
import rmnvich.apps.notes.presentation.ui.adapter.dashboard.NotesAdapter
import javax.inject.Inject


class DashboardNotesFragment : Fragment() {

    @Inject
    lateinit var mViewModelFactory: ViewModelFactory

    @Inject
    lateinit var mAdapter: NotesAdapter

    private lateinit var mDashboardNotesViewModel: DashboardNotesViewModel
    private lateinit var mDashboardNotesBinding: DashboardNotesFragmentBinding

    companion object {
        fun newInstance() = DashboardNotesFragment()
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        App.getApp(activity?.applicationContext).componentsHolder
                .getComponent(javaClass, DashboardNotesModule(activity?.application!!))
                ?.inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        mDashboardNotesBinding = DataBindingUtil.inflate(inflater,
                R.layout.dashboard_notes_fragment, container, false)

        mDashboardNotesViewModel = ViewModelProviders.of(activity!!, mViewModelFactory)
                .get(DashboardNotesViewModel::class.java)
        mDashboardNotesBinding.viewmodel = mDashboardNotesViewModel

        mDashboardNotesBinding.swipeRefreshLayout.isEnabled = false
        mDashboardNotesBinding.swipeRefreshLayout.setColorSchemeResources(
                R.color.colorPrimary, R.color.colorAccent)

        mDashboardNotesBinding.recyclerNotes.layoutManager = LinearLayoutManager(context,
                LinearLayoutManager.VERTICAL, false)
        mDashboardNotesBinding.recyclerNotes.adapter = mAdapter
        mAdapter.setOnItemClickLIstener { mDashboardNotesViewModel.selectNote(it) }

        (activity as MainActivity).setSupportActionBar(mDashboardNotesBinding.toolbar)
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
        mDashboardNotesViewModel.getAddEditNoteEvent().observe(this,
                Observer { handleAddEditNoteEvent() })
        observeSnackbar()
    }

    private fun handleResponse(response: List<Note>) {
        mAdapter.setData(response)
        mDashboardNotesBinding.recyclerNotes.scrollToPosition(0)
    }

    private fun handleAddEditNoteEvent() {
        activity?.supportFragmentManager?.beginTransaction()
                ?.setCustomAnimations(R.anim.fade_in, android.R.anim.fade_out,
                        R.anim.fade_in, R.anim.fade_out)
                ?.replace(R.id.container, AddEditNoteFragment.newInstance())
                ?.addToBackStack(null)
                ?.commit()
    }

    //TODO: Make snackbar with action
    private fun observeSnackbar() {
        mDashboardNotesViewModel.getSnackbar().observe(this, Observer {
            Snackbar.make(mDashboardNotesBinding.root, getString(it!!),
                    Snackbar.LENGTH_LONG).show()
        })
    }

    override fun onDetach() {
        super.onDetach()
        App.getApp(activity?.applicationContext).componentsHolder
                .releaseComponent(javaClass)
    }
}