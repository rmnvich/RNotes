package rmnvich.apps.notes.presentation.ui.fragment.reminders

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.content.Intent
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.*
import com.daimajia.swipe.util.Attributes
import org.jetbrains.anko.noButton
import org.jetbrains.anko.support.v4.alert
import org.jetbrains.anko.yesButton
import rmnvich.apps.notes.App
import rmnvich.apps.notes.R
import rmnvich.apps.notes.data.common.Constants
import rmnvich.apps.notes.databinding.DashboardRemindersFragmentBinding
import rmnvich.apps.notes.di.reminders.DashboardRemindersModule
import rmnvich.apps.notes.domain.entity.Reminder
import rmnvich.apps.notes.domain.utils.ViewModelFactory
import rmnvich.apps.notes.presentation.ui.activity.addeditreminder.AddEditReminderActivity
import rmnvich.apps.notes.presentation.ui.adapter.reminder.RemindersAdapter
import javax.inject.Inject

class DashboardRemindersFragment : Fragment() {

    @Inject
    lateinit var mViewModelFactory: ViewModelFactory

    @Inject
    lateinit var mRemindersAdapter: RemindersAdapter

    private lateinit var mDashboardRemindersViewModel: DashboardRemindersViewModel
    private lateinit var mDashboardRemindersBinding: DashboardRemindersFragmentBinding

    private var isCompletedReminders = false

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        val componentHolder = App.getApp(activity?.applicationContext).componentsHolder
        isCompletedReminders = arguments?.getBoolean(Constants.EXTRA_COMPLETED_REMINDERS)!!

        if (!componentHolder.isComponentReleased(javaClass))
            componentHolder.releaseComponent(javaClass)

        componentHolder.getComponent(
                javaClass,
                DashboardRemindersModule(isCompletedReminders)
        )?.inject(this)
    }

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View {
        mDashboardRemindersBinding = DataBindingUtil.inflate(
                inflater,
                R.layout.dashboard_reminders_fragment, container, false
        )

        if (isCompletedReminders) {
            mDashboardRemindersBinding.tvEmpty.text = getString(R.string.you_have_no_completed_reminders)
            mDashboardRemindersBinding.ivEmpty.setImageResource(R.drawable.empty_completed_reminders)
        } else {
            mDashboardRemindersBinding.tvEmpty.text = getString(R.string.you_have_no_reminders)
            mDashboardRemindersBinding.ivEmpty.setImageResource(R.drawable.empty_reminders)
        }

        initRecyclerView()
        setHasOptionsMenu(true)

        return mDashboardRemindersBinding.root
    }

    private fun initRecyclerView() {
        mDashboardRemindersBinding.recyclerReminders.layoutManager = LinearLayoutManager(
                context,
                LinearLayoutManager.VERTICAL, false
        )

        mRemindersAdapter.mode = Attributes.Mode.Multiple
        mRemindersAdapter.setOnItemClickListener(
                onClickReminder = { mDashboardRemindersViewModel.editReminder(it) },
                onClickDelete = { mDashboardRemindersViewModel.deleteReminder(it) },
                onClickComplete = { reminderId, isCompleted ->
                    mDashboardRemindersViewModel.doneOrUndoneReminder(reminderId, isCompleted)
                },
                onClickPin = { reminderId, isPinned ->
                    mDashboardRemindersViewModel.pinOrUnpinReminder(reminderId, isPinned)
                }
        )
        mDashboardRemindersBinding.recyclerReminders.adapter = mRemindersAdapter
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater?.inflate(R.menu.reminders_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        return when (item?.itemId) {
            R.id.menu_empty_completed_reminders -> {
                alert(getString(R.string.restore_reminders_impossible), getString(R.string.are_you_sure)) {
                    yesButton { mDashboardRemindersViewModel.deleteCompletedReminders() }
                    noButton {}
                }.show()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val viewModelKey = if (isCompletedReminders) {
            Constants.KEY_COMPLETED_REMINDERS
        } else Constants.KEY_ACTIVE_REMINDERS

        mDashboardRemindersViewModel = ViewModelProviders.of(activity!!, mViewModelFactory)
                .get(viewModelKey, DashboardRemindersViewModel::class.java)
        mDashboardRemindersBinding.viewmodel = mDashboardRemindersViewModel

        mDashboardRemindersViewModel.getReminders()?.observe(this,
                Observer { handleRemindersResponse(it!!) })
        mDashboardRemindersViewModel.getEditReminderEvent().observe(this,
                Observer { handleEditReminderEvent(it!!) })
        observeSnackbar()
    }

    private fun handleRemindersResponse(response: List<Reminder>) {
        mRemindersAdapter.setData(response)
    }

    private fun handleEditReminderEvent(reminderId: Int) {
        val intent = Intent(activity, AddEditReminderActivity::class.java)
        intent.putExtra(Constants.EXTRA_REMINDER_ID, reminderId)

        activity?.startActivity(intent)
        activity?.overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
    }

    private fun observeSnackbar() {
        mDashboardRemindersViewModel.getSnackbar().observe(this, Observer {
            Snackbar.make(
                    mDashboardRemindersBinding.root, getString(it!!),
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