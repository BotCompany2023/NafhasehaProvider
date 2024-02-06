package com.sa.nafhasehaprovider.network

import com.sa.nafhasehaprovider.entity.response.versionUpdateResponse.VersionUpdateResponse
import com.sa.nafhasehaprovider.entity.response.canceledReasonsResponse.CanceledReasonsResponse
import com.sa.nafhasehaprovider.entity.response.addCarResponse.AddCarResponse
import com.sa.nafhasehaprovider.entity.response.areasResponse.AreasResponse
import com.sa.nafhasehaprovider.entity.response.authenticationResponse.AuthResponse
import com.sa.nafhasehaprovider.entity.response.cancelOrderResponse.CancelOrderResponse
import com.sa.nafhasehaprovider.entity.response.carBrandsResponse.CarBrandsResponse
import com.sa.nafhasehaprovider.entity.response.carModelsResponse.CarModelsResponse
import com.sa.nafhasehaprovider.entity.response.carYearsResponse.CarYearsResponse
import com.sa.nafhasehaprovider.entity.response.categoriesResponse.CategoriesResponse
import com.sa.nafhasehaprovider.entity.response.cityResponse.CityResponse
import com.sa.nafhasehaprovider.entity.response.countNotificationResponse.CountNotificationResponse
import com.sa.nafhasehaprovider.entity.response.fqResponse.FaqsResponse
import com.sa.nafhasehaprovider.entity.response.generalResponse.GeneralResponse
import com.sa.nafhasehaprovider.entity.response.getBanksResponse.GetBanksResponse
import com.sa.nafhasehaprovider.entity.response.homeResponse.HomeResponse
import com.sa.nafhasehaprovider.entity.response.iconSocialMediaResponse.IconSocialMediaResponse
import com.sa.nafhasehaprovider.entity.response.infoResponse.InfoResponse
import com.sa.nafhasehaprovider.entity.response.myCarResponse.MyCarResponse
import com.sa.nafhasehaprovider.entity.response.notificationResponse.NotificationResponse
import com.sa.nafhasehaprovider.entity.response.ordersResponse.OrdersResponse
import com.sa.nafhasehaprovider.entity.response.providerDataResponse.ProviderDataResponse
import com.sa.nafhasehaprovider.entity.response.providesMapResponse.ProvidesMapResponse
import com.sa.nafhasehaprovider.entity.response.showOrderResponse.ShowOrderResponse
import com.sa.nafhasehaprovider.entity.response.showPackageResponse.ShowPackageResponse
import com.sa.nafhasehaprovider.entity.response.typeCarResponse.CarTypeResponse
import com.sa.nafhasehaprovider.entity.response.vehicleTransporterResponse.VehicleTransporterResponse
import com.sa.nafhasehaprovider.entity.response.walletResponse.WalletResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.*


interface APIEndPoint {


    @GET("cities")
    suspend fun cities(
        @Query("country_id") countryId: Int,
        @Query("count_paginate") countPaginate: String,
    ): Response<CityResponse>


    @GET("areas")
    suspend fun areas(
        @Query("city_id") cityId: Int,
        @Query("count_paginate") countPaginate: String,
    ): Response<AreasResponse>

    @GET("logout")
    suspend fun logOut(): Response<GeneralResponse>


    @POST("login")
    suspend fun login(
        @Query("phone") phone: String,
        @Query("password") password: String,
        @Query("device_token") firebase_token: String
    ): Response<AuthResponse>


    @POST("register")
    @Multipart
    suspend fun register(
        @Part("provider_type") provider_type: RequestBody,
        @Part("name") name: RequestBody,
        @Part("country_id") country_id: RequestBody,
        @Part("phone") phone: RequestBody,
        @Part("email") email: RequestBody,
        @Part("password") password: RequestBody,
        @Part("address") address: RequestBody,
        @Part("lat") lat: RequestBody,
        @Part("long") long: RequestBody,
        @Part("city_id") city_id: RequestBody,
        @Part("area_id") area_id: RequestBody,
        @Part commercialRegister: MultipartBody.Part? = null,
        @Part("services_from_home") services_from_home: RequestBody,
        @Part("transporter_id") transporter_id: RequestBody ? =null,
        @Part("categories[]") categories: List<Int>
    ): Response<AuthResponse>


    @POST("check-phone")
    suspend fun checkPhone(
        @Query("phone") phone: String,
        @Query("country_id") countryId: Int,
        @Query("is_reset") is_reset: Int = 1,
    ): Response<GeneralResponse>



    @POST("change-password")
    suspend fun changePassword(
        @Query("old_password") oldPassword: String, @Query("new_password") newPassword: String
    ): Response<GeneralResponse>


