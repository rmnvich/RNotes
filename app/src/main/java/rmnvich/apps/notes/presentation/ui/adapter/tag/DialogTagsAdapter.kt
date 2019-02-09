package rmnvich.apps.notes.presentation.ui.adapter.tag

import android.databinding.DataBindingUtil
import android.support.v7.util.DiffUtil
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import rmnvich.apps.notes.R
import rmnvich.apps.notes.databinding.ItemSimpleTagBinding
import rmnvich.apps.notes.domain.entity.Tag

class DialogTagsAdapter : RecyclerView.Adapter<DialogTagsAdapter.ViewHolder>(), Filterable {

    interface OnClickTagInDialogListener {
        fun onClickTag(tag: Tag)
    }

    fun setClickListener(listener: OnClickTagInDialogListener) {
        mClickListener = listener
    }

    inline fun setOnTagClickListener(crossinline onClickTag: (Tag) -> Unit) {
        setClickListener(object : OnClickTagInDialogListener {
            override fun onClickTag(tag: Tag) {
                onClickTag(tag)
            }
        })
    }

    private lateinit var mClickListener: OnClickTagInDialogListener

    private var mTagList: List<Tag> = ArrayList()
    private var mTagListFiltered: List<Tag> = ArrayList()

    fun setData(data: List<Tag>) {
        val diffUtilCallback = TagsDiffUtil(mTagListFiltered, data)
        val diffResult = DiffUtil.calculateDiff(diffUtilCallback)

        mTagList = data
        mTagListFiltered = data
        diffResult.dispatchUpdatesTo(this)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding: ItemSimpleTagBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.context),
                R.layout.item_simple_tag, parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return mTagListFiltered.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(mTagListFiltered[position])
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(charSequence: CharSequence): Filter.FilterResults {
                val charString = charSequence.toString()
                mTagListFiltered = if (charString.isEmpty()) {
                    mTagList
                } else {
                    val filteredList = ArrayList<Tag>()
                    for (row in mTagList) {
                        if (row.name.toLowerCase().contains(charString.toLowerCase())) {
                            filteredList.add(row)
                        }
                    }
                    filteredList
                }
                val filterResults = Filter.FilterResults()
                filterResults.values = mTagListFiltered
                return filterResults
            }

            @Suppress("UNCHECKED_CAST")
            override fun publishResults(charSequence: CharSequence, filterResults: Filter.FilterResults) {
                mTagListFiltered = filterResults.values as List<Tag>
                notifyDataSetChanged()
            }
        }
    }

    inner class ViewHolder(private val binding: ItemSimpleTagBinding)
        : RecyclerView.ViewHolder(binding.root) {

        init {
            binding.root.setOnClickListener {
                mClickListener.onClickTag(mTagListFiltered[adapterPosition])
            }
        }

        fun bind(tag: Tag) {
            binding.tag = tag
            binding.executePendingBindings()
        }
    }
}