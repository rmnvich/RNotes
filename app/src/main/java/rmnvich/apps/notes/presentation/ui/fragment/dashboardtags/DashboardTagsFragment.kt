package rmnvich.apps.notes.presentation.ui.fragment.dashboardtags

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.main_activity.*
import rmnvich.apps.notes.App

import rmnvich.apps.notes.R
import rmnvich.apps.notes.databinding.DashboardTagsFragmentBinding
import rmnvich.apps.notes.domain.entity.Tag
import rmnvich.apps.notes.domain.utils.ViewModelFactory
import rmnvich.apps.notes.presentation.ui.activity.main.MainActivity
import rmnvich.apps.notes.presentation.ui.adapter.tag.TagsAdapter
import javax.inject.Inject

class DashboardTagsFragment : Fragment() {

    @Inject
    lateinit var mViewModelFactory: ViewModelFactory

    @Inject
    lateinit var mAdapter: TagsAdapter

    private lateinit var mDashboardTagsViewModel: DashboardTagsViewModel
    private lateinit var mDashboardTagsBinding: DashboardTagsFragmentBinding

    companion object {
        fun newInstance() = DashboardTagsFragment()
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
        mDashboardTagsBinding = DataBindingUtil.inflate(
                inflater,
                R.layout.dashboard_tags_fragment, container, false
        )

        mDashboardTagsViewModel = ViewModelProviders.of(activity!!, mViewModelFactory)
                .get(DashboardTagsViewModel::class.java)
        mDashboardTagsBinding.viewmodel = mDashboardTagsViewModel

        mDashboardTagsBinding.swipeRefreshLayout.setColorSchemeResources(R.color.colorAccent)

        mDashboardTagsBinding.recyclerTags.layoutManager = LinearLayoutManager(
                context, LinearLayoutManager.VERTICAL, false)
        mDashboardTagsBinding.recyclerTags.adapter = mAdapter
        mAdapter.setOnTagClickListener(
                onClickDelete = {

                }, onClickApply = { tagId, tagName ->

        })

        (activity as MainActivity).toolbar.setTitle(R.string.title_tags)

        return mDashboardTagsBinding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        mDashboardTagsViewModel.getTags(false)?.observe(this,
                Observer<List<Tag>> { handleResponse(it!!) })
        observeSnackbar()
    }

    private fun handleResponse(response: List<Tag>) {
        mAdapter.setData(response)
    }

    private fun observeSnackbar() {
        mDashboardTagsViewModel.getSnackbar().observe(this, Observer {
            Snackbar.make(
                    mDashboardTagsBinding.root, getString(it!!),
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
