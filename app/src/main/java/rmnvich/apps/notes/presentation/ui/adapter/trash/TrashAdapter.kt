package rmnvich.apps.notes.presentation.ui.adapter.trash

import android.databinding.DataBindingUtil
import android.support.v7.util.DiffUtil
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import rmnvich.apps.notes.R
import rmnvich.apps.notes.databinding.ItemSimpleNoteBinding
import rmnvich.apps.notes.domain.entity.Note
import rmnvich.apps.notes.presentation.ui.adapter.dashboard.NotesAdapter
import java.util.*

class TrashAdapter : RecyclerView.Adapter<TrashAdapter.ViewHolder>() {

    interface OnClickTrashNoteListener {
        fun onClickNote(note: Note)
    }

    fun setClickListener(listener: OnClickTrashNoteListener) {
        mClickListener = listener
    }

    inline fun setOnItemClickListener(crossinline onClickNote: (Note) -> Unit) {
        setClickListener(object : OnClickTrashNoteListener {
            override fun onClickNote(note: Note) {
                onClickNote(note)
            }
        })
    }

    private lateinit var mClickListener: OnClickTrashNoteListener

    var mNoteList: List<Note> = LinkedList()

    fun setData(data: List<Note>) {
        val diffUtilCallback = TrashDiffUtil(mNoteList, data)
        val diffResult = DiffUtil.calculateDiff(diffUtilCallback)

        mNoteList = data
        diffResult.dispatchUpdatesTo(this)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrashAdapter.ViewHolder {
        val binding: ItemSimpleNoteBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.context),
                R.layout.item_simple_note, parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return mNoteList.size
    }

    override fun onBindViewHolder(holder: TrashAdapter.ViewHolder, position: Int) {
        holder.bind(mNoteList[position])
    }

    inner class ViewHolder(private val binding: ItemSimpleNoteBinding)
        : RecyclerView.ViewHolder(binding.root) {

        init {
            binding.root.setOnClickListener {
                mClickListener.onClickNote(mNoteList[adapterPosition])
            }
        }

        fun bind(note: Note) {
            binding.note = note
            binding.executePendingBindings()
        }
    }
}