package rmnvich.apps.notes.presentation.ui.adapter.dashboard

import android.databinding.DataBindingUtil
import android.support.v7.util.DiffUtil
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AlphaAnimation
import rmnvich.apps.notes.R
import rmnvich.apps.notes.databinding.ItemNoteBinding
import rmnvich.apps.notes.domain.entity.Note
import java.util.*

class NotesAdapter : RecyclerView.Adapter<NotesAdapter.ViewHolder>() {

    private var mNoteList: List<Note> = LinkedList()

    fun setData(data: List<Note>) {
        val diffUtilCallback = NotesDiffutil(mNoteList, data)
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
        setFadeAnimation(holder.itemView)
    }

    private fun setFadeAnimation(view: View) {
        val anim = AlphaAnimation(0.0f, 1.0f)
        anim.duration = 450
        view.startAnimation(anim)
    }

    inner class ViewHolder(
        private val binding: ItemNoteBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        init {
            binding.root.setOnClickListener {}
        }

        fun bind(note: Note) {
            binding.note = note
            binding.executePendingBindings()
        }
    }
}