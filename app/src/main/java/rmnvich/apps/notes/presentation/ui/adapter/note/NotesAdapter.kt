package rmnvich.apps.notes.presentation.ui.adapter.note

import android.content.Context
import android.databinding.DataBindingUtil
import android.graphics.drawable.GradientDrawable
import android.os.Build
import android.os.Handler
import android.os.VibrationEffect
import android.os.Vibrator
import android.support.v7.util.DiffUtil
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.daimajia.swipe.adapters.RecyclerSwipeAdapter
import com.like.LikeButton
import com.like.OnLikeListener
import rmnvich.apps.notes.R
import rmnvich.apps.notes.databinding.ItemNoteBinding
import rmnvich.apps.notes.domain.entity.NoteWithTag
import java.io.File
import java.util.*


class NotesAdapter : RecyclerSwipeAdapter<NotesAdapter.ViewHolder>() {

    interface OnClickNoteListener {
        fun onClickNote(noteId: Int)

        fun onClickFavorite(noteId: Int, isFavorite: Boolean)

        fun onClickDelete(noteId: Int, position: Int)
    }

    fun setClickListener(listener: OnClickNoteListener) {
        mClickListener = listener
    }

    inline fun setOnItemClickListener(
            crossinline onClickNote: (Int) -> Unit,
            crossinline onClickDelete: (Int, Int) -> Unit,
            crossinline onClickFavorite: (Int, Boolean) -> Unit
    ) {
        setClickListener(object : OnClickNoteListener {
            override fun onClickDelete(noteId: Int, position: Int) {
                onClickDelete(noteId, position)
            }

            override fun onClickNote(noteId: Int) {
                onClickNote(noteId)
            }

            override fun onClickFavorite(noteId: Int, isFavorite: Boolean) {
                onClickFavorite(noteId, isFavorite)
            }
        })
    }

    private lateinit var mClickListener: OnClickNoteListener
    private var mNoteList: List<NoteWithTag> = LinkedList()

    fun setData(data: List<NoteWithTag>) {
        val diffUtilCallback = NotesDiffUtil(mNoteList, data)
        val diffResult = DiffUtil.calculateDiff(diffUtilCallback)

        mNoteList = data
        diffResult.dispatchUpdatesTo(this)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding: ItemNoteBinding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.item_note, parent, false
        )
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return mNoteList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(mNoteList[position])
    }

    override fun getSwipeLayoutResourceId(position: Int): Int {
        return R.id.swipe_layout
    }

    inner class ViewHolder(private val binding: ItemNoteBinding) : RecyclerView.ViewHolder(binding.root) {

        init {
            binding.noteLayout.setOnClickListener {
                mClickListener.onClickNote(mNoteList[adapterPosition].noteId)
            }

            binding.noteDeleteButton.setOnClickListener {
                vibrate()
                binding.swipeLayout.close()

                Handler().postDelayed({
                    try {
                        mClickListener.onClickDelete(
                                mNoteList[adapterPosition].noteId,
                                adapterPosition
                        )
                    } catch (ignore: IndexOutOfBoundsException) {
                    }
                }, 400)
            }

            binding.noteButtonStar.setOnLikeListener(object : OnLikeListener {
                override fun liked(p0: LikeButton?) =
                        mClickListener.onClickFavorite(mNoteList[adapterPosition].noteId, true)

                override fun unLiked(p0: LikeButton?) =
                        mClickListener.onClickFavorite(mNoteList[adapterPosition].noteId, false)
            })
        }

        //TODO: databinding
        fun bind(note: NoteWithTag) {
            binding.note = note

            Glide.with(binding.root)
                    .load(File(note.noteImagePath))
                    .into(binding.ivNoteImage)

            val drawable = binding.noteColorView.background as GradientDrawable
            drawable.setColor(note.noteColor)
            binding.noteColorView.background = drawable

            binding.executePendingBindings()
        }

        private fun vibrate() {
            val vibrator = binding.root.context?.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                vibrator.vibrate(VibrationEffect.createOneShot(50, VibrationEffect.DEFAULT_AMPLITUDE))
            } else vibrator.vibrate(50)
        }
    }
}