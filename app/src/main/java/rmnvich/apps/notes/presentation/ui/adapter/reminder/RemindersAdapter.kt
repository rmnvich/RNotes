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

    private var mColors: List<Int> = listOf(
            -855682478,     //Material A200 Red
            -864268801,     //Material A200 Deep purple
            -866947586,     //Material A200 Blue
            -868170497,     //Material A200 Cyan
            -860684455,     //Material A200 Lime
            -855648448,     //Material A200 Amber
            -855675328      //Material A200 Orange
    )

    interface OnClickReminderListener {
        fun onClickReminder(reminderId: Int)

        fun onClickDone(reminderId: Int, isDone: Boolean)

        fun onClickDelete(reminder: Reminder)
    }

    fun setClickListener(listener: OnClickReminderListener) {
        mClickListener = listener
    }

    inline fun setOnItemClickListener(
            crossinline onClickReminder: (Int) -> Unit,
            crossinline onClickDone: (Int, Boolean) -> Unit,
            crossinline onClickDelete: (Reminder) -> Unit
    ) {
        setClickListener(object : OnClickReminderListener {
            override fun onClickReminder(reminderId: Int) {
                onClickReminder(reminderId)
            }

            override fun onClickDone(reminderId: Int, isDone: Boolean) {
                onClickDone(reminderId, isDone)
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

            binding.btnDeleteReminder.setOnClickListener {
                vibrate()
                binding.swipeLayout.close()

                Handler().postDelayed({
                    try {
                        mClickListener.onClickDelete(mReminderList[adapterPosition])
                    } catch (ignore: IndexOutOfBoundsException) {
                    }
                }, 400)
            }

            binding.btnDoneReminder.setOnClickListener {
                vibrate()
                binding.swipeLayout.close()

                Handler().postDelayed({
                    try {
                        mClickListener.onClickDone(
                                mReminderList[adapterPosition].id,
                                !mReminderList[adapterPosition].isCompleted
                        )
                    } catch (ignore: IndexOutOfBoundsException) {
                    }
                }, 400)
            }
        }

        fun bind(reminder: Reminder) {
            binding.reminder = reminder

            binding.tvReminderRepeat.text = binding.root.context.resources
                    .getStringArray(R.array.repeat_types)[reminder.repeatType]
            binding.reminderCircleView.circleColor = mColors[reminder.colorNumber]
            binding.tvReminderFirstLater.text = reminder.text.substring(0, 1)

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