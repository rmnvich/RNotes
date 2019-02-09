package rmnvich.apps.notes.domain.repositories

import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Single
import rmnvich.apps.notes.domain.entity.Reminder

interface RemindersRepository {

    fun getReminders(isCompleted: Boolean): Flowable<List<Reminder>>

    fun getReminderById(reminderId: Int): Single<Reminder>

    fun doneOrUndoneReminder(reminderId: Int, isCompleted: Boolean): Completable

    fun insertReminder(reminder: Reminder): Completable

    fun updateReminder(reminder: Reminder, reminderId: Int): Completable

    fun deleteReminder(reminder: Reminder): Completable

    fun deleteCompletedReminders(): Completable
}