package com.farm.farmus_application.data.token

data class TokenBody(
    val name : String,
    val nickName : String?,
    val email : String,
    val role : String,
    val phoneNumber : String,
    val profile : String?,
    val iat : Int,
    val exp : Int,
    val iss : String,
)
