package com.farm.farmus_application.repository.reserve

import com.farm.farmus_application.model.reserve.cancel.ReserveCancelRes
import com.farm.farmus_application.model.reserve.client_list.ReserveClientListRes
import com.farm.farmus_application.model.reserve.farm_list.ReserveFarmListRes
import com.farm.farmus_application.model.reserve.request.ReserveRequestReq
import com.farm.farmus_application.model.reserve.request.ReserveRequestRes
import com.farm.farmus_application.model.reserve.reserve_list.ReserveListRes
import com.farm.farmus_application.model.reserve.status.ReserveStatusRes
import com.farm.farmus_application.model.reserve.unbookable.ReserveUnBookableRes
import com.farm.farmus_application.network.ReserveApiClient
import retrofit2.Response
import javax.inject.Inject
import javax.inject.Singleton

class ReserveRepository @Inject constructor(
    private val reserveApiClient: ReserveApiClient
): ReserveDataSourceInterface {
    override suspend fun postReserveRequest(params: ReserveRequestReq): Response<ReserveRequestRes> {
        return reserveApiClient.postReserveRequest(params)
    }

    override suspend fun getReservedFarmList(farmId: String): Response<ReserveFarmListRes> {
        return reserveApiClient.getReservedFarmList(farmId)
    }

    override suspend fun getReserveClientList(email: String): Response<ReserveClientListRes> {
        return reserveApiClient.getReserveClientList(email)
    }

    override suspend fun putReserveCancel(reserveId: String): Response<ReserveCancelRes> {
        return reserveApiClient.putReserveCancel(reserveId)
    }

    override suspend fun putReserveStatus(
        status: ReserveDataSourceInterface.Status,
        reserveId: String
    ): Response<ReserveStatusRes> {
        return reserveApiClient.putReserveStatus(status = status.toString(), reserveId = reserveId)
    }

    override suspend fun getReserveUnBookable(farmId: String): Response<ReserveUnBookableRes> {
        return reserveApiClient.getReserveUnBookable(farmId)
    }

    override suspend fun getCurrentList(): Response<ReserveListRes> {
        return reserveApiClient.getCurrentList()
    }

    override suspend fun getPastList(): Response<ReserveListRes> {
        return reserveApiClient.getPastList()
    }
}