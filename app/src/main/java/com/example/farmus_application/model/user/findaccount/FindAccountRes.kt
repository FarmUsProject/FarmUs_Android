package com.example.farmus_application.model.user.findaccount

import com.google.gson.annotations.SerializedName

data class FindAccountRes(
    @SerializedName("result") val result: Boolean,
    @SerializedName("code") val code: Int,
    @SerializedName("message") val message: String
)