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
        val oldNote = oldList[oldItemPosition]
        val newNote = newList[newItemPosition]
        return oldNote.id == newNote.id
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldNote = oldList[oldItemPosition]
        val newNote = newList[newItemPosition]
        return oldNote.text == newNote.text &&
                oldNote.isCompleted == newNote.isCompleted &&
                oldNote.repeatType == newNote.repeatType &&
                oldNote.timeRemind == newNote.timeRemind
    }
}