package com.farm.farmus_application.ui.farm

import android.graphics.Color
import android.text.style.BackgroundColorSpan
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.prolificinteractive.materialcalendarview.DayViewDecorator
import com.prolificinteractive.materialcalendarview.DayViewFacade

class UnavailableDatesDecorator(private val dates: List<CalendarDay>) : DayViewDecorator {
    override fun shouldDecorate(day: CalendarDay?): Boolean {
        return dates.contains(day)
    }

    override fun decorate(view: DayViewFacade) {
        view.addSpan(object : BackgroundColorSpan(Color.LTGRAY) {})
        view.setDaysDisabled(true)
    }

}