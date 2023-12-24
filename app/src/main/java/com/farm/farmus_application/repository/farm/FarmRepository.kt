package com.farm.farmus_application.repository.farm

import com.farm.farmus_application.model.farm.detail.DetailRes
import com.farm.farmus_application.model.farm.detail.DetailResult
import com.farm.farmus_application.model.farm.editinfo.EditinfoRes
import com.farm.farmus_application.model.farm.list.ListRes
import com.farm.farmus_application.model.farm.myfarm.MyFarmRes
import com.farm.farmus_application.model.farm.phone.PhoneNumberRes
import com.farm.farmus_application.model.farm.postings.PostingsRes
import com.farm.farmus_application.model.farm.register.RegisterRes
import com.farm.farmus_application.model.farm.search.SearchedRes import com.farm.farmus_application.model.farm.unavailableDate.DeleteDateRes
import com.farm.farmus_application.model.farm.unavailableDate.RetrieveDateRes
import com.farm.farmus_application.model.farm.unavailableDate.UnavailableDateAdditionReq
import com.farm.farmus_application.model.farm.unavailableDate.UnavailaleDateAdditionRes
import com.farm.farmus_application.model.favorite.FavoriteFarmRes
import com.farm.farmus_application.network.FarmApiClient
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Response
import java.io.File
import java.util.*
import javax.inject.Inject

