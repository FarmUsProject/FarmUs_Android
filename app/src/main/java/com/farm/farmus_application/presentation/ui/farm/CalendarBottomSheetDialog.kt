package com.farm.farmus_application.presentation.ui.farm

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Bundle
import android.text.style.ForegroundColorSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import com.farm.farmus_application.R
import com.farm.farmus_application.databinding.DialogBottomSheetCalendarBinding
import com.farm.farmus_application.model.farm.detail.DetailResult
import com.farm.farmus_application.data.reserve.remote.dto.request.ReserveRequestReq
import com.farm.farmus_application.data.reserve.remote.dto.unbookable.UnBookableResult
import com.farm.farmus_application.UserPrefsStorage
import com.farm.farmus_application.utils.JWTUtils
import com.farm.farmus_application.presentation.viewmodel.calendar.CalendarViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.prolificinteractive.materialcalendarview.DayViewDecorator
import com.prolificinteractive.materialcalendarview.DayViewFacade
import dagger.hilt.android.AndroidEntryPoint
import java.time.*
import java.time.temporal.ChronoUnit
import java.util.stream.Collectors
import java.util.stream.Stream

@AndroidEntryPoint
class CalendarBottomSheetDialog(private val farmDetail: DetailResult): BottomSheetDialogFragment() {

    private lateinit var binding: DialogBottomSheetCalendarBinding
//    private lateinit var calendarViewModel: CalendarBottomSheetViewModel
    private val calendarViewModel: CalendarViewModel by viewModels()
    private lateinit var firstSelectedDay: CalendarDay
    private lateinit var lastSelectedDay: CalendarDay
    private var unBookDayList: List<UnBookableResult> = listOf()

    private val tokenBody = JWTUtils.decoded(UserPrefsStorage.accessToken.toString())!!.tokenBody

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = DataBindingUtil.inflate(inflater, R.layout.dialog_bottom_sheet_calendar, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initCalendarDialog()

        calendarViewModel.isSuccessReserve.observe(viewLifecycleOwner) { result ->
            if (result.isSuccess) {
                Toast.makeText(requireContext(), result.message, Toast.LENGTH_SHORT).show()
                calendarViewModel.getFarmerPhoneNumber(farmDetail.FarmID)
            } else {
                Toast.makeText(requireContext(), result.toString(), Toast.LENGTH_SHORT).show()
            }
        }

        calendarViewModel.unBookable.observe(viewLifecycleOwner) { result ->
            if (result.isSuccess) {
//                val resultList = result.result
//                unBookDayList = if (resultList.isNullOrEmpty()) {
//                    listOf()
//                } else {
//                    binding.bottomSheetCalendar.addDecorator(UnBookableDayDecorator(resultList))
//                    resultList
//                }
                result.result?.let {
                    binding.bottomSheetCalendar.addDecorator(UnBookableDayDecorator(it))
                    unBookDayList = it
                }
            } else {
                Toast.makeText(requireContext(), result.message, Toast.LENGTH_SHORT).show()
            }
        }

        calendarViewModel.farmerPhoneNumber.observe(viewLifecycleOwner) { result ->
            val uri = Uri.parse("sms:${result.PhoneNumber}")
            val intent = Intent(Intent.ACTION_SENDTO, uri)
            startActivity(intent)
        }

        binding.bottomSheetCalendar.setOnDateChangedListener { widget, date, _ ->
            widget.apply {
                removeDecorators()
                addDecorator(SelectedDecorator(date))
                addDecorators(TodayDecorator(requireContext()), BeforeDayDecorator(),SelectedDecorator(date))
                addDecorator(UnBookableDayDecorator(unBookDayList))
            }
            settingCalendarStartText(date)
            settingButtonSelect(false)
        }

        binding.bottomSheetCalendar.setOnRangeSelectedListener { widget, dates ->
            val dayList = mutableListOf<CalendarDay>().apply {
                addAll(dates)
                removeAt(0)
                removeAt(this.size -1)
            }
            if (firstSelectedDay != dates[0]) {
                widget.addDecorator(SelectedDecorator(dates[0]))
                settingCalendarStartText(dates[0])
            } else {
                widget.addDecorator(SelectedDecorator(dates[dates.size -1]))
            }
            if (dayList.size != 0) {
                widget.addDecorator(RangeDayDecorator(dayList, requireContext()))
            }
            settingCalendarLastText(dates[dates.size-1])
            settingButtonSelect(true)
        }

        binding.applicationButton.setOnClickListener {
            val email = tokenBody.email ?: ""
            val reserveRequestReq = ReserveRequestReq(
                email = email,
                farmId = farmDetail.FarmID.toString(),
                startDate = firstSelectedDay.date.toString(),
                endDate = lastSelectedDay.date.toString()
            )
            calendarViewModel.postReserveRequest(reserveRequestReq)
        }
    }

