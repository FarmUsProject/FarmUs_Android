package com.farm.farmus_application.data.mypage.remote.dto

import com.google.gson.annotations.SerializedName

data class EditInfoNicknameReq(@SerializedName("nickname") val nickname: String)
data class EditInfoNameReq(@SerializedName("name") val name: String)
data class EditInfoPasswordReq(@SerializedName("password") val password: String)
data class EditInfoPhoneNumberReq(@SerializedName("phoneNumber") val phoneNumber: String)
