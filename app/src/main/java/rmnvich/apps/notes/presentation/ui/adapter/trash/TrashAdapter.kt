package rmnvich.apps.notes.presentation.ui.adapter.trash

import android.databinding.DataBindingUtil
import android.support.v7.util.DiffUtil
import android.support.v7.widget.CardView
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.bumptech.glide.Glide
import rmnvich.apps.notes.R
import rmnvich.apps.notes.databinding.ItemSimpleNoteBinding
import rmnvich.apps.notes.domain.entity.Note
import java.io.File
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
        val binding: ItemSimpleNoteBinding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.item_simple_note, parent, false
        )
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return mNoteList.size
    }

    override fun onBindViewHolder(holder: TrashAdapter.ViewHolder, position: Int) {
        holder.bind(mNoteList[position])
        (holder.itemView as CardView).setCardBackgroundColor(
                holder.itemView.context.resources.getColor(R.color.colorItemBackground)
        )
    }

    inner class ViewHolder(private val binding: ItemSimpleNoteBinding) : RecyclerView.ViewHolder(binding.root) {

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

            Glide.with(binding.root)
                    .load(File(note.imagePath))
                    .into(binding.ivNoteImage)

            binding.executePendingBindings()
        }
    }
}