    private fun initCalendarDialog() {
        val today = CalendarDay.today()
        settingCalendarStartText(today)
        settingCalendarLastText(today)
        calendarViewModel.getReserveUnBookable(farmDetail.FarmID.toString())
        binding.bottomSheetCalendar.apply {
            addDecorator(TodayDecorator(requireContext()))
            addDecorator(BeforeDayDecorator())
            setTitleFormatter { day -> "${day.month}월${day.year}년" }
            setLeftArrow(R.drawable.calendar_back_button_vector)
            setRightArrow(R.drawable.calendar_forward_button_vector)
        }
    }

    private fun settingCalendarStartText(date: CalendarDay) {
        binding.apply {
            calendarStartDayYear.text = date.year.toString()
            calendarStartDayMonth.text = String.format("%02d",date.month)
            calendarStartDayDate.text = date.day.toString()
        }
        firstSelectedDay = date
    }

    private fun settingCalendarLastText(date: CalendarDay) {
        binding.apply {
            calendarLastDayYear.text = date.year.toString()
            calendarLastDayMonth.text = String.format("%02d",date.month)
            calendarLastDayDate.text = date.day.toString()
        }
        lastSelectedDay = date
    }

    private fun settingButtonSelect(selected: Boolean) {
        if (selected) {
            binding.apply {
                startDayConstraint.isSelected = false
                lastDayConstraint.isSelected = true
                applicationButton.isEnabled = true
            }
        } else {
            binding.apply {
                startDayConstraint.isSelected = true
                lastDayConstraint.isSelected = false
                applicationButton.isEnabled = false
            }
        }
    }

}

class TodayDecorator(context: Context) : DayViewDecorator {

    private var drawable: Drawable = ContextCompat.getDrawable(context, R.drawable.calendar_today_background)!!

    override fun shouldDecorate(day: CalendarDay): Boolean {
        return day == CalendarDay.today()
    }

    override fun decorate(view: DayViewFacade?) {
        view?.setSelectionDrawable(drawable)
        view?.setDaysDisabled(true)
        view?.let {
            it.setSelectionDrawable(drawable)
            it.setDaysDisabled(true)
        }
    }
}

class BeforeDayDecorator: DayViewDecorator {

    override fun shouldDecorate(day: CalendarDay?): Boolean {
        return day?.isBefore(CalendarDay.today()) ?: false
    }

    override fun decorate(view: DayViewFacade?) {
        view?.let {
            it.addSpan(ForegroundColorSpan(Color.parseColor("#cccccc")))
            it.setDaysDisabled(true)
        }
    }
}

class UnBookableDayDecorator(private val unBookableDays: List<UnBookableResult>): DayViewDecorator {
    override fun shouldDecorate(day: CalendarDay): Boolean {
        return convertCalendarDay().contains(day)
    }

    override fun decorate(view: DayViewFacade?) {
        view?.let {
            it.addSpan(ForegroundColorSpan(Color.parseColor("#cccccc")))
            it.setDaysDisabled(true)
        }
    }

    private fun convertCalendarDay(): MutableList<CalendarDay> {
        val unBookDays = mutableListOf<CalendarDay>()
        for (unBookDay in unBookableDays) {
            val stDay = LocalDate.parse(unBookDay.startAt.substring(0 until 10))
            val endDay = LocalDate.parse(unBookDay.endAt.substring(0 until 10))
            val numOfBetween = ChronoUnit.DAYS.between(stDay, endDay)
            val unBookableList = Stream.iterate(stDay) {date -> date.plusDays(1) }
                .limit(numOfBetween + 1)
                .collect(Collectors.toList())
            unBookDays.addAll(unBookableList.toMutableList().map {
                CalendarDay.from(it.year, it.monthValue, it.dayOfMonth)
            })
        }
        return unBookDays
        }
}

class RangeDayDecorator(
    private val days: List<CalendarDay>,
    context: Context
): DayViewDecorator {
    private var drawable: Drawable = ContextCompat.getDrawable(context, R.drawable.calendar_range_background)!!

    override fun shouldDecorate(day: CalendarDay?): Boolean {
        return days.contains(day)
    }

    override fun decorate(view: DayViewFacade?) {
        view?.setSelectionDrawable(drawable)
        view?.setDaysDisabled(true)
    }
}

class SelectedDecorator(private val selectedDay: CalendarDay): DayViewDecorator {
    override fun shouldDecorate(day: CalendarDay?): Boolean {
        return day == selectedDay
    }

    override fun decorate(view: DayViewFacade?) {
        view?.addSpan(ForegroundColorSpan(Color.parseColor("#ffffff")))
    }
}



