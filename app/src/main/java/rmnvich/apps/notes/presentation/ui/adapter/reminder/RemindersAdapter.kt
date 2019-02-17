package rmnvich.apps.notes.presentation.ui.adapter.reminder

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
import rmnvich.apps.notes.R
import rmnvich.apps.notes.databinding.ItemReminderBinding
import rmnvich.apps.notes.domain.entity.Reminder
import java.util.*

class RemindersAdapter : RecyclerSwipeAdapter<RemindersAdapter.ViewHolder>() {

    interface OnClickReminderListener {
        fun onClickReminder(reminderId: Int)

        fun onClickComplete(reminderId: Int, isCompleted: Boolean)

        fun onClickPin(reminderId: Int, isPinned: Boolean)

        fun onClickDelete(reminder: Reminder)
    }

    fun setClickListener(listener: OnClickReminderListener) {
        mClickListener = listener
    }

    inline fun setOnItemClickListener(
            crossinline onClickReminder: (Int) -> Unit,
            crossinline onClickComplete: (Int, Boolean) -> Unit,
            crossinline onClickPin: (Int, Boolean) -> Unit,
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

    override fun getSwipeLayoutResourceId(position: Int): Int {
        return R.id.swipe_layout
    }

    inner class ViewHolder(private val binding: ItemReminderBinding) :
            RecyclerView.ViewHolder(binding.root) {

        init {
            binding.reminderLayout.setOnClickListener {
                mClickListener.onClickReminder(mReminderList[adapterPosition].id)
            }

            binding.btnDeleteReminder.setOnClickListener { deleteReminder() }

            binding.btnDoneReminder.setOnClickListener { completeReminder() }

            binding.btnPinReminder.setOnClickListener { pinReminder() }
        }

        private fun deleteReminder() {
            vibrate()
            binding.swipeLayout.close()

            Handler().postDelayed({
                try {
                    mClickListener.onClickDelete(mReminderList[adapterPosition])
                } catch (ignore: IndexOutOfBoundsException) {
                }
            }, 400)
        }

        private fun pinReminder() {
            vibrate()
            binding.swipeLayout.close()

            Handler().postDelayed({
                try {
                    mClickListener.onClickPin(
                            mReminderList[adapterPosition].id,
                            !mReminderList[adapterPosition].isPinned
                    )
                } catch (ignore: IndexOutOfBoundsException) {
                }
            }, 400)
        }

        private fun completeReminder() {
            vibrate()
            binding.swipeLayout.close()

            Handler().postDelayed({
                try {
                    mClickListener.onClickComplete(
                            mReminderList[adapterPosition].id,
                            !mReminderList[adapterPosition].isCompleted
                    )
                } catch (ignore: IndexOutOfBoundsException) {
                }
            }, 400)
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