package rmnvich.apps.notes.presentation.ui.fragment.reminders

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.content.Intent
import android.databinding.ObservableBoolean
import io.reactivex.disposables.CompositeDisposable
import rmnvich.apps.notes.R
import rmnvich.apps.notes.domain.entity.Reminder
import rmnvich.apps.notes.domain.interactors.dashboardreminders.DashboardRemindersInteractor
import rmnvich.apps.notes.domain.utils.SingleLiveEvent
import rmnvich.apps.notes.presentation.utils.DateHelper

class DashboardRemindersViewModel(
        private val applicationContext: Application,
        private val mDashboardRemindersInteractor: DashboardRemindersInteractor,
        private val isCompletedReminders: Boolean
) : AndroidViewModel(applicationContext) {

    private val mCompositeDisposable: CompositeDisposable = CompositeDisposable()

    val bIsShowingProgressBar: ObservableBoolean = ObservableBoolean(false)

    val bRemindersIsEmpty: ObservableBoolean = ObservableBoolean(false)

    private val mEditReminderEvent = SingleLiveEvent<Int>()

    private val mShareReminderEvent = SingleLiveEvent<Intent>()

    private val mSnackbarMessage = SingleLiveEvent<Int>()

    private var mRemindersResponse: MutableLiveData<List<Reminder>>? = null

    fun getSnackbar(): SingleLiveEvent<Int> = mSnackbarMessage

    fun getShareReminderEvent(): SingleLiveEvent<Intent> = mShareReminderEvent

    fun getEditReminderEvent(): SingleLiveEvent<Int> = mEditReminderEvent

    private fun showSnackbarMessage(message: Int?) {
        mSnackbarMessage.value = message
    }

    fun getReminders(): LiveData<List<Reminder>>? {
        if (mRemindersResponse == null) {
            mRemindersResponse = MutableLiveData()
            loadReminders()
        }
        return mRemindersResponse
    }

    fun editReminder(reminderId: Int?) {
        mEditReminderEvent.value = reminderId
    }

    fun deleteReminder(reminder: Reminder) {
        mCompositeDisposable.add(
                mDashboardRemindersInteractor
                        .deleteReminder(reminder)
                        .subscribe({
                            showSnackbarMessage(R.string.reminder_has_been_deleted)
                        }, { showSnackbarMessage(R.string.error_message) })
        )
    }

    fun deleteCompletedReminders() {
        mCompositeDisposable.add(
                mDashboardRemindersInteractor
                        .deleteCompletedReminders()
                        .subscribe({
                            showSnackbarMessage(R.string.reminders_has_been_deleted)
                        }, { showSnackbarMessage(R.string.error_message) })
        )
    }

    fun doneOrUndoneReminder(reminderId: Int, isCompleted: Boolean) {
        mCompositeDisposable.add(
                mDashboardRemindersInteractor
                        .doneOrUndoneReminder(reminderId, isCompleted)
                        .subscribe()
        )
    }

    fun shareReminder(reminderId: Int) {
        mCompositeDisposable.add(
                mDashboardRemindersInteractor
                        .getReminder(reminderId)
                        .subscribe({
                            val intent = Intent(Intent.ACTION_SEND)

                            intent.type = "text/plain"
                            intent.putExtra(Intent.EXTRA_TEXT, "${it.text} \n\n" +
                                    "${applicationContext.getString(R.string.remind_me_at)} " +
                                    "${DateHelper.convertLongToTime(it.timeRemind)}, " +
                                    DateHelper.convertLongToDate(it.timeRemind))

                            mShareReminderEvent.value = intent
                        }, {})
        )
    }

    fun pinOrUnpinReminder(reminderId: Int, isPinned: Boolean) {
        mCompositeDisposable.add(
                mDashboardRemindersInteractor
                        .pinOrUnpinReminder(reminderId, isPinned)
                        .subscribe()
        )
    }

    private fun loadReminders() {
        mCompositeDisposable.add(mDashboardRemindersInteractor
                .getReminders(isCompletedReminders)
                .doOnSubscribe { bIsShowingProgressBar.set(true) }
                .subscribe({
                    bIsShowingProgressBar.set(false)
                    bRemindersIsEmpty.set(it.isEmpty())
                    mRemindersResponse?.value = it
                }, {
                    bIsShowingProgressBar.set(false)
                    showSnackbarMessage(R.string.error_message)
                })
        )
    }

    override fun onCleared() {
        super.onCleared()
        mCompositeDisposable.clear()
    }
}