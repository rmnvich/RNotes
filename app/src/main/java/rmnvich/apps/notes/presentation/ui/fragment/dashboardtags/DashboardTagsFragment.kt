package rmnvich.apps.notes.presentation.ui.fragment.dashboardtags

import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import rmnvich.apps.notes.R

class DashboardTagsFragment : Fragment() {

    companion object {
        fun newInstance() = DashboardTagsFragment()
    }

    private lateinit var viewModel: DashboardTagsViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.dashboard_tags_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(DashboardTagsViewModel::class.java)
        // TODO: Use the ViewModel
    }

}
