package rmnvich.apps.notes.data.repositories.datasource.dao

import android.arch.persistence.room.*
import io.reactivex.Flowable
import io.reactivex.Single
import rmnvich.apps.notes.domain.entity.Reminder

@Dao
interface ReminderDao {

    @Query("SELECT * FROM reminder WHERE isCompleted = :isCompleted ORDER BY timeRemind DESC")
    fun getReminders(isCompleted: Boolean): Flowable<List<Reminder>>

    @Query("SELECT * FROM reminder WHERE id = :reminderId")
    fun getReminderById(reminderId: Int): Single<Reminder>

    @Query("UPDATE reminder SET isCompleted = :isCompleted WHERE id = :reminderId")
    fun doneOrUndoneReminder(reminderId: Int, isCompleted: Boolean)

    @Insert
    fun insertReminder(reminder: Reminder)

    @Update
    fun updateReminder(reminder: Reminder)

    @Delete
    fun deleteReminder(reminder: Reminder)
}