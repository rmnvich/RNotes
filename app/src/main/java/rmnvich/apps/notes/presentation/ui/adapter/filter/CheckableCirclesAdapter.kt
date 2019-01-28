package rmnvich.apps.notes.presentation.ui.adapter.filter

import android.databinding.DataBindingUtil
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import rmnvich.apps.notes.R
import rmnvich.apps.notes.databinding.ItemCheckableColorBinding
import rmnvich.apps.notes.domain.entity.Color
import java.util.*

class CheckableCirclesAdapter : RecyclerView.Adapter<CheckableCirclesAdapter.ViewHolder>() {

    private var mColors: List<Color> = listOf(
            Color(-16777216, "Black"),
            Color(-0xbbcca, "Red"),
            Color(-0x16e19d, "Pink"),
            Color(-0xd36d, "Light pink"),
            Color(-0x63d850, "Purple"),
            Color(-0x98c549, "Deep purple"),
            Color(-0xc0ae4b, "Indigo"),
            Color(-0xde690d, "Blue"),
            Color(-0xfc560c, "Light blue"),
            Color(-0xff432c, "Cyan"),
            Color(-0xff6978, "Teal"),
            Color(-0xb350b0, "Green"),
            Color(-0x743cb6, "Light green"),
            Color(-0x3223c7, "Lime"),
            Color(-0x14c5, "Yellow"),
            Color(-0x3ef9, "Amber"),
            Color(-0x6800, "Orange"),
            Color(-0x86aab8, "Brown"),
            Color(-0x9f8275, "Blue gray"),
            Color(-0x616162, "Gray"))

    var mCheckedColors: MutableList<Color> = LinkedList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding: ItemCheckableColorBinding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.context), R.layout.item_checkable_color,
                parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(mColors[position])
    }

    override fun getItemCount(): Int {
        return mColors.size
    }

    inner class ViewHolder(private val binding: ItemCheckableColorBinding)
        : RecyclerView.ViewHolder(binding.root) {

        init {
            binding.root.setOnClickListener {
                binding.chbCircleColor.isChecked = !binding.chbCircleColor.isChecked

                if (binding.chbCircleColor.isChecked) {
                    mCheckedColors.add(mColors[adapterPosition])
                } else mCheckedColors.remove(mColors[adapterPosition])
            }
        }

        fun bind(color: Color) {
            binding.color = color
            binding.executePendingBindings()
        }
    }
}