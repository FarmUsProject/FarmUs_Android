package com.farm.farmus_application.model.farm.unavailableDate

import com.google.gson.annotations.SerializedName

// 농장주가 농장 이용불가 기간 추가 하는 API의 Res
data class UnavailaleDateAdditionRes(
    @SerializedName("isSuccess") val isSuccess : Boolean,
    @SerializedName("code") val code : String,
    @SerializedName("message") val message : String,
    @SerializedName("result") val result : FarmDataID // List형태로 날라오는 건지 궁금...
)
