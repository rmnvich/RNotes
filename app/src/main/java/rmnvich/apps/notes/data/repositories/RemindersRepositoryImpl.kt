package rmnvich.apps.notes.data.repositories

import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Single
import rmnvich.apps.notes.data.repositories.datasource.Database
import rmnvich.apps.notes.domain.entity.Reminder
import rmnvich.apps.notes.domain.repositories.RemindersRepository

class RemindersRepositoryImpl(database: Database) : RemindersRepository {

    private val reminderDao = database.reminderDao()

    override fun getReminders(isCompleted: Boolean): Flowable<List<Reminder>> {
        return reminderDao.getReminders(isCompleted)
    }

    override fun getReminderById(reminderId: Int): Single<Reminder> {
        return reminderDao.getReminderById(reminderId)
    }

    override fun doneOrUndoneReminder(reminderId: Int, isCompleted: Boolean): Completable {
        return Completable.fromAction { reminderDao.doneOrUndoneReminder(reminderId, isCompleted) }
    }

    override fun pinOrUnpinReminder(reminderId: Int, isPinned: Boolean): Completable {
        return Completable.fromAction { reminderDao.pinOrUnpinReminder(reminderId, isPinned) }
    }

    override fun insertReminder(reminder: Reminder): Completable {
        return Completable.fromAction { reminderDao.insertReminder(reminder) }
    }

    override fun updateReminder(reminder: Reminder, reminderId: Int): Completable {
        return getReminderById(reminderId)
                .flatMapCompletable {
                    Completable.fromAction {
                        it.text = reminder.text
                        it.repeatType = reminder.repeatType
                        it.timeRemind = reminder.timeRemind
                        reminderDao.updateReminder(it)
                    }
                }
    }

    override fun deleteReminder(reminder: Reminder): Completable {
        return Completable.fromAction { reminderDao.deleteReminder(reminder) }
    }

    override fun deleteCompletedReminders(): Completable {
        return Completable.fromAction { reminderDao.deleteCompletedReminders() }
    }
}