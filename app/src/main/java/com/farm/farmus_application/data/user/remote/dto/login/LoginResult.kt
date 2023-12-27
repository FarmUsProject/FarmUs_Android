package com.farm.farmus_application.data.user.remote.dto.login

import com.google.gson.annotations.SerializedName

data class LoginResult(
    @SerializedName("accesstoken") val accesstoken: String,
    @SerializedName("profile") val profile : String, // profile img url
    @SerializedName("status") val status: Boolean
)