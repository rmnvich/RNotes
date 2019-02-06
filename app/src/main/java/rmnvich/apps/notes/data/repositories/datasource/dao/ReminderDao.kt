package rmnvich.apps.notes.data.repositories.datasource.dao

import android.arch.persistence.room.*
import io.reactivex.Flowable
import io.reactivex.Single
import rmnvich.apps.notes.domain.entity.Reminder

@Dao
interface ReminderDao {

    @Query("SELECT * FROM reminder WHERE isDone = :isDone ORDER BY timeRemind DESC")
    fun getReminders(isDone: Boolean): Flowable<List<Reminder>>

    @Query("SELECT * FROM reminder WHERE id = :reminderId")
    fun getReminderById(reminderId: Int): Single<Reminder>

    @Query("UPDATE reminder SET isDone = :isDone WHERE id = :reminderId")
    fun doneOrUndoneReminder(reminderId: Int, isDone: Boolean)

    @Insert
    fun insertReminder(reminder: Reminder)

    @Update
    fun updateReminder(reminder: Reminder)

    @Delete
    fun deleteReminder(reminder: Reminder)
}