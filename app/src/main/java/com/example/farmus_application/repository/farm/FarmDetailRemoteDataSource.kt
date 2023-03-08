package com.example.farmus_application.repository.farm

import com.example.farmus_application.model.FarmDetail
import com.example.farmus_application.network.ApiClient

class FarmDetailRemoteDataSource(private val apiClient: ApiClient): FarmDetailDataSource{

    override suspend fun getFarmList(): List<FarmDetail> {
        return apiClient.getFarmList()
    }
}