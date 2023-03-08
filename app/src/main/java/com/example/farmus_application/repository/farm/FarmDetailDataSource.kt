package com.example.farmus_application.repository.farm

import com.example.farmus_application.model.FarmDetail

interface FarmDetailDataSource {

    suspend fun getFarmList(): List<FarmDetail>
}