package rmnvich.apps.notes.presentation.ui.fragment.dashboardnotes

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log.d
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import rmnvich.apps.notes.App
import rmnvich.apps.notes.R
import rmnvich.apps.notes.databinding.DashboardNotesFragmentBinding
import rmnvich.apps.notes.di.dashboardnotes.DashboardNotesModule
import rmnvich.apps.notes.domain.entity.Note
import rmnvich.apps.notes.presentation.utils.DebugLogger
import rmnvich.apps.notes.presentation.utils.DebugLogger.Companion.log
import rmnvich.apps.notes.presentation.utils.ViewModelFactory
import javax.inject.Inject

class DashboardNotesFragment : Fragment() {

    @Inject
    lateinit var mViewModelFactory: ViewModelFactory

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

        return mDashboardNotesBinding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        mDashboardNotesViewModel = ViewModelProviders.of(this, mViewModelFactory)
                .get(DashboardNotesViewModel::class.java)
    }

    override fun onResume() {
        super.onResume()
        mDashboardNotesViewModel.getNotes()
                .observe(this, Observer<List<Note>> {
                    DebugLogger.log("List size = ${it?.size}")
                    mDashboardNotesViewModel.bIsShowingProgressBar.set(false)
                    mDashboardNotesViewModel.bDataIsEmpty.set(it?.isEmpty()!!)
                })
    }

    override fun onDetach() {
        super.onDetach()
        App.getApp(activity?.applicationContext).componentsHolder
                .releaseComponent(javaClass)
    }
}