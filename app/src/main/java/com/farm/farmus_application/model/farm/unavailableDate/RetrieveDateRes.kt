package com.farm.farmus_application.model.farm.unavailableDate

import com.google.gson.annotations.SerializedName
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

data class RetrieveDateRes(
    @SerializedName("isSuccess") val isSuccess : Boolean,
    @SerializedName("code") val code : Int,
    @SerializedName("message") val message : String,
    @SerializedName("result") val result : List<UnavailableDateInfo>
)

data class UnavailableDateInfo(
    @SerializedName("FarmDateID") val FarmDateID : Int,
    @SerializedName("UnavailableStartDate") val UnavailableStartDate : String,
    @SerializedName("UnavailableEndDate") val UnavailableEndDate : String
){
    private fun formatDate(dateString: String): String {
        val parser = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
        val date = parser.parse(dateString)
        val formatter = SimpleDateFormat("yyyy.MM.dd", Locale.getDefault())
        return formatter.format(date ?: Date())
    }

    fun getFormattedStartDate(): String {
        return formatDate(UnavailableStartDate)
    }

    fun getFormattedEndDate(): String {
        return formatDate(UnavailableEndDate)
    }

    private fun parseDate(dateString: String): Date? {
        val parser = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
        return parser.parse(dateString)
    }

    fun getStartDate(): Date? {
        return parseDate(UnavailableStartDate)
    }

    fun getEndDate(): Date? {
        return parseDate(UnavailableEndDate)
    }
}