class FarmRepository @Inject constructor(
    private val farmApiClient: FarmApiClient
) : FarmDataSourceInterface {

    companion object {
        private var tempFarmDetail: DetailResult? = null
    }
    override suspend fun getFarmList(email : String): Response<ListRes> {
        return farmApiClient.getFarmList(email)
    }

    override suspend fun postFarmPostings(
        name: String,
        owner: String,
        startDate: String,
        endDate: String,
        price: String,
        squaredMeters: String,
        locationBig: String,
        locationMid: String,
        locationSmall: String,
        description: String,
        category: String,
        tag: String,
        imageFiles: List<File>
    ): Response<PostingsRes> {
        // 텍스트 데이터를 RequestBody로 변환
        val nameRequestBody = name.toRequestBody("text/plain".toMediaTypeOrNull())
        val ownerRequestBody = owner.toRequestBody("text/plain".toMediaTypeOrNull())
        val startDateRequestBody = startDate.toRequestBody("text/plain".toMediaTypeOrNull())
        val endDateRequestBody = endDate.toRequestBody("text/plain".toMediaTypeOrNull())
        val priceRequestBody = price.toRequestBody("text/plain".toMediaTypeOrNull())
        val squareMetersRequestBody = squaredMeters.toRequestBody("text/plain".toMediaTypeOrNull())
        val locationBigRequestBody = locationBig.toRequestBody("text/plain".toMediaTypeOrNull())
        val locationMidRequestBody = locationMid.toRequestBody("text/plain".toMediaTypeOrNull())
        val locationSmallRequestBody = locationSmall.toRequestBody("text/plain".toMediaTypeOrNull())
        val descriptionRequestBody = description.toRequestBody("text/plain".toMediaTypeOrNull())
        val categoryRequestBody = category.toRequestBody("text/plain".toMediaTypeOrNull())
        val tagRequestBody = tag.toRequestBody("text/plain".toMediaTypeOrNull())

        // 이미지 데이터를 MultipartBody.Part로 변환
        val imageParts = mutableListOf<MultipartBody.Part>()
        imageFiles.forEach { imageFile ->
            val mimeType = when(imageFile.extension.toLowerCase(Locale.ROOT)){
                "jpeg","jpg" -> "image/jpeg"
                "png" -> "image/png"
                else -> return@forEach
            }
            val imageRequestBody = imageFile.asRequestBody(mimeType.toMediaTypeOrNull())
            val imagePart = MultipartBody.Part.createFormData("file", imageFile.path, imageRequestBody)
            imageParts.add(imagePart)
        }

        // API 호출
        return farmApiClient.postFarmPostings(
            name = nameRequestBody,
            owner = ownerRequestBody,
            startDate = startDateRequestBody,
            endDate = endDateRequestBody,
            price = priceRequestBody,
            squaredMeters = squareMetersRequestBody,
            locationBig = locationBigRequestBody,
            locationMid = locationMidRequestBody,
            locationSmall = locationSmallRequestBody,
            description = descriptionRequestBody,
            category = categoryRequestBody,
            tag = tagRequestBody,
            file = imageParts
        )
    }

    override suspend fun patchFarmEditinfo(
        farmId: Int,
        farmName: String,
        farmInfo: String,
        locationBig: String,
        locationMid: String,
        locationSmall: String,
        size: String,
        price: String,
        file: List<File>
    ): Response<EditinfoRes> {
        // 텍스트 데이터를 RequestBody로 변환
        val farmNameRequestBody = farmName.toRequestBody("text/plain".toMediaTypeOrNull())
        val farmInfoRequestBody = farmInfo.toRequestBody("text/plain".toMediaTypeOrNull())
        val locationBigRequestBody = locationBig.toRequestBody("text/plain".toMediaTypeOrNull())
        val locationMidRequestBody = locationMid.toRequestBody("text/plain".toMediaTypeOrNull())
        val locationSmallRequestBody = locationSmall.toRequestBody("text/plain".toMediaTypeOrNull())
        val sizeRequestBody = size.toRequestBody("text/plain".toMediaTypeOrNull())
        val priceRequestBody = price.toRequestBody("text/plain".toMediaTypeOrNull())

        // 이미지 데이터를 MultipartBody.Part로 변환
        val imageParts = mutableListOf<MultipartBody.Part>()
        file.forEach { imageFile ->
            val mimeType = when(imageFile.extension.toLowerCase(Locale.ROOT)){
                "jpeg","jpg" -> "image/jpeg"
                "png" -> "image/png"
                else -> return@forEach
            }
            val imageRequestBody = imageFile.asRequestBody(mimeType.toMediaTypeOrNull())
            val imagePart = MultipartBody.Part.createFormData("file", imageFile.path, imageRequestBody)
            imageParts.add(imagePart)
        }

        return farmApiClient.patchFarmEditinfo(
            farmId = farmId,
            farmName = farmNameRequestBody,
            farmInfo = farmInfoRequestBody,
            locationBig = locationBigRequestBody,
            locationMid = locationMidRequestBody,
            locationSmall = locationSmallRequestBody,
            size = sizeRequestBody,
            price = priceRequestBody,
            file = imageParts
        )
    }

    override suspend fun patchFarmRegister(): Response<RegisterRes> {
        return farmApiClient.patchFarmRegister()
    }

    override suspend fun getFarmSearchKeyword(keyword: String): Response<SearchedRes> {
        return farmApiClient.getFarmSearchKeyword(keyword)
    }

    override suspend fun getFarmSearchByFilter(locationBig : String, locationMid : String): Response<SearchedRes> {
        return farmApiClient.getFarmSearchByFilter(locationBig, locationMid)
    }

    override suspend fun getFarmDetail(farmid: Int): Response<DetailRes> {
        return farmApiClient.getFarmDetail(farmid)
    }

    override suspend fun getFarmerPhoneNumber(farmId: Int): Response<PhoneNumberRes> {
        return farmApiClient.getFarmerPhoneNumber(farmId)
    }

    override suspend fun getMyFarm(): Response<MyFarmRes> {
        return farmApiClient.getMyFarm()
    }

    override suspend fun getFavoriteFarmList(email: String): Response<FavoriteFarmRes> {
        return farmApiClient.getFavoriteFarmList(email)
    }

    // 글 수정을 위한 데이터 넘기기------------------
    suspend fun saveTempFarmDetail(farmDetail: DetailResult?){
        tempFarmDetail = farmDetail
    }

    suspend fun getTempFarmDetail(): DetailResult?{
        val farmDetail = tempFarmDetail
        tempFarmDetail = null
        return farmDetail
    }
    // -----------------------------------------
    override suspend fun postUnavailableDate(params : UnavailableDateAdditionReq) : Response<UnavailaleDateAdditionRes>{
        return farmApiClient.postUnavailableDate(params)
    }

    override suspend fun putDeleteUnavailableDate(farmDateID : Int) : Response<DeleteDateRes>{
        return farmApiClient.putDeleteUnavailableDate(farmDateID)
    }
    override suspend fun getUnavailableDate(farmId : Int) : Response<RetrieveDateRes>{
        return farmApiClient.getUnavailableDate(farmId)
    }
    
}