package com.farm.farmus_application.utils

import android.util.Log
import com.farm.farmus_application.data.token.TokenBody
import com.farm.farmus_application.data.token.TokenHeader
import com.farm.farmus_application.data.token.TokenInfo
import com.google.gson.Gson
import java.io.UnsupportedEncodingException

object JWTUtils {
    @JvmStatic
    @Throws(Exception::class)
    fun decoded(JWTEncoded : String) : TokenInfo? {
        try{
            val split = JWTEncoded.split("\\.".toRegex()).toTypedArray()
            Log.d("JWT_DECODED", "Token :" + JWTEncoded)
            Log.d("JWT_DECODED", "Header " + getJson(split[0], TokenHeader::class.java))
            Log.d("JWT_DECODED", "Body " + getJson(split[1], TokenBody::class.java))

            val tokenHeader = getJson(split[0], TokenHeader::class.java)
            val tokenBody = getJson(split[1], TokenBody::class.java)

            return TokenInfo(tokenHeader, tokenBody)

        }catch (e : UnsupportedEncodingException){
            e.printStackTrace()
        }
        return null
    }

    @Throws(UnsupportedEncodingException::class)
    private fun <T>getJson(strEncoded : String, clazz: Class<T>) : T {
        val decodedBytes = android.util.Base64.decode(strEncoded, android.util.Base64.URL_SAFE)
        val jsonString = String(decodedBytes, charset("UTF-8"))
        return Gson().fromJson(jsonString, clazz)
    }
}