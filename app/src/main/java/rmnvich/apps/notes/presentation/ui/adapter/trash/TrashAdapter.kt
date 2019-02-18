package rmnvich.apps.notes.presentation.ui.adapter.trash

import android.databinding.DataBindingUtil
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

class TrashAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

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

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == 0) {
            val binding: ItemSimpleNoteBinding = DataBindingUtil.inflate(
                    LayoutInflater.from(parent.context),
                    R.layout.item_simple_note, parent, false
            )
            UnlockedNoteViewHolder(binding)
        } else {
            val binding: ItemSimpleLockedNoteBinding = DataBindingUtil.inflate(
                    LayoutInflater.from(parent.context),
                    R.layout.item_simple_locked_note, parent, false
            )
            LockedNoteViewHolder(binding)
        }
    }

    override fun getItemCount(): Int {
        return mNoteList.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is UnlockedNoteViewHolder -> holder.bind(mNoteList[position])
            is LockedNoteViewHolder -> holder.bind(mNoteList[position])
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (!mNoteList[position].isLocked) 0 else 1
    }

    //TODO: fix viewHolders!

    inner class LockedNoteViewHolder(private val binding: ItemSimpleLockedNoteBinding) : RecyclerView.ViewHolder(binding.root) {

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
            val resources = binding.root.context.resources

            val color = if (!mSelectedNotes.contains(note)) {
                mSelectedNotes.add(note)
                resources.getColor(R.color.colorAccent)
            } else {
                mSelectedNotes.remove(note)
                resources.getColor(R.color.colorItemBackground)
            }

            (binding.root as CardView).setCardBackgroundColor(color)
            mSelectListener.onNoteSelected(mSelectedNotes.size)
        }

        fun bind(note: Note) {
            binding.note = note

            //TODO: fix this
            if (!mSelectedNotes.contains(note))
                binding.cardViewSimpleNote.setCardBackgroundColor(binding.root.context
                        .resources.getColor(R.color.colorItemBackground))
            else binding.cardViewSimpleNote.setCardBackgroundColor(binding.root.context
                    .resources.getColor(R.color.colorAccent))

            binding.executePendingBindings()
        }
    }

    inner class UnlockedNoteViewHolder(private val binding: ItemSimpleNoteBinding) : RecyclerView.ViewHolder(binding.root) {

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
            val resources = binding.root.context.resources

            val color = if (!mSelectedNotes.contains(note)) {
                mSelectedNotes.add(note)
                resources.getColor(R.color.colorAccent)
            } else {
                mSelectedNotes.remove(note)
                resources.getColor(R.color.colorItemBackground)
            }

            (binding.root as CardView).setCardBackgroundColor(color)
            mSelectListener.onNoteSelected(mSelectedNotes.size)
        }

        fun bind(note: Note) {
            binding.note = note

            //TODO: fix this
            if (!mSelectedNotes.contains(note))
                binding.cardViewSimpleNote.setCardBackgroundColor(binding.root.context
                        .resources.getColor(R.color.colorItemBackground))
            else binding.cardViewSimpleNote.setCardBackgroundColor(binding.root.context
                    .resources.getColor(R.color.colorAccent))

            binding.executePendingBindings()
        }
    }
}