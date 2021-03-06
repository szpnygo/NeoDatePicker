package com.neobaran.open.neocalendar.view

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.RelativeLayout
import com.neobaran.open.neocalendar.CalendarMonthUtil
import com.neobaran.open.neocalendar.R
import com.neobaran.open.neocalendar.adapter.DayAdapter
import com.neobaran.open.neocalendar.bean.DayBean
import kotlinx.android.synthetic.main.month_view.view.*

class MonthView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0, defStyleRes: Int = 0
) : RelativeLayout(context, attrs, defStyleAttr, defStyleRes) {

    private var clickItemListener: ((day: DayBean) -> Unit)? = null

    fun setOnClickItemListener(l: ((day: DayBean) -> Unit)) {
        clickItemListener = l
    }

    private val dayAdapter: DayAdapter = DayAdapter(context)

    fun setSelectedDay(d: DayBean?) {
        dayAdapter.setSelectedDay(d)
    }

    init {
        View.inflate(context, R.layout.month_view, this)
    }

    fun initData(year: Int, month: Int) {
        with(month_view_list) {
            layoutManager = androidx.recyclerview.widget.GridLayoutManager(context, 7)
            adapter = dayAdapter
        }

        dayAdapter.submitList(CalendarMonthUtil(context, year, month).createMonthDayList())
        dayAdapter.setOnClickItemListener {
            clickItemListener?.invoke(it)
        }
    }

}