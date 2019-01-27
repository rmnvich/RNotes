package rmnvich.apps.notes.presentation.ui.adapter.dashboard

import android.graphics.Color
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.jaredrummler.android.colorpicker.ColorPickerDialog.MATERIAL_COLORS
import kotlinx.android.synthetic.main.item_checkable_color.view.*
import rmnvich.apps.notes.R
import java.util.*

class CheckableCirclesAdapter : RecyclerView.Adapter<CheckableCirclesAdapter.ViewHolder>() {

    private var mColors: MutableList<Int> = LinkedList()

    init {
        mColors.add(Color.BLACK)
        for (i in 0 until MATERIAL_COLORS.size) {
            mColors.add(MATERIAL_COLORS[i])
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(
                R.layout.item_checkable_color, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.itemView.filter_circle_color.circleColor = mColors[position]
    }

    override fun getItemCount(): Int {
        return mColors.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        init {
            itemView.setOnClickListener {
                itemView.chb_circle_color.isChecked = !itemView.chb_circle_color.isChecked
            }
        }
    }
}