    @POST("check-code")
    suspend fun checkCode(
        @Query("provider_id") userId: Int,
        @Query("code") code: String,
    ): Response<AuthResponse>


    @POST("check-code")
    suspend fun checkCodeReset(
        @Query("provider_id") userId: Int,
        @Query("code") code: String,
    ): Response<GeneralResponse>




    @POST("send-code")
    suspend fun sendActivationCode(
        @Query("provider_id") provider_id: Int,
        @Query("type") type: String,
    ): Response<GeneralResponse>


    @POST("forgot-password")
    suspend fun resetPassword(
        @Query("provider_id") provider_id: Int, @Query("password") password: String
    ): Response<GeneralResponse>

    @GET("profile")
    suspend fun getProfile(): Response<AuthResponse>

    @GET("icons")
    suspend fun getIconsSocialMedia(): Response<IconSocialMediaResponse>


    @GET("infos")
    suspend fun getInfo(@Query("type") type: String): Response<InfoResponse>

    @GET("faqs")
    suspend fun getFaqs(): Response<FaqsResponse>


    @POST("contact-us")
    suspend fun contactUs(
        @Query("title") title: String,
        @Query("country_id") country_id: Int,
        @Query("phone") phone: String,
        @Query("note") note: String
    ): Response<GeneralResponse>


    @POST("edit-profile")
    @Multipart
    suspend fun editProfile(
        @Part image: MultipartBody.Part? = null,
        @Part("provider_type") provider_type: RequestBody,
        @Part("name") nameProvider: RequestBody,
        @Part("country_id") countryId: RequestBody,
        @Part("phone") phone: RequestBody,
        @Part("email") email: RequestBody,
        @Part("address") address: RequestBody,
        @Part("lat") lat: RequestBody,
        @Part("long") lang: RequestBody,
        @Part("city_id") city_id: RequestBody,
        @Part("area_id") area_id: RequestBody,
        @Part("services_from_home") services_from_home: RequestBody,
        @Part("transporter_id")transporter_id: RequestBody,
        @Part("categories[]") categories: List<Int>
    ): Response<AuthResponse>


    @GET("notifications")
    suspend fun getNotification(@Query("page") page: Int,
                                @Query("count_paginate") countPaginate: Int): Response<NotificationResponse>

    @POST("notifications/save_token")
    suspend fun saveToken(@Query("fcm_token")fcmToken:String): Response<GeneralResponse>

    @GET("notifications/count")
    suspend fun getCountNotification(): Response<CountNotificationResponse>


    @GET("home")
    suspend fun getHome(
        @Query("page") page: Int, @Query("count_p aginate") countPaginate: Int
    ): Response<HomeResponse>


    @GET("packages/show")
    suspend fun showPackage(@Query("package_id") packageId: Int): Response<ShowPackageResponse>

    @GET("my-wallet")
    suspend fun myWallet(
        @Query("page") page: Int, @Query("count_paginate") countPaginate: Int
    ): Response<WalletResponse>


    @GET("banks")
    suspend fun getBanks(): Response<GetBanksResponse>


    @POST("request-withdrawal")
    suspend fun requestWithdrawal(@Query("bank_id")bank_id:Int,
    @Query("full_name")full_name:String,
    @Query("iban")Iban:String,
    @Query("amount")amount:Int): Response<GeneralResponse>


    @GET("vehicles/my-vehicles")
    suspend fun getCar(
        @Query("page") page: Int,
        @Query("count_paginate") countPaginate: Int): Response<MyCarResponse>

    @GET("vehicles/types")
    suspend fun getCarType(
        @Query("page") page: Int, @Query("count_paginate") countPaginate: String
    ): Response<CarTypeResponse>

    @GET("vehicles/brands")
    suspend fun getCarBrands(
        @Query("type_id") typeId: Int,
        @Query("page") page: Int,
        @Query("count_paginate") countPaginate: String
    ): Response<CarBrandsResponse>


    @GET("vehicles/models")
    suspend fun getCarModels(
        @Query("vehicle_brand_id") vehicle_brand_id: Int,
        @Query("page") page: Int,
        @Query("count_paginate") countPaginate: String
    ): Response<CarModelsResponse>

    @GET("vehicles/manufacture-years")
    suspend fun getCarYears(
        @Query("page") page: Int, @Query("count_paginate") countPaginate: String
    ): Response<CarYearsResponse>


    @POST("vehicles/store")
    @Multipart
    suspend fun addCar(
        @Part("vehicle_type_id") vehicle_type_id: RequestBody,
        @Part("vehicle_brand_id") vehicle_brand_id: RequestBody,
        @Part("vehicle_model_id") vehicle_model_id: RequestBody,
        @Part("vehicle_manufacture_year_id") vehicle_manufacture_year_id: RequestBody,
        @Part("letters_ar") letters_ar: RequestBody,
        @Part("numbers_ar") numbers_ar: RequestBody,
        @Part("letters_en") letters_en: RequestBody,
        @Part("numbers_en") numbers_en: RequestBody,
        @Part("periodic_inspection") periodic_inspection: RequestBody,
        @Part image: MultipartBody.Part
    ): Response<AddCarResponse>


