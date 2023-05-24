package com.sa.nafhasehaprovider.repository

import com.sa.nafhasehaprovider.network.APIEndPoint
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.Part

class MainRepo(private val api: APIEndPoint) {



    suspend fun city(countryId: Int, countPaginate: String) = api.cities(countryId, countPaginate)
    suspend fun areas(cityId: Int, countPaginate: String) = api.areas(cityId, countPaginate)
    suspend fun logOut() = api.logOut()


    suspend fun infos(typeInfo: String) = api.getInfo(typeInfo)

    suspend fun iconsSocialMedia() = api.getIconsSocialMedia()
    suspend fun faqs() = api.getFaqs()

    suspend fun loginUser(
        phone: String, password: String, firebase_token: String
    ) = api.login(phone, password, firebase_token)

    suspend fun contactUs(
        title: String, country_id: Int, phone: String,notes: String
    ) = api.contactUs(title,country_id,phone,notes)


    suspend fun registerUser(
        provider_type: RequestBody,
        name: RequestBody,
        country_id: RequestBody,
        phone: RequestBody,
        email: RequestBody,
        password: RequestBody,
        address: RequestBody,
        lat: RequestBody,
        long: RequestBody,
        city_id: RequestBody,
        area_id: RequestBody,
        commercialRegister: MultipartBody.Part? = null,
        services_from_home: RequestBody,
        categories: List<Int>
    ) = api.register(provider_type,
        name,
        country_id,phone,email,password,address,lat,long,city_id,area_id,commercialRegister,
        services_from_home,categories)

    suspend fun checkPhoneUser(
        phone: String, countryId: Int
    ) = api.checkPhone(phone, countryId)

    suspend fun checkOtpCode(
        provider_id: Int, code: String
    ) = api.checkCode(provider_id, code)

    suspend fun sendActivationCode(
        userId: Int, type: String
    ) = api.sendActivationCode(userId, type)

    suspend fun resetPasswordUser(
        userId: Int, newPassword: String
    ) = api.resetPassword(userId, newPassword)

    suspend fun getProfileUser() = api.getProfile()


    suspend fun editProfile(
        image: MultipartBody.Part?,
        name: RequestBody,
        countryId: RequestBody,
        phone: RequestBody,
        email: RequestBody,
        address: RequestBody,
        lat: RequestBody,
        lang: RequestBody,
        city_id: RequestBody,
        area_id: RequestBody
    ) = api.editProfile(image, name, countryId, phone, email, address, lat, lang, city_id, area_id)

    suspend fun notification() = api.getNotification()
    suspend fun home() = api.getHome()

    suspend fun showPackage(packageId: Int) = api.showPackage(packageId)

    suspend fun wallet(page: Int, countPaginate: Int) = api.myWallet(page, countPaginate)
    suspend fun getCar(page: Int, countPaginate: Int) = api.getCar(page, countPaginate)
    suspend fun getCarType(page: Int, countPaginate: String) = api.getCarType(page, countPaginate)

    suspend fun getCarBrands(typeId: Int, page: Int, countPaginate: String) =
        api.getCarBrands(typeId, page, countPaginate)

    suspend fun getCarModels(brandId: Int, page: Int, countPaginate: String) =
        api.getCarModels(brandId, page, countPaginate)

    suspend fun getCarYears(page: Int, countPaginate: String) = api.getCarYears(page, countPaginate)

    suspend fun addCar(
        vehicle_type_id: RequestBody,
        vehicle_brand_id: RequestBody,
        vehicle_model_id: RequestBody,
        vehicle_manufacture_year_id: RequestBody,
        letters_ar: RequestBody,
        numbers_ar: RequestBody,
        letters_en: RequestBody,
        numbers_en: RequestBody,
        periodic_inspection: RequestBody,
        image: MultipartBody.Part
    ) = api.addCar(
        vehicle_type_id,
        vehicle_brand_id,
        vehicle_model_id,
        vehicle_manufacture_year_id,
        letters_ar,
        numbers_ar,
        letters_en,
        numbers_en,
        periodic_inspection,
        image
    )


