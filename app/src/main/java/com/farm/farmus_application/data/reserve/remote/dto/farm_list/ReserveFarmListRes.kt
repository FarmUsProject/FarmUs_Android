package com.farm.farmus_application.data.reserve.remote.dto.farm_list

import com.google.gson.annotations.SerializedName

data class ReserveFarmListRes(
    @SerializedName("isSuccess") val isSuccess : Boolean,
    @SerializedName("code") val code : Int,
    @SerializedName("message") val message : String,
    @SerializedName("result") val result : List<ReserveFarmListResult>?
)
