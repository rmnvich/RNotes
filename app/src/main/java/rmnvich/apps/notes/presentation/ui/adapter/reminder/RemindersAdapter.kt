package rmnvich.apps.notes.presentation.ui.adapter.reminder

import android.content.Context
import android.databinding.DataBindingUtil
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.support.v7.util.DiffUtil
import android.support.v7.widget.PopupMenu
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import org.jetbrains.anko.sdk27.coroutines.onClick
import rmnvich.apps.notes.R
import rmnvich.apps.notes.databinding.ItemReminderBinding
import rmnvich.apps.notes.domain.entity.Reminder
import java.util.*

class RemindersAdapter : RecyclerView.Adapter<RemindersAdapter.ViewHolder>() {

    interface OnClickReminderListener {
        fun onClickReminder(reminderId: Int)

        fun onClickComplete(reminderId: Int, isCompleted: Boolean)

        fun onClickPin(reminderId: Int, isPinned: Boolean)

        fun onClickShare(reminderId: Int)

        fun onClickDelete(reminder: Reminder)
    }

    fun setClickListener(listener: OnClickReminderListener) {
        mClickListener = listener
    }

    inline fun setOnItemClickListener(
            crossinline onClickReminder: (Int) -> Unit,
            crossinline onClickComplete: (Int, Boolean) -> Unit,
            crossinline onClickPin: (Int, Boolean) -> Unit,
            crossinline onClickShare: (Int) -> Unit,
            crossinline onClickDelete: (Reminder) -> Unit
    ) {
        setClickListener(object : OnClickReminderListener {
            override fun onClickReminder(reminderId: Int) {
                onClickReminder(reminderId)
            }

            override fun onClickComplete(reminderId: Int, isCompleted: Boolean) {
                onClickComplete(reminderId, isCompleted)
            }

            override fun onClickPin(reminderId: Int, isPinned: Boolean) {
                onClickPin(reminderId, isPinned)
            }

            override fun onClickShare(reminderId: Int) {
                onClickShare(reminderId)
            }

            override fun onClickDelete(reminder: Reminder) {
                onClickDelete(reminder)
            }

        })
    }

    private lateinit var mClickListener: OnClickReminderListener
    private var mReminderList: List<Reminder> = LinkedList()

    fun setData(data: List<Reminder>) {
        val diffUtilCallback = RemindersDiffUtil(mReminderList, data)
        val diffResult = DiffUtil.calculateDiff(diffUtilCallback)

        mReminderList = data
        diffResult.dispatchUpdatesTo(this)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding: ItemReminderBinding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.item_reminder, parent, false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(mReminderList[position])
    }

    override fun getItemCount(): Int {
        return mReminderList.size
    }

    inner class ViewHolder(private val binding: ItemReminderBinding) :
            RecyclerView.ViewHolder(binding.root) {

        init {
            binding.reminderLayout.onClick {
                mClickListener.onClickReminder(mReminderList[adapterPosition].id)
            }

            binding.btnActionMore.onClick {
                val popupMenu = PopupMenu(binding.root.context, binding.btnActionMore)
                popupMenu.inflate(R.menu.reminder_menu)

                val itemDone = popupMenu.menu.findItem(R.id.menu_complete_reminder)
                val itemPin = popupMenu.menu.findItem(R.id.menu_pin_reminder)

                itemDone.setTitle(getTitleForMenuItemDone())
                itemPin.setTitle(getTitleForMenuItemPin())

                popupMenu.setOnMenuItemClickListener { item ->
                    when (item.itemId) {
                        R.id.menu_complete_reminder -> {
                            vibrate()
                            mClickListener.onClickComplete(
                                    mReminderList[adapterPosition].id,
                                    !mReminderList[adapterPosition].isCompleted
                            )
                            true
                        }
                        R.id.menu_pin_reminder -> {
                            vibrate()
                            mClickListener.onClickPin(
                                    mReminderList[adapterPosition].id,
                                    !mReminderList[adapterPosition].isPinned
                            )
                            true
                        }
                        R.id.menu_delete_reminder -> {
                            vibrate()
                            mClickListener.onClickDelete(mReminderList[adapterPosition])
                            true
                        }
                        R.id.menu_share_reminder -> {
                            mClickListener.onClickShare(mReminderList[adapterPosition].id)
                            true
                        }
                        else -> false
                    }
                }
                popupMenu.show()
            }
        }

        private fun getTitleForMenuItemDone(): Int {
            return if (mReminderList[adapterPosition].isCompleted)
                R.string.undone
            else R.string.done
        }

        private fun getTitleForMenuItemPin(): Int {
            return if (mReminderList[adapterPosition].isPinned)
                R.string.unpin
            else R.string.pin
        }

        fun bind(reminder: Reminder) {
            binding.reminder = reminder
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