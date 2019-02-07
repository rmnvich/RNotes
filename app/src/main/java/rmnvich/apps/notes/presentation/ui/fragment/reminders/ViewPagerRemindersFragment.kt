package rmnvich.apps.notes.presentation.ui.fragment.reminders

import android.content.Intent
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItemAdapter
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItems
import kotlinx.android.synthetic.main.main_activity.*
import rmnvich.apps.notes.R
import rmnvich.apps.notes.data.common.Constants
import rmnvich.apps.notes.databinding.ViewPagerRemindersFragmentBinding
import rmnvich.apps.notes.presentation.ui.activity.addeditreminder.AddEditReminderActivity
import rmnvich.apps.notes.presentation.ui.activity.main.MainActivity

class ViewPagerRemindersFragment : Fragment() {

    private lateinit var binding: ViewPagerRemindersFragmentBinding

    private lateinit var mAdapter: FragmentPagerItemAdapter

    companion object {
        fun newInstance(): ViewPagerRemindersFragment {
            return ViewPagerRemindersFragment()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.view_pager_reminders_fragment, container, false)

        (activity as MainActivity).setSupportActionBar(binding.remindersToolbar)
        binding.remindersToolbar.setNavigationOnClickListener {
            (activity as MainActivity).drawer_layout.openDrawer(Gravity.START)
        }

        val activeRemindersArgument = Bundle()
        activeRemindersArgument.putBoolean(Constants.EXTRA_COMPLETED_REMINDERS, false)

        val completedRemindersArgument = Bundle()
        completedRemindersArgument.putBoolean(Constants.EXTRA_COMPLETED_REMINDERS, true)

        mAdapter = FragmentPagerItemAdapter(
                childFragmentManager, FragmentPagerItems.with(context)
                .add(R.string.active_reminders, DashboardRemindersFragment::class.java, activeRemindersArgument)
                .add(R.string.completed_reminders, DashboardRemindersFragment::class.java, completedRemindersArgument)
                .create()
        )

        binding.fabAddReminder.setOnClickListener {
            val intent = Intent(activity, AddEditReminderActivity::class.java)

            activity?.startActivity(intent)
            activity?.overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.remindersViewpager.adapter = mAdapter
        binding.viewpagertab.setViewPager(binding.remindersViewpager)
    }
}