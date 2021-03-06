package rmnvich.apps.notes.presentation.ui.adapter.note

import android.support.v7.util.DiffUtil
import rmnvich.apps.notes.domain.entity.NoteWithTag

class NotesDiffUtil(
        private val oldList: List<NoteWithTag>,
        private val newList: List<NoteWithTag>
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
        return oldNote.noteText == newNote.noteText &&
                oldNote.noteTitle == newNote.noteTitle &&
                oldNote.noteTimestamp == newNote.noteTimestamp &&
                oldNote.noteColor == newNote.noteColor &&
                oldNote.noteImagePath == newNote.noteImagePath &&
                oldNote.tagName == newNote.tagName &&
                oldNote.noteIsFavorite == newNote.noteIsFavorite &&
                oldNote.noteIsDeleted == newNote.noteIsDeleted &&
                oldNote.noteIsLocked == newNote.noteIsLocked
    }
}