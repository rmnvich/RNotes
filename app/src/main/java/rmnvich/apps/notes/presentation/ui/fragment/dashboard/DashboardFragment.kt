package rmnvich.apps.notes.presentation.ui.fragment.dashboard

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import rmnvich.apps.notes.App
import rmnvich.apps.notes.R

class DashboardFragment : Fragment() {

    companion object {
        fun newInstance() = DashboardFragment()
    }

    private lateinit var dashboardViewModel: DashboardViewModel

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        App.getApp(activity?.applicationContext).componentsHolder
                .getComponent(javaClass)?.inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.dashboard_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        dashboardViewModel = ViewModelProviders.of(this)
                .get(DashboardViewModel::class.java)
    }

    override fun onDetach() {
        super.onDetach()
        App.getApp(activity?.applicationContext).componentsHolder
                .releaseComponent(javaClass)
    }
}
