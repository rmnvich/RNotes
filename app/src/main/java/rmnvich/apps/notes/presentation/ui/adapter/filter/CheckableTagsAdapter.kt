package rmnvich.apps.notes.presentation.ui.adapter.filter

import android.databinding.DataBindingUtil
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import rmnvich.apps.notes.R
import rmnvich.apps.notes.databinding.ItemSimpleCheckableTagBinding
import rmnvich.apps.notes.domain.entity.Tag
import java.util.*

class CheckableTagsAdapter : RecyclerView.Adapter<CheckableTagsAdapter.ViewHolder>() {

    private var mTags: List<Tag> = LinkedList()

    var mCheckedTags: MutableList<Tag> = LinkedList()

    fun setData(data: List<Tag>) {
        mTags = data
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding: ItemSimpleCheckableTagBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.context),
                R.layout.item_simple_checkable_tag, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(mTags[position])
    }

    override fun getItemCount(): Int {
        return mTags.size
    }

    inner class ViewHolder(private val binding: ItemSimpleCheckableTagBinding)
        : RecyclerView.ViewHolder(binding.root) {

        init {
            binding.root.setOnClickListener {
                binding.chbTag.isChecked = !binding.chbTag.isChecked

                if (binding.chbTag.isChecked) {
                    mCheckedTags.add(mTags[adapterPosition])
                } else mCheckedTags.remove(mTags[adapterPosition])
            }
        }

        fun bind(tag: Tag) {
            binding.tag = tag
            binding.executePendingBindings()
        }
    }
}