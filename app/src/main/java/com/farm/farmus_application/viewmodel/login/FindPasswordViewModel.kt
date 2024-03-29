package com.farm.farmus_application.viewmodel.login

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.farm.farmus_application.model.user.findpassword.FindPasswordRes
import com.farm.farmus_application.repository.user.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject
@HiltViewModel
class FindPasswordViewModel @Inject constructor(
    private val userRepository: UserRepository
): ViewModel(){
    val findPasswordResponse : MutableLiveData<FindPasswordRes> = MutableLiveData()

    fun findPassword(userEmail: String){
        viewModelScope.launch {
            try {
                val response = userRepository.getUserFindPassword(userEmail = userEmail)
                if(response.isSuccessful){
                    Log.d("FindPassword Communication Success: ", response.body().toString())
                    val responseBody = response.body()
                    responseBody?.let {
                        if(it.result){
                            // 비밀번호를 찾은 경우
                            Log.d("FindPassword Success : ", response.body().toString())
                            findPasswordResponse.postValue(it)
                        }else{
                            // 비밀번호를 찾지 못한 경우
                            Log.d("FindPassword Failed : ", response.body().toString())
                            findPasswordResponse.postValue(it)
                        }
                    }
                }else{
                    Log.d("FindPassword Communication Success: ", response.body().toString())
                    response.message()
                }
            }catch (e : Exception){
                e.printStackTrace()
            }
        }
    }
}