    @POST("vehicles/edit")
    @Multipart
    suspend fun updateCar(
        @Part("vehicle_id") vehicle_id: RequestBody,
        @Part("vehicle_type_id") vehicle_type_id: RequestBody,
        @Part("vehicle_brand_id") vehicle_brand_id: RequestBody,
        @Part("vehicle_model_id") vehicle_model_id: RequestBody,
        @Part("vehicle_manufacture_year_id") vehicle_manufacture_year_id: RequestBody,
        @Part("letters_ar") letters_ar: RequestBody,
        @Part("numbers_ar") numbers_ar: RequestBody,
        @Part("letters_en") letters_en: RequestBody,
        @Part("numbers_en") numbers_en: RequestBody,
        @Part("periodic_inspection") periodic_inspection: RequestBody,
        @Part image: MultipartBody.Part? = null
    ): Response<AddCarResponse>




    @DELETE("vehicles/delete/{idCar}")
    suspend fun deleteCar(@Path("idCar") idCar: Int): Response<GeneralResponse>


    @GET("vehicles/my-vehicle")
    suspend fun showDataCar(@Query("vehicle_id") idCar: Int): Response<AddCarResponse>


    @POST("services/get-provides-map")
    suspend fun getProvidesMap(
        @Query("lat") lat: Double,
        @Query("long") long: Double,
        @Query("service_id") service_id: Int,

        ): Response<ProvidesMapResponse>


    @PUT("services/view-provide-map/{idProvider}")
    @FormUrlEncoded
    suspend fun getProvidesDataMap(
        @Field("lat") lat: Double,
        @Field("long") long: Double,
        @Path("idProvider") idProvider: Int,

        ): Response<ProviderDataResponse>

    @GET("all-categories")
    suspend fun getCategories(
        @Query("page") page: Int, @Query("count_paginate") countPaginate: String = "ALL"
    ): Response<CategoriesResponse>


    @GET("ongoing-orders")
    suspend fun getOrdersApproved(
        @Query("page") page: Int,
        @Query("count_paginate") countPaginate: String="ALL"
    ): Response<OrdersResponse>

    @GET("completed-orders")
    suspend fun getOrdersCompleted(
        @Query("page") page: Int, @Query("count_paginate") countPaginate: Int
    ): Response<OrdersResponse>


    @GET("order/{idOrder}")
    suspend fun showOrder(@Path("idOrder") idOrder: Int): Response<ShowOrderResponse>


    @POST("cancel-orders-ongoing")
    suspend fun cancelOrderOngoing(@Query("order_id") idOrder: Int): Response<GeneralResponse>

    @GET("change-status-get-orders")
    suspend fun changeStatusGetOrders(): Response<GeneralResponse>


    @POST("submit-price")
    suspend fun submitPriceOffer(
        @Query("order_id") idOrder: Int,
        @Query("price") price: String
    ): Response<ShowOrderResponse>


    @GET("canceled-reasons")
    suspend fun getCanceledReasons(): Response<CanceledReasonsResponse>

    @POST("cancel-orders-accept")
    suspend fun cancelOrder(@Query("order_id") idOrder: Int,
                            @Query("cancel_reason_id") cancelReasonId: Int):
            Response<CancelOrderResponse>

    @POST("accept-order")
    suspend fun acceptedOrder(@Query("order_id") idOrder: Int):
            Response<GeneralResponse>

    @POST("store-complete-order")
    suspend fun storeCompletedOrder(@Query("order_id") idOrder: Int):
            Response<GeneralResponse>


    @POST("pick-up")
    suspend fun pickUp(@Query("order_id") idOrder: Int):
            Response<GeneralResponse>


    @POST("maintenance-report")
    @Multipart
    suspend fun submitReports(
        @Part("order_id") order_id: RequestBody,
        @Part("date_at") date_at: RequestBody,
        @Part("time_at") time_at: RequestBody,
        @Part("details") details: RequestBody,
        @Part("price") price: RequestBody,
        @Part images: List<MultipartBody.Part>? = null
    ): Response<GeneralResponse>



    @GET("update-version")
    suspend fun versionUpdate(@Query("device_type") deviceType: String,
                              @Query("current_version")currentVersion:String
    ): Response<VersionUpdateResponse>

    @GET("transport-vehicles")
    suspend fun getVehicleTransporter(
        @Query("count_paginate") countPaginate: String = "ALL"
    ): Response<VehicleTransporterResponse>


}