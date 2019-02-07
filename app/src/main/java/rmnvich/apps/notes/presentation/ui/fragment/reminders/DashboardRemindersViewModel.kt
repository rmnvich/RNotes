package rmnvich.apps.notes.presentation.ui.fragment.reminders

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.databinding.ObservableBoolean
import io.reactivex.disposables.CompositeDisposable
import rmnvich.apps.notes.R
import rmnvich.apps.notes.domain.entity.Reminder
import rmnvich.apps.notes.domain.interactors.dashboardreminders.DashboardRemindersInteractor
import rmnvich.apps.notes.domain.utils.SingleLiveEvent

class DashboardRemindersViewModel(
    private val mDashboardRemindersInteractor: DashboardRemindersInteractor,
    private val isCompletedReminders: Boolean
) : ViewModel() {

    private val mCompositeDisposable: CompositeDisposable = CompositeDisposable()

    val bIsShowingProgressBar: ObservableBoolean = ObservableBoolean(false)

    val bRemindersIsEmpty: ObservableBoolean = ObservableBoolean(false)

    var bIsRecyclerNeedToScroll: Boolean = false

    private val mEditReminderEvent = SingleLiveEvent<Int>()

    private val mAddReminderEvent = SingleLiveEvent<Void>()

    private val mSnackbarMessage = SingleLiveEvent<Int>()

    private var mRemindersResponse: MutableLiveData<List<Reminder>>? = null

    fun getSnackbar(): SingleLiveEvent<Int> = mSnackbarMessage

    fun getEditReminderEvent(): SingleLiveEvent<Int> = mEditReminderEvent

    fun getAddReminderEvent(): SingleLiveEvent<Void> = mAddReminderEvent

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

    fun addReminder() {
        mAddReminderEvent.call()
        bIsRecyclerNeedToScroll = true
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

    fun doneOrUndoneReminder(reminderId: Int, isDone: Boolean) {
        mCompositeDisposable.add(
            mDashboardRemindersInteractor
                .doneOrUndoneReminder(reminderId, isDone)
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