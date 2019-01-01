package rmnvich.apps.notes.presentation.ui.adapter.dashboard

import android.databinding.DataBindingUtil
import android.support.v7.util.DiffUtil
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.CompoundButton
import com.like.LikeButton
import com.like.OnLikeListener
import rmnvich.apps.notes.R
import rmnvich.apps.notes.databinding.ItemNoteBinding
import rmnvich.apps.notes.domain.entity.Note
import java.util.*

class NotesAdapter : RecyclerView.Adapter<NotesAdapter.ViewHolder>() {

    private lateinit var mClickListener: OnClickNoteListener
    private var mNoteList: List<Note> = LinkedList()

    fun setData(data: List<Note>) {
        val diffUtilCallback = NotesDiffUtil(mNoteList, data)
        val diffResult = DiffUtil.calculateDiff(diffUtilCallback)

        mNoteList = data
        diffResult.dispatchUpdatesTo(this)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding: ItemNoteBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.context),
                R.layout.item_note, parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return mNoteList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(mNoteList[position])
    }

    interface OnClickNoteListener {
        fun onClickNote(noteId: Int)

        fun onClickFavorite(noteId: Int, isFavorite: Boolean)
    }

    fun setClickListener(listener: OnClickNoteListener) {
        mClickListener = listener
    }

    inline fun setOnItemClickListener(crossinline onClickNote: (Int) -> Unit,
                                      crossinline onClickFavorite: (Int, Boolean) -> Unit) {
        setClickListener(object : OnClickNoteListener {
            override fun onClickNote(noteId: Int) {
                onClickNote(noteId)
            }

            override fun onClickFavorite(noteId: Int, isFavorite: Boolean) {
                onClickFavorite(noteId, isFavorite)
            }
        })
    }

    inner class ViewHolder(private val binding: ItemNoteBinding)
        : RecyclerView.ViewHolder(binding.root) {

        init {
            binding.root.setOnClickListener {
                mClickListener.onClickNote(mNoteList[adapterPosition].noteId)
            }

            binding.noteButtonStar.setOnLikeListener(object : OnLikeListener {
                override fun liked(p0: LikeButton?) =
                        mClickListener.onClickFavorite(mNoteList[adapterPosition].noteId, true)

                override fun unLiked(p0: LikeButton?) =
                        mClickListener.onClickFavorite(mNoteList[adapterPosition].noteId, false)
            })
        }

        fun bind(note: Note) {
            binding.note = note
            binding.executePendingBindings()
        }
    }
}