package com.neobaran.open.neocalendar.view

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.widget.RelativeLayout
import androidx.core.os.ConfigurationCompat
import androidx.viewpager2.widget.ViewPager2
import com.neobaran.open.neocalendar.R
import com.neobaran.open.neocalendar.adapter.MonthAdapter
import com.neobaran.open.neocalendar.bean.DayBean
import com.neobaran.open.neocalendar.bean.MonthBean
import com.neobaran.open.neocalendar.day
import com.neobaran.open.neocalendar.month
import com.neobaran.open.neocalendar.year
import kotlinx.android.synthetic.main.month_view_pager.view.*
import java.util.*

class MonthViewPager @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0, defStyleRes: Int = 0
) : RelativeLayout(context, attrs, defStyleAttr, defStyleRes) {

    private val list = arrayListOf<MonthBean>()
    private val adapter = MonthAdapter(list)

    private val selectedDay: DayBean = DayBean(1970, 0, 1)

    private var monthSelectedListener: ((month: MonthBean) -> Unit)? = null
    private var daySelectedListener: ((day: DayBean) -> Unit)? = null

    fun setMonthSelectedListener(l: (month: MonthBean) -> Unit) {
        monthSelectedListener = l
    }

    fun setDaySelectedListener(l: (day: DayBean) -> Unit) {
        daySelectedListener = l
    }

    init {
        View.inflate(context, R.layout.month_view_pager, this)
        month_view_pager.adapter = adapter
        month_view_pager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                monthSelectedListener?.invoke(list[position])
                if (position == list.size - 1) {
                    list.add(list[position].createNextMonth())
                    adapter.notifyItemChanged(position + 1)
                } else if (position == 0) {
                    list.add(0, list[position].createPrevMonth())
                    adapter.notifyDataSetChanged()
                    month_view_pager.setCurrentItem(1, false)
                }
            }
        })
        adapter.setDaySelectedListener {
            updateSelectedDay(it.year, it.month, it.day)
            if (it.type == -1) {
                prevMonth()

            } else if (it.type == 1) {
                nextMonth()

            }

        }
    }

    fun initData() {
        val cal =
            Calendar.getInstance(ConfigurationCompat.getLocales(resources.configuration)[0]).apply {
                //设置今日
                updateSelectedDay(year(), month(), day())
                set(Calendar.YEAR, 1970)
                set(Calendar.MONTH, 0)
            }

        for (i in 0 until ((Calendar.getInstance().year() - cal.year() + 1) * 12 + cal.month() + 2)) {
            list.add(MonthBean(cal.year(), cal.month()))
            cal.add(Calendar.MONTH, 1)
        }

        adapter.notifyDataSetChanged()
        month_view_pager.setCurrentItem(list.size - 3, false)
    }

    fun nextMonth() {
        month_view_pager.setCurrentItem(month_view_pager.currentItem + 1, true)
        adapter.notifyItemChanged(month_view_pager.currentItem)
    }

    fun prevMonth() {
        month_view_pager.setCurrentItem(month_view_pager.currentItem - 1, true)
        adapter.notifyItemChanged(month_view_pager.currentItem)
    }

    private fun updateSelectedDay(year: Int, month: Int, day: Int) {
        selectedDay.update(year, month, day)
        adapter.setSelectedDay(selectedDay)
//        adapter.notifyItemChanged(month_view_pager.currentItem)
        daySelectedListener?.invoke(selectedDay)
    }

}