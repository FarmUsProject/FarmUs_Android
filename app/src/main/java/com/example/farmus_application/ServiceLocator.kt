package com.example.farmus_application

import com.example.farmus_application.network.ApiClient

object ServiceLocator {

    private var apiClient: ApiClient? = null

    fun provideApiClient() : ApiClient {
        return apiClient ?: kotlin.run {
            ApiClient.create().also { newApiClient ->
                apiClient = newApiClient
            }
        }
    }
}