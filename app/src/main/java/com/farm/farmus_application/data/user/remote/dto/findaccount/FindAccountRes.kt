package com.farm.farmus_application.data.user.remote.dto.findaccount

import com.google.gson.annotations.SerializedName

data class FindAccountRes(
    @SerializedName("result") val result: Boolean,
    @SerializedName("code") val code : Int,
    @SerializedName("message") val message : String,
    @SerializedName("Name") val name : String,
    @SerializedName("Email") val email : String,
    @SerializedName("Status") val status : String
)