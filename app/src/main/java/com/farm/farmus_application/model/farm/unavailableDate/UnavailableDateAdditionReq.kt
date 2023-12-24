package com.farm.farmus_application.model.farm.unavailableDate

import com.google.gson.annotations.SerializedName

// 농장주가 농장 이용불가 기간 추가 하는 API의 Req
data class UnavailableDateAdditionReq(
    @SerializedName("farmID") val farmID : Int,
    @SerializedName("unavailableStartDate") val unavailableStartDate : String,
    @SerializedName("unavailableEndDate") val unavailableEndDate : String
)
