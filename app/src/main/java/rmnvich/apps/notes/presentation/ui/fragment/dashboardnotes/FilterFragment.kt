package rmnvich.apps.notes.presentation.ui.fragment.dashboardnotes

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import rmnvich.apps.notes.App
import rmnvich.apps.notes.R
import rmnvich.apps.notes.data.common.Constants.*
import rmnvich.apps.notes.databinding.FilterFragmentBinding
import rmnvich.apps.notes.domain.entity.Filter
import rmnvich.apps.notes.domain.entity.Tag
import rmnvich.apps.notes.presentation.ui.activity.main.MainActivity
import rmnvich.apps.notes.presentation.ui.adapter.filter.CheckableCirclesAdapter
import rmnvich.apps.notes.presentation.ui.adapter.filter.CheckableTagsAdapter
import javax.inject.Inject

class FilterFragment : Fragment() {

    @Inject
    lateinit var mTagsAdapter: CheckableTagsAdapter

    @Inject
    lateinit var mCirclesAdapter: CheckableCirclesAdapter

    private lateinit var mSharedViewModel: DashboardNotesViewModel
    private lateinit var mFilterBinding: FilterFragmentBinding

    private var isFavoriteNotes = false

    companion object {
        fun newInstance(isFavoriteNotes: Boolean): FilterFragment {
            val args = Bundle()
            args.putBoolean(EXTRA_FAVORITE_NOTES, isFavoriteNotes)

            val fragment = FilterFragment()
            fragment.arguments = args

            return fragment
        }
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        isFavoriteNotes = arguments?.getBoolean(EXTRA_FAVORITE_NOTES)!!
        App.getApp(activity?.applicationContext).componentsHolder
                .getComponent(javaClass).inject(this)
    }

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View {
        mFilterBinding = DataBindingUtil.inflate(
                inflater,
                R.layout.filter_fragment, container, false
        )

        mFilterBinding.layoutExpandTags.setOnClickListener {
            if (mFilterBinding.tagsExpandableLayout.isExpanded) {
                mFilterBinding.tagsExpandableLayout.collapse()
                mFilterBinding.ivTagsDropdown
                        .setImageResource(R.drawable.ic_action_keyboard_arrow_down)
            } else {
                mFilterBinding.tagsExpandableLayout.expand()
                mFilterBinding.ivTagsDropdown
                        .setImageResource(R.drawable.ic_action_keyboard_arrow_up)
            }
        }

        mFilterBinding.layoutExpandColors.setOnClickListener {
            if (mFilterBinding.colorsExpandableLayout.isExpanded) {
                mFilterBinding.colorsExpandableLayout.collapse()
                mFilterBinding.ivColorsDropdown
                        .setImageResource(R.drawable.ic_action_keyboard_arrow_down)
            } else {
                mFilterBinding.colorsExpandableLayout.expand()
                mFilterBinding.ivColorsDropdown
                        .setImageResource(R.drawable.ic_action_keyboard_arrow_up)
            }
        }

        initToolbar()
        initRecyclerView()

        return mFilterBinding.root
    }

    private fun initToolbar() {
        (activity as MainActivity).setSupportActionBar(mFilterBinding.filterToolbar)
        mFilterBinding.filterToolbar.setNavigationOnClickListener { popBackStack() }
    }

    private fun initRecyclerView() {
        mFilterBinding.recyclerCheckableTags.layoutManager =
                LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        mFilterBinding.recyclerCheckableTags.adapter = mTagsAdapter

        mFilterBinding.recyclerCheckableColors.layoutManager =
                LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        mFilterBinding.recyclerCheckableColors.adapter = mCirclesAdapter
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val viewModelKey = if (isFavoriteNotes) {
            KEY_IS_FAVORITE_NOTES
        } else KEY_ALL_NOTES

        mSharedViewModel = ViewModelProviders.of(activity!!)
                .get(viewModelKey, DashboardNotesViewModel::class.java)
        mFilterBinding.viewmodel = mSharedViewModel

        mSharedViewModel.getTags(false)?.observe(this,
                Observer { handleTagsResponse(it!!) })
        mSharedViewModel.getApplyFilterEvent().observe(this,
                Observer { handleApplyFilterEvent() })
        mSharedViewModel.getResetFilterEvent().observe(this,
                Observer { handleResetFilterEvent() })
    }

    private fun handleApplyFilterEvent() {
        mSharedViewModel.getSharedFilter().value = Filter(
                mCirclesAdapter.mCheckedColors,
                mTagsAdapter.mCheckedTags
        )
        popBackStack()
    }

    private fun handleResetFilterEvent() {
        mSharedViewModel.getSharedFilter().value = Filter(emptyList(), emptyList())
        popBackStack()
    }

    private fun handleTagsResponse(tags: List<Tag>) {
        mTagsAdapter.setData(tags)
    }

    private fun popBackStack() {
        (activity as MainActivity).supportFragmentManager?.popBackStack()
    }

    override fun onDetach() {
        super.onDetach()
        App.getApp(activity?.applicationContext).componentsHolder
                .releaseComponent(javaClass)
    }
}