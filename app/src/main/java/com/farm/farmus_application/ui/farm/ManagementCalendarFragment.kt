package com.farm.farmus_application.ui.farm

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.farm.farmus_application.R
import com.farm.farmus_application.databinding.FragmentManagementCalendarBinding
import com.farm.farmus_application.ui.farm.adapter.ManagementRVAdapter
import com.farm.farmus_application.ui.farm.adapter.OnDeleteClickListener
import com.farm.farmus_application.viewmodel.farm.ManagementCalendarViewModel
import com.prolificinteractive.materialcalendarview.CalendarDay
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import java.util.TimeZone

@AndroidEntryPoint
class ManagementCalendarFragment : Fragment(), OnDeleteClickListener {

    private lateinit var binding: FragmentManagementCalendarBinding
    private lateinit var firstSelectedDay: CalendarDay
    private lateinit var lastSelectedDay: CalendarDay
    private var farmId = 0
    private val managementCalendarViewModel: ManagementCalendarViewModel by viewModels()
    private val managementRVAdapter = ManagementRVAdapter(this)


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_management_calendar,
            container,
            false
        )
        farmId = arguments?.getInt("farmId") ?: 0
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView()

        // 뒤로 가기 버튼
        binding.managementCalendarBack.setOnClickListener {
            activity?.supportFragmentManager?.apply {
                beginTransaction().remove(this@ManagementCalendarFragment).commit()
                popBackStack()
            }
        }


        binding.managementCalendar.setOnDateChangedListener { widget, date, _ ->
            if(managementCalendarViewModel.isSelectedDateUnavailable(date)){
                Toast.makeText(context, "이미 등록된 날짜입니다.", Toast.LENGTH_SHORT).show()
                return@setOnDateChangedListener
            }

            widget.apply {
                removeDecorators()
                addDecorator(UnavailableDatesDecorator(managementCalendarViewModel.unavailableDates.value ?: listOf()))
                addDecorator(SelectedDecorator(date))
                addDecorators(
                    TodayDecorator(requireContext()),
                    BeforeDayDecorator(),
                    SelectedDecorator(date)
                )
            }
            settingCalendarStartText(date)
            activateAddButton(false)
        }

        binding.managementCalendar.setOnRangeSelectedListener { widget, dates ->
            val dayList = mutableListOf<CalendarDay>().apply {
                addAll(dates)
                removeAt(0)
                removeAt(this.size - 1)
            }
            if (firstSelectedDay != dates[0]) {
                widget.addDecorator(SelectedDecorator(dates[0]))
                settingCalendarStartText(dates[0])
            } else {
                widget.addDecorator(SelectedDecorator(dates[dates.size - 1]))
            }
            if (dayList.size != 0) {
                widget.addDecorator(RangeDayDecorator(dayList, requireContext()))
            }
            settingCalendarLastText(dates[dates.size - 1])
            activateAddButton(true)
        }

        // '추가' 버튼 클릭 이벤트
        binding.managementCalendarAdd.setOnClickListener {
            if (binding.managementCalendarAdd.isSelected) {
                val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                dateFormat.timeZone = TimeZone.getTimeZone("UTC")

                // CalendarDay를 Calendar로 변환
                val startCalendar = Calendar.getInstance().apply {
                    set(firstSelectedDay.year, firstSelectedDay.month - 1, firstSelectedDay.day)
                }
                val endCalendar = Calendar.getInstance().apply {
                    set(lastSelectedDay.year, lastSelectedDay.month - 1, lastSelectedDay.day)
                }

                // 포맷된 날짜 문자열 생성
                val unavailableStartDate = dateFormat.format(startCalendar.time)
                val unavailableEndDate = dateFormat.format(endCalendar.time)

                managementCalendarViewModel.addUnavailableDate(
                    farmId,
                    unavailableStartDate,
                    unavailableEndDate
                )
            }
        }

        // 이용 불가 기간 추가 버튼 클릭 후 결과 관찰
        managementCalendarViewModel.isSuccessAddDate.observe(viewLifecycleOwner) {
            if (it) {
                managementCalendarViewModel.addUnavailableDate(
                    farmId,
                    firstSelectedDay.toString(),
                    lastSelectedDay.toString()
                )
            }
        }

        managementCalendarViewModel.isSuccessDeleteDate.observe(viewLifecycleOwner) {
            if (it) {
                Toast.makeText(context, "삭제되었습니다.", Toast.LENGTH_SHORT).show()
            }
        }


        // 이용 불가 기간 목록 전체 확인 -> 리사이클러 뷰로 아이템 넣기
        managementCalendarViewModel.unavailableDateInfoList.observe(viewLifecycleOwner) {
            managementRVAdapter.submitList(it) // 리사이클러 뷰에 불가 기간 표기
        }

        managementCalendarViewModel.unavailableDates.observe(viewLifecycleOwner) {
            updateCalendarWithUnavailableDates(it)
        }

        managementCalendarViewModel.errorMessage.observe(viewLifecycleOwner) { errorMessage ->
            Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show()
        }


    }

    private fun initView() {
        val date = CalendarDay.today()
        settingCalendarStartText(date)
        settingCalendarLastText(date)

        binding.managementCalendar.apply {
            addDecorator(TodayDecorator(requireContext()))
            addDecorator(BeforeDayDecorator())
            setTitleFormatter { day -> "${day.month}월${day.year}년" }
            setLeftArrow(R.drawable.calendar_back_button_vector)
            setRightArrow(R.drawable.calendar_forward_button_vector)
        }

        binding.dateRecyclerView.layoutManager = LinearLayoutManager(context)
        binding.dateRecyclerView.adapter = managementRVAdapter;

        managementCalendarViewModel.getUnavailableDateList(farmId)
    }

    private fun settingCalendarStartText(date: CalendarDay) {
        binding.apply {
            calendarStartDayYear.text = date.year.toString()
            calendarStartDayMonth.text = String.format("%02d", date.month)
            calendarStartDayDate.text = date.day.toString()
        }
        firstSelectedDay = date
    }

    private fun settingCalendarLastText(date: CalendarDay) {
        binding.apply {
            calendarLastDayYear.text = date.year.toString()
            calendarLastDayMonth.text = String.format("%02d", date.month)
            calendarLastDayDate.text = date.day.toString()
        }
        lastSelectedDay = date
    }

    private fun activateAddButton(switch: Boolean) {
        if (switch) {
            binding.apply {
                startDayConstraint.isSelected = true
                lastDayConstraint.isSelected = false
                managementCalendarAdd.setTextColor(Color.GREEN)
                managementCalendarAdd.isSelected = true
            }
        } else {
            binding.apply {
                startDayConstraint.isSelected = false
                lastDayConstraint.isSelected = true
                managementCalendarAdd.setTextColor(Color.GRAY)
                managementCalendarAdd.isSelected = false
            }
        }
    }

    override fun onDeleteClick(farmDateID: Int) {
        managementCalendarViewModel.deleteUnavailableDate(farmDateID, farmId)
    }

    private fun updateCalendarWithUnavailableDates(unavailableDates: List<CalendarDay>){
        binding.managementCalendar.apply {
            removeDecorators()
            addDecorator(UnavailableDatesDecorator(unavailableDates))
        }
    }
}
