package com.farm.farmus_application.data.reserve.remote.dto.reserve_list

data class ReserveListResult(
    val FarmID: Int,
    val OwnerEmail: String,
    val Reserveid: Int,
    val UserEmail: String,
    val endAt: String,
    val startAt: String,
    val Name: String,
    val Picture_url: String?
)