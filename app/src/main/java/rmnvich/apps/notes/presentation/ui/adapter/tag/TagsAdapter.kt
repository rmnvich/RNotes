package rmnvich.apps.notes.presentation.ui.adapter.tag

import android.databinding.DataBindingUtil
import android.support.v7.util.DiffUtil
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import rmnvich.apps.notes.R
import rmnvich.apps.notes.databinding.ItemTagBinding
import rmnvich.apps.notes.domain.entity.Tag
import java.lang.IndexOutOfBoundsException
import java.util.*

class TagsAdapter : RecyclerView.Adapter<TagsAdapter.ViewHolder>() {

    interface OnClickTagListener {
        fun onClickDelete(tag: Tag)

        fun onClickApply(tagId: Int, tagName: String)
    }

    fun setClickListener(listener: OnClickTagListener) {
        mClickListener = listener
    }

    inline fun setOnTagClickListener(crossinline onClickDelete: (Tag) -> Unit,
                                     crossinline onClickApply: (Int, String) -> Unit) {
        setClickListener(object : OnClickTagListener {
            override fun onClickDelete(tag: Tag) {
                onClickDelete(tag)
            }

            override fun onClickApply(tagId: Int, tagName: String) {
                onClickApply(tagId, tagName)
            }
        })
    }

    private lateinit var mClickListener: OnClickTagListener
    private var mTagList: List<Tag> = LinkedList()

    fun setData(data: List<Tag>) {
        val diffUtilCallback = TagsDiffUtil(mTagList, data)
        val diffResult = DiffUtil.calculateDiff(diffUtilCallback)

        mTagList = data
        diffResult.dispatchUpdatesTo(this)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding: ItemTagBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.context),
                R.layout.item_tag, parent, false)
        return ViewHolder(binding)

    }

    override fun getItemCount(): Int {
        return mTagList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(mTagList[position])
    }

    inner class ViewHolder(private val binding: ItemTagBinding)
        : RecyclerView.ViewHolder(binding.root) {

        init {

            binding.ivDeleteTag.setOnClickListener {
                try {
                    mClickListener.onClickDelete(mTagList[adapterPosition])
                } catch (e: IndexOutOfBoundsException) { }
            }

            binding.ivEditTag.setOnClickListener {
                mClickListener.onClickApply(mTagList[adapterPosition].tagId,
                        binding.etTagText.text.toString())
            }
        }

        fun bind(tag: Tag) {
            binding.tag = tag
            binding.executePendingBindings()
        }
    }
}