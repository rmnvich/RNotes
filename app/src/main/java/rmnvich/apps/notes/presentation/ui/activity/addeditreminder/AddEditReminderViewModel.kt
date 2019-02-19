package rmnvich.apps.notes.presentation.ui.activity.addeditreminder

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.databinding.ObservableBoolean
import android.databinding.ObservableField
import io.reactivex.disposables.CompositeDisposable
import rmnvich.apps.notes.R
import rmnvich.apps.notes.domain.entity.Reminder
import rmnvich.apps.notes.domain.interactors.addeditreminder.AddEditReminderInteractor
import rmnvich.apps.notes.domain.utils.SingleLiveEvent
import rmnvich.apps.notes.presentation.utils.DateHelper
import rmnvich.apps.notes.presentation.utils.DateHelper.getDefaultTimeRemindInMills
import java.util.concurrent.ThreadLocalRandom

class AddEditReminderViewModel(
        private val applicationContext: Application,
        private val addEditReminderInteractor: AddEditReminderInteractor
) : AndroidViewModel(applicationContext) {

    private val mCompositeDisposable = CompositeDisposable()

    private var mReminderResponse: MutableLiveData<Reminder>? = null

    val bIsShowingProgressBar = ObservableBoolean(false)

    val reminderText = ObservableField("")

    val repeatType = ObservableField(0)

    val repeatName = ObservableField(
            applicationContext.resources
                    .getStringArray(R.array.repeat_types)[0]
    )

    val timeRemind = ObservableField(
            getDefaultTimeRemindInMills()
    )

    var reminderId = 0

    private val onCreateClickEvent = SingleLiveEvent<Void>()

    private val onClickDateEvent = SingleLiveEvent<Long>()

    private val onClickTimeEvent = SingleLiveEvent<Long>()

    private val onClickRepeatEvent = SingleLiveEvent<Int>()

    private val mSnackbarMessage = SingleLiveEvent<Int>()

    fun getSnackbar(): SingleLiveEvent<Int> = mSnackbarMessage

    fun getCreateClickEvent(): SingleLiveEvent<Void> = onCreateClickEvent

    fun getClickDateEvent(): SingleLiveEvent<Long> = onClickDateEvent

    fun getClickTimeEvent(): SingleLiveEvent<Long> = onClickTimeEvent

    fun getClickRepeatEvent(): SingleLiveEvent<Int> = onClickRepeatEvent

    fun onClickDate() {
        onClickDateEvent.value = timeRemind.get()
    }

    fun onClickTime() {
        onClickTimeEvent.value = timeRemind.get()
    }

    fun onClickRepeat() {
        onClickRepeatEvent.value = repeatType.get()
    }

    fun onDatePickerDialogClicked(year: Int, month: Int, day: Int) {
        timeRemind.set(DateHelper.getDate(timeRemind.get()!!, year, month, day))
    }

    fun onTimePickerDialogClicked(hour: Int, minute: Int) {
        timeRemind.set(DateHelper.getTime(timeRemind.get()!!, hour, minute))
    }

    fun onRepeatDialogClicked(repeatType: Int) {
        this.repeatType.set(repeatType)
        repeatName.set(applicationContext.resources
                .getStringArray(R.array.repeat_types)[repeatType])
    }

    fun getReminder(reminderId: Int): LiveData<Reminder>? {
        if (mReminderResponse == null) {
            mReminderResponse = MutableLiveData()
            loadReminder(reminderId)
        }
        return mReminderResponse
    }

    fun insertOrUpdateReminder() {
        if (!reminderText.get()?.trim().isNullOrEmpty()) {
            val reminder = Reminder()
            reminder.text = reminderText.get()?.trim()!!
            reminder.timeRemind = timeRemind.get()!!
            reminder.repeatType = repeatType.get()!!

            if (reminderId == 0) {
                insertReminder(reminder)
            } else updateReminder(reminder, reminderId)
        } else showSnackbarMessage(R.string.empty_reminder)
    }

    private fun loadReminder(reminderId: Int) {
        mCompositeDisposable.add(addEditReminderInteractor.getReminderById(reminderId)
                .doOnSubscribe { bIsShowingProgressBar.set(true) }
                .subscribe({
                    bIsShowingProgressBar.set(false)
                    mReminderResponse?.value = it

                    this.reminderId = it.id

                    setObservableFields(it.text, it.timeRemind, it.repeatType)
                }, {
                    bIsShowingProgressBar.set(false)
                    showSnackbarMessage(R.string.error_message)
                })
        )
    }

    private fun insertReminder(reminder: Reminder) {
        bIsShowingProgressBar.set(true)
        mCompositeDisposable.add(
                addEditReminderInteractor
                        .insertReminder(reminder)
                        .subscribe({
                            bIsShowingProgressBar.set(false)
                            onCreateClickEvent.call()
                        }, {
                            bIsShowingProgressBar.set(false)
                            showSnackbarMessage(R.string.error_message)
                        })
        )
    }

    private fun updateReminder(reminder: Reminder, reminderId: Int) {
        bIsShowingProgressBar.set(true)
        mCompositeDisposable.add(
                addEditReminderInteractor
                        .updateReminder(reminder, reminderId)
                        .subscribe({
                            bIsShowingProgressBar.set(false)
                            onCreateClickEvent.call()
                        }, {
                            bIsShowingProgressBar.set(false)
                            showSnackbarMessage(R.string.error_message)
                        })
        )
    }

    private fun setObservableFields(reminderText: String, timeRemind: Long, repeatType: Int) {
        this.reminderText.set(reminderText)
        this.timeRemind.set(timeRemind)
        this.repeatType.set(repeatType)

        this.repeatName.set(
                applicationContext.resources
                        .getStringArray(R.array.repeat_types)[repeatType]
        )
    }

    private fun showSnackbarMessage(message: Int?) {
        mSnackbarMessage.value = message
    }

    override fun onCleared() {
        super.onCleared()
        mCompositeDisposable.clear()
    }
}