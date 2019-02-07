package rmnvich.apps.notes.presentation.ui.activity.addeditreminder

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.databinding.ObservableBoolean
import android.databinding.ObservableField
import io.reactivex.disposables.CompositeDisposable
import rmnvich.apps.notes.R
import rmnvich.apps.notes.domain.entity.Reminder
import rmnvich.apps.notes.domain.interactors.addeditreminder.AddEditReminderInteractor
import rmnvich.apps.notes.domain.utils.SingleLiveEvent

class AddEditReminderViewModel(
        private val addEditReminderInteractor: AddEditReminderInteractor
) : ViewModel() {

    private val mCompositeDisposable: CompositeDisposable = CompositeDisposable()

    private var mReminderResponse: MutableLiveData<Reminder>? = null

    val bIsShowingProgressBar: ObservableBoolean = ObservableBoolean(false)

    val reminderText: ObservableField<String> = ObservableField("")

    val timeRemind: ObservableField<Long> = ObservableField(0L)

    val repeatType: ObservableField<Int> = ObservableField(0)

    var reminderId: Int = 0

    private val onCreateClickEvent = SingleLiveEvent<Void>()

    private val onClickDateEvent = SingleLiveEvent<Void>()

    private val onClickTimeEvent = SingleLiveEvent<Void>()

    private val onClickRepeatEvent = SingleLiveEvent<Void>()

    private val mSnackbarMessage: SingleLiveEvent<Int> = SingleLiveEvent()

    fun getSnackbar(): SingleLiveEvent<Int> = mSnackbarMessage

    fun getCreateClickEvent(): SingleLiveEvent<Void> = onCreateClickEvent

    fun getClickDateEvent(): SingleLiveEvent<Void> = onClickDateEvent

    fun getClickTimeEvent(): SingleLiveEvent<Void> = onClickTimeEvent

    fun getClickRepeatEvent(): SingleLiveEvent<Void> = onClickRepeatEvent

    fun onClickDate() = onClickDateEvent.call()

    fun onClickTime() = onClickTimeEvent.call()

    fun onClickRepeat() = onClickRepeatEvent.call()

    fun onClickCreate() {
        onCreateClickEvent.call()
    }

    fun onDatePickerDialogClicked() {

    }

    fun onTimePickerDialogClicked() {

    }

    fun onRepeatDialogClicked(repeatType: Int) {

    }

    fun getReminder(reminderId: Int): LiveData<Reminder>? {
        if (mReminderResponse == null) {
            mReminderResponse = MutableLiveData()
            loadReminder(reminderId)
        }
        return mReminderResponse
    }

    fun insertOrUpdateReminder() {
        if (!reminderText.get().isNullOrEmpty()) {
            val reminder = Reminder()
            reminder.text = reminderText.get()?.trim()!!
            reminder.timeRemind = timeRemind.get()!!
            reminder.repeatType = repeatType.get()!!

            if (reminderId == 0) {
                insertReminder(reminder)
            } else updateReminder(reminder, reminderId)
        } else onCreateClickEvent.call()
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
    }

    private fun showSnackbarMessage(message: Int?) {
        mSnackbarMessage.value = message
    }

    override fun onCleared() {
        super.onCleared()
        mCompositeDisposable.clear()
    }
}