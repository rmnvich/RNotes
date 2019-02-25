package rmnvich.apps.notes.domain.interactors.dashboardreminders

import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Single
import rmnvich.apps.notes.data.common.Constants
import rmnvich.apps.notes.data.common.SchedulersProvider
import rmnvich.apps.notes.domain.entity.Reminder
import rmnvich.apps.notes.domain.repositories.RemindersRepository
import java.util.concurrent.TimeUnit

class DashboardRemindersInteractor(
        private val remindersRepository: RemindersRepository,
        private val schedulersProvider: SchedulersProvider
) {

    fun getReminders(isDone: Boolean): Flowable<List<Reminder>> {
        return Flowable.timer(Constants.DEFAULT_DELAY, TimeUnit.MILLISECONDS)
                .flatMap { remindersRepository.getReminders(isDone) }
                .subscribeOn(schedulersProvider.io())
                .observeOn(schedulersProvider.ui())
    }

    fun getReminder(reminderId: Int): Single<Reminder> {
        return remindersRepository.getReminderById(reminderId)
                .subscribeOn(schedulersProvider.io())
                .observeOn(schedulersProvider.ui())
    }

    fun deleteReminder(reminder: Reminder): Completable {
        return remindersRepository.deleteReminder(reminder)
                .subscribeOn(schedulersProvider.io())
                .observeOn(schedulersProvider.ui())
    }

    fun deleteCompletedReminders(): Completable {
        return remindersRepository.deleteCompletedReminders()
                .subscribeOn(schedulersProvider.io())
                .observeOn(schedulersProvider.ui())
    }

    fun doneOrUndoneReminder(reminderId: Int, isCompleted: Boolean): Completable {
        return remindersRepository.doneOrUndoneReminder(reminderId, isCompleted)
                .subscribeOn(schedulersProvider.io())
                .observeOn(schedulersProvider.ui())
    }

    fun pinOrUnpinReminder(reminderId: Int, isPinned: Boolean): Completable {
        return remindersRepository.pinOrUnpinReminder(reminderId, isPinned)
                .subscribeOn(schedulersProvider.io())
                .observeOn(schedulersProvider.ui())
    }
}