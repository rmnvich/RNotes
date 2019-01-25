package rmnvich.apps.notes.presentation.ui.adapter.trash

import android.support.v7.util.DiffUtil
import rmnvich.apps.notes.domain.entity.Note

class TrashDiffUtil(private val oldList: List<Note>,
                    private val newList: List<Note>
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
        return oldNote.noteId == newNote.noteId
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldNote = oldList[oldItemPosition]
        val newNote = newList[newItemPosition]
        return oldNote.text == newNote.text &&
                oldNote.timestamp == newNote.timestamp &&
                oldNote.isDeleted == newNote.isDeleted
    }
}