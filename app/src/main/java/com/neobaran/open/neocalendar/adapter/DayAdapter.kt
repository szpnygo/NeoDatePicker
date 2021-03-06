package com.neobaran.open.neocalendar.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.neobaran.open.neocalendar.R
import com.neobaran.open.neocalendar.bean.DayBean
import kotlinx.android.synthetic.main.item_day.view.*

class DayAdapter(private val context: Context) :
    ListAdapter<DayBean, DayAdapter.ItemViewHolder>(DayDiffCallback()) {

    private var clickItemListener: ((day: DayBean) -> Unit)? = null

    fun setOnClickItemListener(l: ((day: DayBean) -> Unit)) {
        clickItemListener = l
    }

    private var selectedDay: DayBean? = null

    fun setSelectedDay(d: DayBean?) {
        selectedDay = d
    }

    private var lastPosition = 0

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        return ItemViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_day,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val day = getItem(position)
        with(holder.itemView) {
            setOnClickListener {
                clickItemListener?.invoke(day)
                notifyItemChanged(position)
                notifyItemChanged(lastPosition)
            }
            date_day.text = day.showData
            if (day.type == 0) {
                date_day.setTextColor(ContextCompat.getColor(context, R.color.text_main))
            } else {
                date_day.setTextColor(ContextCompat.getColor(context, R.color.text_disable))
            }

            if (day == selectedDay) {
                lastPosition = position
                item_day.setBackgroundResource(R.drawable.ic_selected_bg)
                date_day.setTextColor(ContextCompat.getColor(context, R.color.selected_text))
            } else {
                item_day.setBackgroundResource(0)
            }
        }
    }

    class ItemViewHolder(view: View) : RecyclerView.ViewHolder(view)

}