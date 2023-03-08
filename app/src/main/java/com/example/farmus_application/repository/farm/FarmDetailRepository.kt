package com.example.farmus_application.repository.farm

import com.example.farmus_application.model.FarmDetail

class FarmDetailRepository(private val remoteDataSource: FarmDetailRemoteDataSource) {

    // suspend: Coroutine Scope에서만 Method가 실행되도록 함.
    suspend fun getFarmList(): List<FarmDetail> {

        /**Coroutine이 실행될 Thread 변경, Retrofit2 사용시 필요 X
        withContext(Dispatchers.IO) {
        remoteDataSource.getCategories()
        }
         */
        return remoteDataSource.getFarmList()
    }
}