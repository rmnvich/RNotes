package rmnvich.apps.notes.presentation.ui.adapter.note

import android.content.Context
import android.databinding.DataBindingUtil
import android.os.Build
import android.os.Handler
import android.os.VibrationEffect
import android.os.Vibrator
import android.support.v7.util.DiffUtil
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.daimajia.swipe.adapters.RecyclerSwipeAdapter
import com.like.LikeButton
import com.like.OnLikeListener
import rmnvich.apps.notes.R
import rmnvich.apps.notes.databinding.ItemLockedNoteBinding
import rmnvich.apps.notes.databinding.ItemNoteBinding
import rmnvich.apps.notes.domain.entity.NoteBundle
import rmnvich.apps.notes.domain.entity.NoteWithTag
import java.util.*


class NotesAdapter : RecyclerSwipeAdapter<RecyclerView.ViewHolder>() {

    interface OnClickNoteListener {
        fun onClickNote(noteBundle: NoteBundle)

        fun onClickFavorite(noteId: Int, isFavorite: Boolean)

        fun onClickDelete(noteId: Int, position: Int)
    }

    fun setClickListener(listener: OnClickNoteListener) {
        mClickListener = listener
    }

    inline fun setOnItemClickListener(
            crossinline onClickNote: (NoteBundle) -> Unit,
            crossinline onClickDelete: (Int, Int) -> Unit,
            crossinline onClickFavorite: (Int, Boolean) -> Unit
    ) {
        setClickListener(object : OnClickNoteListener {
            override fun onClickDelete(noteId: Int, position: Int) {
                onClickDelete(noteId, position)
            }

            override fun onClickNote(noteBundle: NoteBundle) {
                onClickNote(noteBundle)
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

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == 0) {
            val binding: ItemNoteBinding = DataBindingUtil.inflate(
                    LayoutInflater.from(parent.context),
                    R.layout.item_note, parent, false
            )
            UnlockedNoteViewHolder(binding)
        } else {
            val binding: ItemLockedNoteBinding = DataBindingUtil.inflate(
                    LayoutInflater.from(parent.context),
                    R.layout.item_locked_note, parent, false
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

    override fun getSwipeLayoutResourceId(position: Int): Int {
        return R.id.swipe_layout
    }

    override fun getItemViewType(position: Int): Int {
        return if (!mNoteList[position].noteIsLocked) 0 else 1
    }

    //TODO: fix view holders

    inner class UnlockedNoteViewHolder(private val binding: ItemNoteBinding) : RecyclerView.ViewHolder(binding.root) {

        init {
            binding.noteLayout.setOnClickListener {
                mClickListener.onClickNote(NoteBundle(mNoteList[adapterPosition].noteId,
                        mNoteList[adapterPosition].noteIsLocked))
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

        fun bind(note: NoteWithTag) {
            binding.note = note
            binding.executePendingBindings()
        }

        private fun vibrate() {
            val vibrator = binding.root.context?.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                vibrator.vibrate(VibrationEffect.createOneShot(50, VibrationEffect.DEFAULT_AMPLITUDE))
            } else vibrator.vibrate(50)
        }
    }

    inner class LockedNoteViewHolder(private val binding: ItemLockedNoteBinding) : RecyclerView.ViewHolder(binding.root) {

        init {
            binding.noteLayout.setOnClickListener {
                mClickListener.onClickNote(NoteBundle(mNoteList[adapterPosition].noteId,
                        mNoteList[adapterPosition].noteIsLocked))
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

        fun bind(note: NoteWithTag) {
            binding.note = note
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