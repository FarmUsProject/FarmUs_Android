package com.farm.farmus_application.presentation.viewmodel.farm

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.farm.farmus_application.model.farm.myfarm.MyFarmRes
import com.farm.farmus_application.data.reserve.remote.dto.reserve_list.ReserveListRes
import com.farm.farmus_application.repository.farm.FarmRepository
import com.farm.farmus_application.repository.reserve.ReserveRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FarmListViewModel @Inject constructor(
    private val reserveRepo: ReserveRepository,
    private val farmRepo: FarmRepository
): ViewModel() {

    private var _currentFarmList = MutableLiveData<ReserveListRes>()
    var currentFarmList: LiveData<ReserveListRes> = _currentFarmList

    private var _pastFarmList = MutableLiveData<ReserveListRes>()
    var pastFarmList: LiveData<ReserveListRes> = _pastFarmList

    private var _myFarmList = MutableLiveData<MyFarmRes>()
    var myFarmRes: LiveData<MyFarmRes> = _myFarmList

    fun getCurrentList() {
        viewModelScope.launch {
            try {
                val response = reserveRepo.getCurrentList()
                if (response.isSuccessful) {
                    _currentFarmList.postValue(response.body())
                } else {
                    Log.e("CurrentFarmListError:","${response.body()}")
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun getPastList() {
        viewModelScope.launch {
            try {
                val response = reserveRepo.getPastList()
                if (response.isSuccessful) {
                    _pastFarmList.postValue(response.body())
                } else {
                    Log.e("PastFarmListError:","${response.body()}")
                }
            } catch (e: Exception){
                e.printStackTrace()
            }
        }
    }

    fun getMyFarmList() {
        viewModelScope.launch {
            try {
                val response = farmRepo.getMyFarm()
                if (response.isSuccessful) {
                    _myFarmList.postValue(response.body())
                } else {
                    Log.e("getMyFarmListError:","${response.errorBody()}")
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

}