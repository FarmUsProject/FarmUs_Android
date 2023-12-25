package com.farm.farmus_application.viewmodel.farm

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.farm.farmus_application.model.farm.unavailableDate.UnavailableDateAdditionReq
import com.farm.farmus_application.model.farm.unavailableDate.UnavailableDateInfo
import com.farm.farmus_application.repository.farm.FarmDataSourceInterface
import com.prolificinteractive.materialcalendarview.CalendarDay
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

// 아키텍처 구조가 잘못 잡혀 있어서.. 이상하긴 하지만 원래는 Repo interface를 넘겨서 해야함.
// DataSrcInterface라고 명명되어있지만 사실 Repo interface임.
@HiltViewModel
class ManagementCalendarViewModel @Inject constructor(
    private val farmRepository: FarmDataSourceInterface,
) : ViewModel() {
    private val TAG = "ManagementCalendarViewModel"

    private val _isSuccessAddDate = MutableLiveData<Boolean>()
    val isSuccessAddDate: LiveData<Boolean> get() = _isSuccessAddDate

    private val _unavailableDateInfoList = MutableLiveData<List<UnavailableDateInfo>>()
    val unavailableDateInfoList: LiveData<List<UnavailableDateInfo>> = _unavailableDateInfoList

    private val _unavailableDates = MutableLiveData<List<CalendarDay>>()
    val unavailableDates: LiveData<List<CalendarDay>> get() = _unavailableDates

    private val _isSuccessDeleteDate = MutableLiveData<Boolean>()
    val isSuccessDeleteDate: LiveData<Boolean> = _isSuccessDeleteDate

    private val _errorMessage = MutableLiveData<String>() // 에러 메시지 전달용 LiveData
    val errorMessage: LiveData<String> = _errorMessage

    fun addUnavailableDate(farmID: Int, unavailableStartDate: String, unavailableEndDate: String) {
        viewModelScope.launch {
            try {
                // Retrofit 이용해서 API 호출 (로컬 정보를 서버로 보냄)
                val response = farmRepository.postUnavailableDate(
                    UnavailableDateAdditionReq(farmID, unavailableStartDate, unavailableEndDate)
                )
                if (response.isSuccessful) {
                    // message와 farmDateID에 대한 처리 필요
                    response.body()?.let {
                        _isSuccessAddDate.postValue(it.isSuccess)

                        // 추가가 성공적으로 이루어지면 목록을 업데이트(UI 바로 반영)
                        if (it.isSuccess) {
                            getUnavailableDateList(farmID)
                        }

                    }
                } else {
                    _errorMessage.value = "이용 불가 기간 추가에 오류가 있습니다."
                    Log.e(TAG, response.errorBody()?.string() ?: response.message())
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun deleteUnavailableDate(farmDateID: Int, farmID: Int) {
        viewModelScope.launch {
            try {
                val response = farmRepository.putDeleteUnavailableDate(farmDateID)
                response.body()?.let {
                    if (it.isSuccess) {
                        _isSuccessDeleteDate.postValue(true)
                        getUnavailableDateList(farmID)
                    } else {
                        _errorMessage.value = "삭제에 문제가 발생했습니다."
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

    }

    fun getUnavailableDateList(farmID: Int) {
        viewModelScope.launch {
            try {
                val response = farmRepository.getUnavailableDate(farmID)
                response.body()?.let {
                    if (it.isSuccess) {
                        _unavailableDateInfoList.postValue(it.result)

                        val unavailableDatesForCalendar = it.result.flatMap { dateInfo->
                            val startDate = dateInfo.getStartDate()
                            val endDate = dateInfo.getEndDate()
                            val dates = mutableListOf<CalendarDay>()

                            val startCalendar = java.util.Calendar.getInstance().apply { time = startDate }
                            val endCalendar = java.util.Calendar.getInstance().apply { time = endDate }

                            while (!startCalendar.after(endCalendar)) {
                                dates.add(
                                    CalendarDay.from(
                                        startCalendar.get(java.util.Calendar.YEAR),
                                        startCalendar.get(java.util.Calendar.MONTH) + 1, // 월(month) 값 조정
                                        startCalendar.get(java.util.Calendar.DAY_OF_MONTH)
                                    )
                                )
                                startCalendar.add(java.util.Calendar.DAY_OF_MONTH, 1)
                            }
                            dates
                        }
                        _unavailableDates.postValue(unavailableDatesForCalendar)
                    } else {
                        _errorMessage.value = "이용 불가 기간 목록을 불러오는 데 문제가 발생했습니다."
                        Log.e(TAG, response.errorBody()?.string() ?: response.message())
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun isSelectedDateUnavailable(selectedDate: CalendarDay): Boolean {
        return unavailableDates.value?.contains(selectedDate) ?: false
    }


}