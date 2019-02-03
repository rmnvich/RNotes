package rmnvich.apps.notes.presentation.ui.fragment.dashboardtags

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.helper.ItemTouchHelper
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.main_activity.*
import org.jetbrains.anko.noButton
import org.jetbrains.anko.support.v4.alert
import org.jetbrains.anko.yesButton
import rmnvich.apps.notes.App

import rmnvich.apps.notes.R
import rmnvich.apps.notes.databinding.DashboardTagsFragmentBinding
import rmnvich.apps.notes.domain.entity.Tag
import rmnvich.apps.notes.domain.utils.ViewModelFactory
import rmnvich.apps.notes.presentation.ui.activity.main.MainActivity
import rmnvich.apps.notes.presentation.ui.adapter.tag.SwipeToDeleteCallback
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

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        mDashboardTagsBinding = DataBindingUtil.inflate(inflater,
                R.layout.dashboard_tags_fragment, container, false)

        mDashboardTagsBinding.swipeRefreshLayout
                .setColorSchemeResources(R.color.colorAccent)
        mDashboardTagsBinding.swipeRefreshLayout.isEnabled = false

        initRecyclerView()
        initToolbar()

        return mDashboardTagsBinding.root
    }

    private fun initToolbar() {
        (activity as MainActivity).setSupportActionBar(mDashboardTagsBinding.tagsToolbar)
        mDashboardTagsBinding.tagsToolbar.setNavigationOnClickListener {
            (activity as MainActivity).drawer_layout.openDrawer(Gravity.START)
        }
    }

    private fun initRecyclerView() {
        mDashboardTagsBinding.recyclerTags.layoutManager = LinearLayoutManager(
                context, LinearLayoutManager.VERTICAL, false)
        mDashboardTagsBinding.recyclerTags.adapter = mAdapter
        mAdapter.setOnTagClickListener(onClickApply = { tagId, tagName ->
            mDashboardTagsViewModel.updateTag(tagId, tagName)
        })
        ItemTouchHelper(object : SwipeToDeleteCallback(context!!) {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                handleDeletingTag(mAdapter.mTagList[viewHolder.adapterPosition],
                        viewHolder.adapterPosition)
            }
        }).attachToRecyclerView(mDashboardTagsBinding.recyclerTags)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        mDashboardTagsViewModel = ViewModelProviders.of(activity!!, mViewModelFactory)
                .get(DashboardTagsViewModel::class.java)
        mDashboardTagsBinding.viewmodel = mDashboardTagsViewModel

        mDashboardTagsViewModel.getTags()?.observe(this,
                Observer<List<Tag>> { handleResponse(it!!) })
        mDashboardTagsViewModel.getDeleteTaskCommand().observe(this,
                Observer { handleTagDeleteEvent() })
        observeSnackbar()
    }

    private fun handleDeletingTag(tag: Tag, position: Int) {
        alert(getString(R.string.alert_dialog_delete_tag_message), getString(R.string.alert_dialog_delete_tag_title)) {
            yesButton { mDashboardTagsViewModel.deleteTag(tag) }
            noButton { mAdapter.notifyItemChanged(position) }
        }.show()
    }

    private fun handleTagDeleteEvent() {
        Snackbar.make(mDashboardTagsBinding.root, getString(R.string.tag_has_been_deleted),
                Snackbar.LENGTH_LONG).show()
    }

    private fun handleResponse(response: List<Tag>) {
        mAdapter.setData(response)

        if (mDashboardTagsViewModel.bIsRecyclerNeedToScroll)
            mDashboardTagsBinding.recyclerTags.scrollToPosition(0)
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
