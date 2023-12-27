package com.farm.farmus_application.data.farm.remote.dto.unavailableDate

import com.google.gson.annotations.SerializedName

data class DeleteDateRes(
    @SerializedName("isSuccess") val isSuccess : Boolean,
    @SerializedName("code") val code : Int,
    @SerializedName("message") val message : String,
    @SerializedName("result") val result : FarmDataID
)
