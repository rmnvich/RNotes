package rmnvich.apps.notes.presentation.ui.adapter.tag

import android.support.v7.util.DiffUtil
import rmnvich.apps.notes.domain.entity.Tag

class TagsDiffUtil(
        private val oldList: List<Tag>,
        private val newList: List<Tag>
) : DiffUtil.Callback() {

    override fun getOldListSize(): Int {
        return oldList.size
    }

    override fun getNewListSize(): Int {
        return newList.size
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldTag = oldList[oldItemPosition]
        val newTag = newList[newItemPosition]
        return oldTag.id == newTag.id
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldTag = oldList[oldItemPosition]
        val newTag = newList[newItemPosition]
        return oldTag.name == newTag.name
    }
}