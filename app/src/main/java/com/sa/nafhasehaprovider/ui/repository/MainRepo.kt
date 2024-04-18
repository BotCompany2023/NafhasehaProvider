package com.sa.nafhasehaprovider.ui.repository

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
        transporter_id: RequestBody,
        categories: List<Int>
    ) = api.register(provider_type,
        name,
        country_id,phone,email,password,address,lat,long,city_id,area_id,commercialRegister,
        services_from_home,transporter_id,categories)

    suspend fun checkPhoneUser(
        phone: String, countryId: Int
    ) = api.checkPhone(phone, countryId)


    suspend fun changePassword(
        oldPassword: String,newPassword: String
    ) = api.changePassword(oldPassword, newPassword)

    suspend fun checkOtpCode(
        provider_id: Int, code: String
    ) = api.checkCode(provider_id, code)

    suspend fun checkCodeReset(
        provider_id: Int, code: String
    ) = api.checkCodeReset(provider_id, code)

    suspend fun sendActivationCode(
        userId: Int, type: String
    ) = api.sendActivationCode(userId, type)

    suspend fun resetPasswordUser(
        userId: Int, newPassword: String
    ) = api.resetPassword(userId, newPassword)

    suspend fun getProfileUser() = api.getProfile()


    suspend fun editProfile(
        image: MultipartBody.Part?,
        provider_type: RequestBody,
        name: RequestBody,
        country_id: RequestBody,
        phone: RequestBody,
        email: RequestBody,
        address: RequestBody,
        lat: RequestBody,
        long: RequestBody,
        city_id: RequestBody,
        area_id: RequestBody,
        services_from_home: RequestBody,
        transporter_id: RequestBody,
        categories: List<Int>
    ) = api.editProfile(
        image,provider_type,name,country_id,
        phone,email,address,lat,long,city_id,area_id, services_from_home,transporter_id, categories
    )

    suspend fun notification(page: Int, countPaginate: Int) = api.getNotification(page,countPaginate)
    suspend fun saveToken(fcmToken:String) = api.saveToken(fcmToken)
    suspend fun showAllNotification() = api.showAllNotification()


    suspend fun countNotification() = api.getCountNotification()


    suspend fun home(page: Int, countPaginate: Int) = api.getHome(page, countPaginate)

    suspend fun showPackage(packageId: Int) = api.showPackage(packageId)

    suspend fun wallet(page: Int, countPaginate: Int) = api.myWallet(page, countPaginate)
    suspend fun getBanks() = api.getBanks()
    suspend fun requestWithdrawal(bank_id:Int,full_name:String,Iban:String,amount:Int) = api.requestWithdrawal(bank_id, full_name,Iban, amount)
    suspend fun getCar(page: Int, countPaginate: Int) = api.getCar(page, countPaginate)
    suspend fun getCarType(page: Int, countPaginate: String) = api.getCarType(page, countPaginate)

    suspend fun getCarBrands(typeId: Int, page: Int, countPaginate: String) =
        api.getCarBrands(typeId, page, countPaginate)

    suspend fun getCarModels(brandId: Int, page: Int, countPaginate: String) =
        api.getCarModels(brandId, page, countPaginate)

    suspend fun getCarYears(page: Int, countPaginate: String) = api.getCarYears(page, countPaginate)



    suspend fun categories(page: Int) = api.getCategories(page)



    suspend fun ordersApproved(page: Int) = api.getOrdersApproved(page)
    suspend fun ordersCompleted(page: Int, countPaginate: Int) = api.getOrdersCompleted(page, countPaginate)
    suspend fun showOrder(idOrder: Int) = api.showOrder(idOrder)
    suspend fun cancelOrderOngoing(idOrder: Int) = api.cancelOrderOngoing(idOrder)
    suspend fun changeStatusGetOrders() = api.changeStatusGetOrders()

    suspend fun submitPriceOffer(idOrder: Int,price:String) = api.submitPriceOffer(idOrder,price)

    suspend fun getCanceledReasons() = api.getCanceledReasons()

    suspend fun cancelOrder(idOrder: Int,cancelReasonId: Int) = api.cancelOrder(idOrder, cancelReasonId)
    suspend fun acceptedOrder(idOrder: Int) = api.acceptedOrder(idOrder)
    suspend fun storeCompletedOrder(idOrder: Int) = api.storeCompletedOrder(idOrder)
    suspend fun pickUp(idOrder: Int) = api.pickUp(idOrder)
    suspend fun submitReportsOrder(order_id: RequestBody,
                                  date_at: RequestBody,
                                   time_at: RequestBody,
                                    details: RequestBody,
                                   price: RequestBody,
                                   image: List<MultipartBody.Part>) = api.submitReports(order_id, date_at, time_at, details, price,image)


    suspend fun versionUpdate(deviceType: String, currentVersion: String) =
        api.versionUpdate(deviceType,currentVersion)


    suspend fun getVehicleTransporter() = api.getVehicleTransporter()



    suspend fun changeLanguage(defaultLanguage: String) =
        api.changeLanguage(defaultLanguage)



}