    suspend fun updateCar(
        vehicle_id: RequestBody,
        vehicle_type_id: RequestBody,
        vehicle_brand_id: RequestBody,
        vehicle_model_id: RequestBody,
        vehicle_manufacture_year_id: RequestBody,
        letters_ar: RequestBody,
        numbers_ar: RequestBody,
        letters_en: RequestBody,
        numbers_en: RequestBody,
        periodic_inspection: RequestBody,
        image: MultipartBody.Part?
    ) = api.updateCar(
        vehicle_id,
        vehicle_type_id,
        vehicle_brand_id,
        vehicle_model_id,
        vehicle_manufacture_year_id,
        letters_ar,
        numbers_ar,
        letters_en,
        numbers_en,
        periodic_inspection,
        image
    )

    suspend fun deleteCar(idCar: Int) = api.deleteCar(idCar)

    suspend fun showDataCar(idCar: Int) = api.showDataCar(idCar)


    suspend fun providesMap(
        lat: Double,
        long: Double,
        service_id: Int
    ) =
        api.getProvidesMap(lat, long, service_id)

    suspend fun providesDataMap(
        lat: Double,
        long: Double,
        idProvider: Int
    ) =
        api.getProvidesDataMap(lat, long, idProvider)

    suspend fun categories(page: Int) = api.getCategories(page)

    suspend fun homeOrCenter() = api.getHomeOrCenter()
    suspend fun getVehicleTransporter() = api.getVehicleTransporter()

    suspend fun checkCouponCode(coupon_code: String, service_id: Int) =
        api.checkCouponCode(coupon_code, service_id)

    //خدمة الفحص الدوري
    suspend fun periodicInspection(
        vehicle_id: Int,
        date_at: String,
        time_at: String,
        city_id: Int,
        coupon_code: String
    ) =
        api.periodicInspection(vehicle_id, date_at, time_at, city_id,coupon_code)



    //خدمة السطحة
    suspend fun transporter(
        transporter_id: Int,
        date_at: String,
        time_at: String,
        address: String,
        lat: Double,
        long: Double,
        address_to: String,
        lat_to: Double,
        long_to: Double,
        details: String,
        coupon_code: String
    ) =
        api.transporter(transporter_id,
            date_at,
            time_at,
            address,
            lat,
            long,
            address_to,
            lat_to,
            long_to,
            details,
            coupon_code)

    //خدمة السطحة
    suspend fun maintenance(
        vehicle_id: RequestBody,
        category_id: RequestBody,
        type_from: RequestBody,
        date_at: RequestBody,
        time_at: RequestBody,
        lat: RequestBody,
        long: RequestBody,
        address: RequestBody,
        details: RequestBody,
        coupon_code: RequestBody,
        images: List<MultipartBody.Part>) =
        api.maintenance(
            vehicle_id,
            category_id,
            type_from,
            date_at,
            time_at,
            lat,
            long,
            address,
            details,
            coupon_code,
            images)



    //خدمة استشاره الاعطال
    suspend fun consultation(
        vehicle_id: RequestBody,
        category_id: RequestBody,
        city_id: RequestBody,
        details: RequestBody,
        coupon_code: RequestBody,
        images: List<MultipartBody.Part>) =
        api.consultation(
            vehicle_id,
            category_id,
            city_id,
            details,
            coupon_code,
            images)


    //خدمة حواجز السيارات
    suspend fun vehicleBarriers(
        vehicle_id: Int,
        position: List<String>,
        date_at: String,
        time_at: String,
        city_id: Int,
        coupon_code: String,
    ) =
        api.vehicleBarriers(
            vehicle_id,
            position,
            date_at,
            time_at,
            city_id,
            coupon_code)


    suspend fun ordersPending() = api.getOrdersPending()
}

