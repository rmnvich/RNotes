package rmnvich.apps.notes.data.repositories

import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Single
import rmnvich.apps.notes.data.repositories.datasource.Database
import rmnvich.apps.notes.domain.entity.Reminder
import rmnvich.apps.notes.domain.repositories.RemindersRepository

class ReminderRepositoryImpl(database: Database) : RemindersRepository {

    private val reminderDao = database.reminderDao()

    override fun getReminders(isDone: Boolean): Flowable<List<Reminder>> {
        return reminderDao.getReminders(isDone)
    }

    override fun getReminderById(reminderId: Int): Single<Reminder> {
        return reminderDao.getReminderById(reminderId)
    }

    override fun doneOrUndoneReminder(reminderId: Int, isDone: Boolean): Completable {
        return Completable.fromAction { reminderDao.doneOrUndoneReminder(reminderId, isDone) }
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
                        it.isDone = reminder.isDone
                        reminderDao.updateReminder(it)
                    }
                }
    }

    override fun deleteReminder(reminder: Reminder): Completable {
        return Completable.fromAction { reminderDao.deleteReminder(reminder) }
    }
}