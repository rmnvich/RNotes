package rmnvich.apps.notes.presentation.ui.adapter.tag

import android.content.Context
import android.databinding.DataBindingUtil
import android.support.v7.util.DiffUtil
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import rmnvich.apps.notes.R
import rmnvich.apps.notes.databinding.ItemTagBinding
import rmnvich.apps.notes.domain.entity.Tag
import java.util.*


class TagsAdapter : RecyclerView.Adapter<TagsAdapter.ViewHolder>() {

    interface OnClickTagListener {
        fun onClickApply(tagId: Int, tagName: String)
    }

    fun setClickListener(listener: OnClickTagListener) {
        mClickListener = listener
    }

    inline fun setOnTagClickListener(crossinline onClickApply: (Int, String) -> Unit) {
        setClickListener(object : OnClickTagListener {
            override fun onClickApply(tagId: Int, tagName: String) {
                onClickApply(tagId, tagName)
            }
        })
    }

    private lateinit var mClickListener: OnClickTagListener
    var mTagList: List<Tag> = LinkedList()

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
            val inputMethodManager = binding.root.context.getSystemService(
                    Context.INPUT_METHOD_SERVICE) as InputMethodManager

            binding.ivEditTag.setOnClickListener {
                if (!binding.etTagText.hasFocus()) {
                    binding.etTagText.isEnabled = true
                    binding.etTagText.requestFocus()
                    inputMethodManager.showSoftInput(binding.etTagText, InputMethodManager.SHOW_IMPLICIT)
                } else {
                    binding.etTagText.isEnabled = false
                    binding.etTagText.clearFocus()
                    inputMethodManager.hideSoftInputFromWindow(binding.etTagText.windowToken, 0)

                    mClickListener.onClickApply(mTagList[adapterPosition].id,
                            binding.etTagText.text.toString())
                }
            }

            binding.etTagText.setOnFocusChangeListener { _, isFocused ->
                if (isFocused) {
                    binding.etTagText.setSelection(binding.etTagText.text.toString().length)
                    if (adapterPosition != 0)
                        binding.topDivider.visibility = VISIBLE
                    binding.bottomDivider.visibility = VISIBLE

                    binding.ivEditTag.setImageResource(R.drawable.ic_action_check_inverted)
                    binding.ivDeleteTag.setImageResource(R.drawable.ic_action_label_empty_inverted)
                } else {
                    binding.etTagText.isEnabled = false
                    binding.topDivider.visibility = INVISIBLE
                    binding.bottomDivider.visibility = INVISIBLE

                    binding.ivEditTag.setImageResource(R.drawable.ic_action_create_inverted)
                    binding.ivDeleteTag.setImageResource(R.drawable.ic_action_label_inverted)

                    mClickListener.onClickApply(mTagList[adapterPosition].id,
                            binding.etTagText.text.toString())
                }
            }
        }

        fun bind(tag: Tag) {
            binding.tag = tag
            binding.executePendingBindings()
        }
    }
}