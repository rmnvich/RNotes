package rmnvich.apps.notes.domain.interactors.addeditreminder

import io.reactivex.Completable
import io.reactivex.Single
import rmnvich.apps.notes.data.common.SchedulersProvider
import rmnvich.apps.notes.domain.entity.Reminder
import rmnvich.apps.notes.domain.repositories.RemindersRepository

class AddEditReminderInteractor(
        private val remindersRepository: RemindersRepository,
        private val schedulersProvider: SchedulersProvider
) {

    fun getReminderById(reminderId: Int): Single<Reminder> {
        return remindersRepository.getReminderById(reminderId)
                .subscribeOn(schedulersProvider.io())
                .observeOn(schedulersProvider.ui())
    }

    fun insertReminder(reminder: Reminder): Completable {
        return remindersRepository.insertReminder(reminder)
                .subscribeOn(schedulersProvider.io())
                .observeOn(schedulersProvider.ui())
    }

    fun updateReminder(reminder: Reminder, reminderId: Int): Completable {
        return remindersRepository.updateReminder(reminder, reminderId)
                .subscribeOn(schedulersProvider.io())
                .observeOn(schedulersProvider.ui())
    }
}