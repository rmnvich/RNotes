package rmnvich.apps.notes.data.repositories.datasource.dao

import android.arch.persistence.room.*
import io.reactivex.Flowable
import io.reactivex.Single
import rmnvich.apps.notes.domain.entity.Reminder

@Dao
interface ReminderDao {

    @Query("SELECT * FROM reminder WHERE isCompleted = :isCompleted ORDER BY isPinned DESC, timeRemind")
    fun getReminders(isCompleted: Boolean): Flowable<List<Reminder>>

    @Query("SELECT * FROM reminder WHERE id = :reminderId")
    fun getReminderById(reminderId: Int): Single<Reminder>

    @Query("UPDATE reminder SET isCompleted = :isCompleted WHERE id = :reminderId")
    fun doneOrUndoneReminder(reminderId: Int, isCompleted: Boolean)

    @Query("UPDATE reminder SET isPinned = :isPinned WHERE id = :reminderId")
    fun pinOrUnpinReminder(reminderId: Int, isPinned: Boolean)

    @Query("DELETE FROM reminder WHERE isCompleted = 1")
    fun deleteCompletedReminders()

    @Insert
    fun insertReminder(reminder: Reminder)

    @Update
    fun updateReminder(reminder: Reminder)

    @Delete
    fun deleteReminder(reminder: Reminder)
}