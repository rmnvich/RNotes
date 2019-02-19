package rmnvich.apps.notes.presentation.ui.adapter.trash

import android.databinding.DataBindingUtil
import android.databinding.ViewDataBinding
import android.support.v4.content.ContextCompat
import android.support.v7.util.DiffUtil
import android.support.v7.widget.CardView
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import rmnvich.apps.notes.R
import rmnvich.apps.notes.databinding.ItemSimpleLockedNoteBinding
import rmnvich.apps.notes.databinding.ItemSimpleNoteBinding
import rmnvich.apps.notes.domain.entity.Note
import java.util.*

class TrashAdapter : RecyclerView.Adapter<TrashAdapter.ViewHolder>() {

    interface OnClickTrashNoteListener {
        fun onClickNote(note: Note)
    }

    interface OnSelectedTrashNoteListener {
        fun onNoteSelected(selectedNotesSize: Int)
    }

    fun setClickListener(listener: OnClickTrashNoteListener) {
        mClickListener = listener
    }

    fun setSelectListener(listener: OnSelectedTrashNoteListener) {
        mSelectListener = listener
    }

    inline fun setOnItemClickListener(crossinline onClickNote: (Note) -> Unit) {
        setClickListener(object : OnClickTrashNoteListener {
            override fun onClickNote(note: Note) {
                onClickNote(note)
            }
        })
    }

    inline fun setOnItemSelectListener(crossinline onNoteSelected: (Int) -> Unit) {
        setSelectListener(object : OnSelectedTrashNoteListener {
            override fun onNoteSelected(selectedNotesSize: Int) {
                onNoteSelected(selectedNotesSize)
            }
        })
    }

    private lateinit var mClickListener: OnClickTrashNoteListener
    private lateinit var mSelectListener: OnSelectedTrashNoteListener

    var mNoteList: List<Note> = LinkedList()
    var mSelectedNotes: MutableList<Note> = LinkedList()

    fun setData(data: List<Note>) {
        val diffUtilCallback = TrashDiffUtil(mNoteList, data)
        val diffResult = DiffUtil.calculateDiff(diffUtilCallback)

        mNoteList = data
        diffResult.dispatchUpdatesTo(this)
    }

    fun unselectAllNotes() {
        clearSelectedNotes()
        notifyDataSetChanged()
    }

    fun clearSelectedNotes() {
        mSelectedNotes.removeAll(mSelectedNotes)
        mSelectListener.onNoteSelected(0)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrashAdapter.ViewHolder {
        return if (viewType == 0) {
            val binding: ItemSimpleNoteBinding = DataBindingUtil.inflate(
                    LayoutInflater.from(parent.context),
                    R.layout.item_simple_note, parent, false
            )
            ViewHolder(binding)
        } else {
            val binding: ItemSimpleLockedNoteBinding = DataBindingUtil.inflate(
                    LayoutInflater.from(parent.context),
                    R.layout.item_simple_locked_note, parent, false
            )
            ViewHolder(binding)
        }
    }

    override fun getItemCount(): Int {
        return mNoteList.size
    }

    override fun onBindViewHolder(holder: TrashAdapter.ViewHolder, position: Int) {
        holder.bind(mNoteList[position])
    }

    override fun getItemViewType(position: Int): Int {
        return if (!mNoteList[position].isLocked) 0 else 1
    }

    inner class ViewHolder(private val binding: ViewDataBinding) : RecyclerView.ViewHolder(binding.root) {

        init {
            binding.root.setOnClickListener {
                val note = mNoteList[adapterPosition]

                if (mSelectedNotes.size == 0)
                    mClickListener.onClickNote(note)
                else selectNote(note)
            }

            binding.root.setOnLongClickListener {
                val note = mNoteList[adapterPosition]

                selectNote(note)
                true
            }
        }

        private fun selectNote(note: Note) {
            val color = if (!mSelectedNotes.contains(note)) {
                mSelectedNotes.add(note)
                ContextCompat.getColor(binding.root.context, R.color.colorAccent)
            } else {
                mSelectedNotes.remove(note)
                ContextCompat.getColor(binding.root.context, R.color.colorItemBackground)
            }

            (binding.root as CardView).setCardBackgroundColor(color)
            mSelectListener.onNoteSelected(mSelectedNotes.size)
        }

        fun bind(note: Note) {
            val color = if (!mSelectedNotes.contains(note)) {
                ContextCompat.getColor(binding.root.context, R.color.colorItemBackground)
            } else ContextCompat.getColor(binding.root.context, R.color.colorAccent)

            try {
                (binding as ItemSimpleNoteBinding).note = note
                binding.cardViewSimpleNote.setCardBackgroundColor(color)
            } catch (e: ClassCastException) {
                (binding as ItemSimpleLockedNoteBinding).note = note
                binding.cardViewSimpleNote.setCardBackgroundColor(color)
            } finally {
                binding.executePendingBindings()
            }
        }
    }
}