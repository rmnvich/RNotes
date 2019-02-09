package rmnvich.apps.notes.presentation.ui.adapter.reminder

import android.support.v7.util.DiffUtil
import rmnvich.apps.notes.domain.entity.Reminder

class RemindersDiffUtil(
    private val oldList: List<Reminder>,
    private val newList: List<Reminder>
) : DiffUtil.Callback() {

    override fun getOldListSize(): Int {
        return oldList.size
    }

    override fun getNewListSize(): Int {
        return newList.size
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldReminder = oldList[oldItemPosition]
        val newReminder = newList[newItemPosition]
        return oldReminder.id == newReminder.id
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldReminder = oldList[oldItemPosition]
        val newReminder = newList[newItemPosition]
        return oldReminder.text == newReminder.text &&
                oldReminder.isCompleted == newReminder.isCompleted &&
                oldReminder.repeatType == newReminder.repeatType &&
                oldReminder.timeRemind == newReminder.timeRemind &&
                oldReminder.isPinned == newReminder.isPinned
    }
}